/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_seguimiento.modelo;

/**
 *
 * @author natha
 */


import java.sql.*;
import java.util.*;

import java.sql.SQLException;

import java.sql.PreparedStatement;


public class UsuarioDAO {
    private final Connection conexion;

    public UsuarioDAO(Connection conexion) {
        this.conexion = conexion;
    }

    // Verifica si la CI existe en profesor o equipo_tecnico
    public boolean existeCI(String ci) throws SQLException {
        String sql = "SELECT 1 FROM profesor WHERE CI = ? UNION ALL " +
                     "SELECT 1 FROM equipo_tecnico WHERE CI = ? LIMIT 1";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, ci);
            stmt.setString(2, ci);
            return stmt.executeQuery().next();
        }
    }

    // Obtiene todos los roles del usuario
    public List<String> obtenerRoles(String ci) throws SQLException {
        List<String> roles = new ArrayList<>();
        
        if (esDeRol(ci, "profesor")) {
            roles.add("PROFESOR");
        }
        
        if (esDeRol(ci, "equipo_tecnico")) {
            roles.add("EQUIPO_TECNICO");
        }
        
        return roles;
    }

    // Verifica si el registro está completo (todos los campos obligatorios)
    public boolean registroCompleto(String ci) throws SQLException {
        String sql = "SELECT (SELECT COUNT(*) FROM profesor WHERE CI = ? AND nombre IS NOT NULL AND apellido IS NOT NULL AND password IS NOT NULL) + " +
                     "(SELECT COUNT(*) FROM equipo_tecnico WHERE CI = ? AND nombre IS NOT NULL AND apellido IS NOT NULL AND password IS NOT NULL) > 0 AS completo";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, ci);
            stmt.setString(2, ci);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getBoolean("completo");
        }
    }

    // Valida credenciales usando tu clase Seguridad
    public boolean validarCredenciales(String ci, String contrasenia) throws SQLException {
        String sql = "SELECT password FROM profesor WHERE CI = ? UNION ALL " +
                     "SELECT password FROM equipo_tecnico WHERE CI = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, ci);
            stmt.setString(2, ci);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String hashGuardado = rs.getString("password");
                return Seguridad.verificarPassword(contrasenia, hashGuardado);
            }
            return false;
        }
    }

    // Método para actualizar contraseña
    public boolean actualizarPassword(String ci, String nuevaContrasenia) throws SQLException {
        String hashNuevo = Seguridad.encriptarPassword(nuevaContrasenia);
        
        String sql = "UPDATE profesor SET password = ? WHERE CI = ?; " +
                     "UPDATE equipo_tecnico SET password = ? WHERE CI = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, hashNuevo);
            stmt.setString(2, ci);
            stmt.setString(3, hashNuevo);
            stmt.setString(4, ci);
            
            int updates = stmt.executeUpdate();
            return updates > 0;
        }
    }

    // Método auxiliar para verificar rol específico
    private boolean esDeRol(String ci, String tabla) throws SQLException {
        String sql = String.format("SELECT 1 FROM %s WHERE CI = ? LIMIT 1", tabla);
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, ci);
            return stmt.executeQuery().next();
        }
    }
    
    public boolean existeCiCorreo(String ci, String correo) throws SQLException {
    String sql = "SELECT 1 FROM profesor WHERE CI = ? AND email = ? " +
                 "UNION ALL " +
                 "SELECT 1 FROM equipo_tecnico WHERE CI = ? AND email = ? LIMIT 1";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, ci);
            stmt.setString(2, correo);
            stmt.setString(3, ci);
            stmt.setString(4, correo);

            // executeQuery().next() devolverá true si al menos un registro existe, 
            // ya sea en profesor, equipo_tecnico, o en ambos sin problemas.
            return stmt.executeQuery().next();
        }
    }
    public boolean insertarUsuarioCompletoTransaccional(String ci, String nombre, String apellido, 
                                                   String telefono, String correo, String password) 
                                                   throws SQLException {
        try {
            conexion.setAutoCommit(false); // Iniciamos transacción

            boolean existeEnProfesor = esDeRol(ci, "profesor");
            boolean existeEnEquipoTecnico = esDeRol(ci, "equipo_tecnico");

            if (!existeEnProfesor && !existeEnEquipoTecnico) {
                return false;
            }

            boolean exitoProfesor = false;
            boolean exitoEquipoTecnico = false;

            if (existeEnProfesor) {
                String sqlProfesor = "UPDATE profesor SET nombre = ?, apellido = ?, nro_telefono = ?, email = ?, password = ? WHERE CI = ?";
                try (PreparedStatement stmt = conexion.prepareStatement(sqlProfesor)) {
                    stmt.setString(1, nombre);
                    stmt.setString(2, apellido);
                    stmt.setString(3, telefono);
                    stmt.setString(4, correo);
                    stmt.setString(5, password);
                    stmt.setString(6, ci);
                    exitoProfesor = stmt.executeUpdate() > 0;
                }
            }

            if (existeEnEquipoTecnico) {
                String sqlEquipo = "UPDATE equipo_tecnico SET nombre = ?, apellido = ?, nro_telefono = ?, email = ?, password = ? WHERE CI = ?";
                try (PreparedStatement stmt = conexion.prepareStatement(sqlEquipo)) {
                    stmt.setString(1, nombre);
                    stmt.setString(2, apellido);
                    stmt.setString(3, telefono);
                    stmt.setString(4, correo);
                    stmt.setString(5, password);
                    stmt.setString(6, ci);
                    exitoEquipoTecnico = stmt.executeUpdate() > 0;
                }
            }

            if (exitoProfesor || exitoEquipoTecnico) {
                conexion.commit(); // Confirmamos los cambios
                return true;
            } else {
                conexion.rollback(); // Deshacemos los cambios
                return false;
            }
        } catch (SQLException e) {
            conexion.rollback(); // En caso de error, deshacemos
            throw e;
        } finally {
            conexion.setAutoCommit(true); // Restauramos el modo auto-commit
        }
    }

}