/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_seguimiento.clases;

import java.sql.Connection;
import com.mycompany.proyecto_seguimiento.clases.CasoSeleccionado;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author natha
 */
public class CasoDAO {
    
    private final Connection conexion;

    public CasoDAO(Connection conexion) {
        this.conexion = conexion;
    }
    
    public void cargarDetalleCaso(int idCaso) {
        String sql = "{CALL get_caso_detalle(?)}";

        try (
             CallableStatement stmt = conexion.prepareCall(sql)) {

            stmt.setInt(1, idCaso);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CasoSeleccionado caso = CasoSeleccionado.getInstancia();
                    caso.setDescripcion(rs.getString("descripcion"));
                    caso.setArchivo(rs.getBytes("archivo"));
                    caso.setCiProfesor(String.valueOf(rs.getInt("profesor_CI")));
                    caso.setNombreProfesor(rs.getString("profesor"));
                    String extension = rs.getString("extension");
                    caso.setExtension(extension != null ? extension : "");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
}
