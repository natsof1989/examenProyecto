/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.examenproyecto.clases;

import com.mycompany.examenproyecto.modelo.Alumno;
import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author natha
 */
public class claseDAO {
    private final Connection conexion;

    public claseDAO(Connection conexion) {
        this.conexion = conexion;
    }
    
     // ------------------- INSERTAR -------------------
    public boolean insertarAlumno(int id, String nombre, String apellido) {
        String sql = "INSERT INTO alumno (id_alumno, nombre, apellido) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, nombre);
            ps.setString(3, apellido);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ------------------- MODIFICAR -------------------
    public boolean modificarAlumno(int id, String nombre, String apellido) {
        String sql = "UPDATE alumno SET nombre = ?, apellido = ? WHERE id_alumno = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ------------------- ELIMINAR -------------------
    public boolean eliminarAlumno(int id) {
        String sql = "DELETE FROM alumno WHERE id_alumno = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ------------------- SELECCIONAR UNO -------------------
    public Alumno obtenerAlumno(int id) {
        String sql = "SELECT * FROM alumno WHERE id_alumno = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Alumno(rs.getInt("id_alumno"),
                                  rs.getString("nombre"),
                                  rs.getString("apellido"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ------------------- SELECCIONAR TODOS -------------------
    public List<Alumno> obtenerTodos() {
        List<Alumno> lista = new ArrayList<>();
        String sql = "SELECT * FROM alumno";
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Alumno(rs.getInt("id_alumno"),
                                     rs.getString("nombre"),
                                     rs.getString("apellido")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
