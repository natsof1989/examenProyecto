/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.proyecto_seguimiento;

import com.mycompany.proyecto_seguimiento.clases.SessionManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author natha
 */
public class RecuperarAccesoController implements Initializable {

    @FXML
    private TextField txt_ci;
    @FXML
    private TextField txt_correo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txt_ci.setText(SessionManager.getInstance().getCiUsuario()); 
        txt_correo.setText(SessionManager.getInstance().getUsuarioDatos().getEmail()); 
        // TODO
    }    
    
}
