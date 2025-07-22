/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.proyecto_seguimiento;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author natha
 */
public class RecuperarAcceso1Controller implements Initializable {

    @FXML
    private TextField digit1;
    @FXML
    private TextField digit2;
    @FXML
    private TextField digit3;
    @FXML
    private TextField digit5;
    @FXML
    private TextField digit4;
    @FXML
    private TextField digit6;
    @FXML
    private Button bt_verificar;
    @FXML
    private TextField txt_newContra;
    @FXML
    private TextField txt_condirmarContra;
    @FXML
    private Button bt_confirmarCotra;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    private void configurarCamposCodigo() {
    TextField[] campos = {digit1, digit2, digit3, digit4, digit5, digit6};

    for (int i = 0; i < campos.length; i++) {
        final int index = i;
        campos[i].textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() > 1)
                campos[index].setText(newText.substring(0, 1));
            if (!newText.isEmpty() && index < campos.length - 1)
                campos[index + 1].requestFocus();
        });
    }
}

    
}
