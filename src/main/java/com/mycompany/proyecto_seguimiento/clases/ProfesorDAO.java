/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_seguimiento.clases;


import com.mycompany.proyecto_seguimiento.modelo.Alumno;
import com.mycompany.proyecto_seguimiento.modelo.Curso;
import com.mycompany.proyecto_seguimiento.modelo.Especialidad;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 *
 * @author natha
 */
public class ProfesorDAO {
    private final Connection conexion;

    public ProfesorDAO(Connection conexion) {
        this.conexion = conexion;
    }

    // Obtener especialidades que enseña ese profesor
    public List<Especialidad> obtenerEspecialidad(String ci) {
        List<Especialidad> especialidades = new ArrayList<>();
        String sql = "SELECT id_especialidad, especialidad " +
                     "FROM vista_profesor_especialidad " +
                     "WHERE profesor_id = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, ci);  // CI del profesor

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id_especialidad");
                    String nombre = rs.getString("especialidad");
                    especialidades.add(new Especialidad(id, nombre));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return especialidades;
    }

    // Obtener los cursos que enseña el profesor en una especialidad
    public List<Curso> obtenerCursos(String ci, int id_especialidad) {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT id_curso, curso " +
                     "FROM vista_profesor_curso " +
                     "WHERE profesor_id = ? AND id_especialidad = ?"; 

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, ci);
            stmt.setInt(2, id_especialidad);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id_curso");  // id_curso ya actualizado en la vista
                    String nombre = rs.getString("curso");
                    cursos.add(new Curso(id, nombre));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cursos;
    }

    // Obtener los alumnos de un curso
    public List<Alumno> obtenerAlumnos(int idCurso) {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT estudiante_id, alumno " +
                     "FROM vista_curso_alumnos " +
                     "WHERE id_curso = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idCurso);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int ci = rs.getInt("estudiante_id");
                    String nombre = rs.getString("alumno");
                    alumnos.add(new Alumno(ci, nombre));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return alumnos;
    }
    
    
    //insertar casos a la base de datos 
    public boolean insertarCaso(String observaciones, int ciProfesor, int ciEstudiante, File archivoSeleccionado) {
        String sql = "INSERT INTO caso (fecha, obs_generales, activo, profesor_CI, estudiante_CI, archivo) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            // Fecha actual
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));

            // Observaciones
            stmt.setString(2, observaciones);

            // Activo por defecto
            stmt.setBoolean(3, true);

            // Claves foráneas
            stmt.setInt(4, ciProfesor);
            stmt.setInt(5, ciEstudiante);

            // Archivo opcional
            if (archivoSeleccionado != null && archivoSeleccionado.exists()) {
                FileInputStream fis = new FileInputStream(archivoSeleccionado);
                stmt.setBinaryStream(6, fis, (int) archivoSeleccionado.length());
                // NOTA: no cerramos fis aquí, lo hace el PreparedStatement después de ejecutar
            } else {
                stmt.setNull(6, Types.BLOB);
            }

            stmt.executeUpdate();
            return true;

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}

