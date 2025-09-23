/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.examenproyecto.clases;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author natha
 */
public class ET_singleton {
    private static ET_singleton instancia;
    
    
    
    private ET_singleton(){}; 
    
    public static ET_singleton getInstancia() {
        if (instancia == null) {
            instancia = new ET_singleton();
        }
        return instancia;
    }
    
    
    //aca mis getters y mis setters 

   
    
    /*public void reset() {
    if (equipos != null) {
        equipos.clear(); // limpia la lista
    }*/
}

    
    

