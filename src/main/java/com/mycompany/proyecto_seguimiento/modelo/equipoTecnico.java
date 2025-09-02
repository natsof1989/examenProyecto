/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_seguimiento.modelo;

/**
 *
 * @author natha
 */
public class equipoTecnico {
    private String ci; 
    private String nombreCompleto; 
    private String departamento; 

    public equipoTecnico() {
    }

    public equipoTecnico(String ci, String nombreCompleto, String departamento) {
        this.ci = ci;
        this.nombreCompleto = nombreCompleto;
        this.departamento = departamento; 
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    
    @Override
    public String toString() {
        return nombreCompleto + " " + departamento;
    }
    
}
