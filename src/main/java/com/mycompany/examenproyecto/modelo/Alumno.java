/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.examenproyecto.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author natha
 */
public class Alumno {
    private String ci; 
    private int edad; 
    private Connection conexion;
    public Alumno(Connection conexion) {
        this.conexion = conexion;
    }

    public Alumno(String ci, int edad) {
        this.ci = ci;
        this.edad = edad;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
    
    public boolean eliminarAlumno(String id) {
        String sql = "DELETE FROM alumno WHERE dni = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean insertarAlumno(String dni, int edad) {
        String sql = "INSERT INTO alumno (dni, edad) VALUES (?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, dni);
            ps.setInt(2, edad);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean existeAlumno(String dni, int edad) {
        String sql = "SELECT * FROM alumno WHERE dni =? and edad=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, dni);
            ps.setInt(2, edad); 
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
     public int verificarEdad(String dni, int edad) {
        String sql = "SELECT edad FROM alumno WHERE dni =?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, dni);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int age = rs.getInt("edad"); 
                return age; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; 
    }
     public boolean existeCI(String dni) {
        String sql = "SELECT dni FROM alumno WHERE dni =?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, dni);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }
    
    
    
    
}
