/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_seguimiento.modelo;

import com.mycompany.proyecto_seguimiento.clases.conexion;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mauricio Mazier
 */
public class CasoDAO {
    private final Connection conexion;

    public CasoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public List<Caso> obtenerCasosPorProfesor(int profesorCI) throws SQLException {
        List<Caso> lista = new ArrayList<>();
        String sql = "SELECT c.id_caso, c.fecha, c.obs_generales, c.estudiante_CI, " +
                     "e.nombre AS nombre_estudiante, e.apellido AS apellido_estudiante, " +
                     "es.nombre AS especialidad, cu.anhio AS curso, cu.seccion " +
                     "FROM caso c " +
                     "JOIN estudiante e ON c.estudiante_CI = e.CI " +
                     "JOIN inscripcion i ON i.estudiante_CI = e.CI " +
                     "JOIN curso cu ON i.curso_id_curso = cu.id_curso " +
                     "JOIN especialidad es ON cu.especialidad_id_especialidad = es.id_especialidad " +
                     "WHERE c.profesor_CI = ? " +
                     "ORDER BY c.fecha DESC";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, profesorCI);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idCaso = rs.getInt("id_caso");
                    LocalDate fecha = rs.getDate("fecha") != null ? rs.getDate("fecha").toLocalDate() : null;
                    String obs = rs.getString("obs_generales");
                    int estudianteCI = rs.getInt("estudiante_CI");
                    String nombreEst = rs.getString("nombre_estudiante");
                    String apellidoEst = rs.getString("apellido_estudiante");
                    String especialidad = rs.getString("especialidad");
                    String curso = rs.getString("curso");
                    String seccion = rs.getString("seccion");
                    lista.add(new Caso(idCaso, fecha, obs, estudianteCI, nombreEst, apellidoEst, especialidad, curso, seccion));
                }
            }
        }
        return lista;
    }

    public boolean insertarCaso(int profesorCI, int estudianteCI, String obsGenerales, String especialidad, String curso, String seccion) throws SQLException {
        // Busca el id_curso correspondiente
        String sqlCurso = "SELECT cu.id_curso FROM curso cu JOIN especialidad es ON cu.especialidad_id_especialidad = es.id_especialidad " +
                          "WHERE es.nombre = ? AND cu.anhio = ? AND cu.seccion = ? LIMIT 1";
        int idCurso = -1;
        try (PreparedStatement ps = conexion.prepareStatement(sqlCurso)) {
            ps.setString(1, especialidad);
            ps.setString(2, curso);
            ps.setString(3, seccion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idCurso = rs.getInt("id_curso");
                }
            }
        }
        if (idCurso == -1) return false; // no existe el curso

        // Inserta la inscripción si no existe
        String sqlIns = "INSERT IGNORE INTO inscripcion (anhio_lectivo, estudiante_CI, curso_id_curso) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sqlIns)) {
            ps.setString(1, curso);
            ps.setInt(2, estudianteCI);
            ps.setInt(3, idCurso);
            ps.executeUpdate();
        }

        // Inserta el caso
        String sql = "INSERT INTO caso (fecha, obs_generales, activo, profesor_CI, estudiante_CI) VALUES (?, ?, 1, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setString(2, obsGenerales);
            ps.setInt(3, profesorCI);
            ps.setInt(4, estudianteCI);
            return ps.executeUpdate() > 0;
        }
    }
}