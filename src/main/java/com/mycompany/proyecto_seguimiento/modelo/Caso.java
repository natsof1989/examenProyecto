/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_seguimiento.modelo;

import java.time.LocalDate;

/**
 *
 * @author Mauricio Mazier
 */
public class Caso {
    private int idCaso;
    private LocalDate fecha;
    private String obsGenerales;
    private int estudianteCI;
    private String estudianteNombre;
    private String estudianteApellido;
    private String especialidad;
    private String curso;
    private String seccion;

    public Caso(int idCaso, LocalDate fecha, String obsGenerales, int estudianteCI, String estudianteNombre, String estudianteApellido, String especialidad, String curso, String seccion) {
        this.idCaso = idCaso;
        this.fecha = fecha;
        this.obsGenerales = obsGenerales;
        this.estudianteCI = estudianteCI;
        this.estudianteNombre = estudianteNombre;
        this.estudianteApellido = estudianteApellido;
        this.especialidad = especialidad;
        this.curso = curso;
        this.seccion = seccion;
    }

    // Getters
    public int getIdCaso() { return idCaso; }
    public LocalDate getFecha() { return fecha; }
    public String getObsGenerales() { return obsGenerales; }
    public int getEstudianteCI() { return estudianteCI; }
    public String getEstudianteNombre() { return estudianteNombre; }
    public String getEstudianteApellido() { return estudianteApellido; }
    public String getEspecialidad() { return especialidad; }
    public String getCurso() { return curso; }
    public String getSeccion() { return seccion; }
}