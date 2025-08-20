/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_seguimiento.modelo;


import java.time.LocalDateTime;
/**
 *
 * @author natha
 */

import java.time.LocalDateTime;

public class Caso {
    private int idCaso;
    private LocalDateTime fecha;
    private String obsGenerales;
    private boolean activo;
    private int profesorCI;
    private int estudianteCI;
    private byte[] archivo; // Para almacenar el MEDIUMBLOB

    // Constructor vacío
    public Caso() {
    }

    // Constructor completo
    public Caso(int idCaso, LocalDateTime fecha, String obsGenerales, boolean activo, int profesorCI, int estudianteCI, byte[] archivo) {
        this.idCaso = idCaso;
        this.fecha = fecha;
        this.obsGenerales = obsGenerales;
        this.activo = activo;
        this.profesorCI = profesorCI;
        this.estudianteCI = estudianteCI;
        this.archivo = archivo;
    }

    // Getters y Setters
    public int getIdCaso() {
        return idCaso;
    }

    public void setIdCaso(int idCaso) {
        this.idCaso = idCaso;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getObsGenerales() {
        return obsGenerales;
    }

    public void setObsGenerales(String obsGenerales) {
        this.obsGenerales = obsGenerales;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getProfesorCI() {
        return profesorCI;
    }

    public void setProfesorCI(int profesorCI) {
        this.profesorCI = profesorCI;
    }

    public int getEstudianteCI() {
        return estudianteCI;
    }

    public void setEstudianteCI(int estudianteCI) {
        this.estudianteCI = estudianteCI;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

   
}

