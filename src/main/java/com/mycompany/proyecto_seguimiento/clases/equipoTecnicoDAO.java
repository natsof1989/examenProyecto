/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_seguimiento.clases;

import com.mycompany.proyecto_seguimiento.modelo.CasoResumen;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author natha
 */
public class equipoTecnicoDAO {
    
    private final Connection conexion;

    public equipoTecnicoDAO(Connection conexion) {
        this.conexion = conexion;
    }
    
    public List<CasoResumen> obtenerCasosSinEquipo() {
        List<CasoResumen> casos = new ArrayList<>();
        String sql = "SELECT id_caso, fecha, estudiante_nombre, especialidad, curso "
                   + "FROM casos_sin_equipo";

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CasoResumen cr = new CasoResumen();
                cr.setId_caso(rs.getInt("id_caso"));
                // Convertir de Timestamp a LocalDateTime
                Timestamp ts = rs.getTimestamp("fecha");
                if (ts != null) {
                    cr.setFecha(ts.toLocalDateTime());
                }
                cr.setEstudiante(rs.getString("estudiante_nombre"));
                cr.setEspecialidad(rs.getString("especialidad"));
                cr.setCurso(rs.getString("curso"));

                casos.add(cr);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return casos;
    }

    
}
