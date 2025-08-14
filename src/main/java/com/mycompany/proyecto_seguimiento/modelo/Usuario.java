/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_seguimiento.modelo;

/**
 *
 * @author natha y mauri
 */
public class Usuario {
    private int cedula; 
    private String nombre; 
    private String apellido; 
    private String email; 
    private String nro_telefono; 
    private String password_hash; 
    private String rol;

    public Usuario(int cedula, String nombre, String apellido, String email, String nro_telefono, String password_hash, String rol) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.nro_telefono = nro_telefono;
        this.password_hash = password_hash;
        this.rol = rol;
    }

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNro_telefono() {
        return nro_telefono;
    }

    public void setNro_telefono(String nro_telefono) {
        this.nro_telefono = nro_telefono;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    //ajusté el nombre del setter para que coincida
    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
}