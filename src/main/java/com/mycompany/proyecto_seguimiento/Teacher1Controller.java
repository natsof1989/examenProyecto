/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.proyecto_seguimiento;
import com.mycompany.proyecto_seguimiento.clases.SessionManager;
import com.mycompany.proyecto_seguimiento.clases.ControladorUtils;
import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author natha
 */
public class Teacher1Controller implements Initializable {

    @FXML
    private Label txt_bienvenida;
    @FXML
    private Text txt_nombre;
    @FXML
    private Text txt_especialidad;
    @FXML
    private Text txt_ci;
    @FXML
    private Button bt_verCaso;
    @FXML
    private Button bt_casoNew;
    @FXML
    private Button bt_orientaciones;
    @FXML
    private Button bt_configurar;
    @FXML
    private Button bt_cerrarSesion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txt_ci.setText(SessionManager.getInstance().getCiUsuario());
        txt_nombre.setText(SessionManager.getInstance().getUsuarioDatos().getNombre() + " "+ SessionManager.getInstance().getUsuarioDatos().getApellido()); 
        
        // TODO
    }    

    @FXML
    private void casosEnviados(ActionEvent event) {
    }

    @FXML
    private void casoNuevo(ActionEvent event) {
    }

    @FXML
    private void orientaciones(ActionEvent event) {
    }

    @FXML
    private void configurar(ActionEvent event) throws IOException {
        ControladorUtils.cambiarFormulario(event, "/com/mycompany/proyecto_seguimiento/configurarCuenta.fxml", "Configuración de cuenta");
        
        
    }

    @FXML
    private void logout(ActionEvent event) {
    }
    
}
