/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.proyecto_seguimiento;

import com.mycompany.proyecto_seguimiento.clases.ControladorUtils;
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
    private Button btn_orientacion;
    @FXML
    private Button btn_configCuenta;
    @FXML
    private Button btn_logout;
    @FXML
    private Button btn_asignar;
    @FXML
    private Button btn_CASOS;
    @FXML
    private Button btn_asignados;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    


    @FXML
    private void configurarCuenta(ActionEvent event) {
        ControladorUtils.cambiarVista("configurarCuenta");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        ControladorUtils.cambiarVista("inicioSesion");
    }

    @FXML
    private void asignarCaso(ActionEvent event) {
        ControladorUtils.cambiarVista("equipoTecnico1");
    }

    @FXML
    private void todo_caso(ActionEvent event) {
        ControladorUtils.cambiarVista("equipoTecnico2");
    }

    @FXML
    private void casosAsignados(ActionEvent event) {
        ControladorUtils.cambiarVista("equipoTecnico3");
    }

    @FXML
    private void orientaciones(ActionEvent event) {
        ControladorUtils.cambiarVista("equipoTecnico4");
    }
    
}
