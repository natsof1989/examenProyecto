/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_seguimiento.modelo;

import org.mindrot.jbcrypt.BCrypt;
/**
 *
 * @author natha
 */
public class Seguridad {
    public static String encriptarPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verificarPassword(String password, String hashGuardado) {
        return BCrypt.checkpw(password, hashGuardado);
    }
    
}
