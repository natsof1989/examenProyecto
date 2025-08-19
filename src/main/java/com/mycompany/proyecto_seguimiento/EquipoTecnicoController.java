/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.proyecto_seguimiento;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author natha
 */
public class EquipoTecnicoController implements Initializable {

    @FXML
    private Button btn_admiCaso;
    @FXML
    private Button btn_atenderC;
    @FXML
    private Button btn_orientacion;
    @FXML
    private Button btn_reporte;
    @FXML
    private Button btn_configCuenta;
    @FXML
    private Button btn_logout;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void administrarCaso(ActionEvent event) {
    }

    @FXML
    private void atenderCaso(ActionEvent event) {
    }

    @FXML
    private void verOrientaciones(ActionEvent event) {
    }

    @FXML
    private void generaReporte(ActionEvent event) {
    }

    @FXML
    private void configurarCuenta(ActionEvent event) {
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
    }
    
}
