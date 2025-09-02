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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author natha
 */
public class EquipoTecnico2Controller implements Initializable {

    @FXML
    private Button btn_volver;
    @FXML
    private TextField txt_buscar;
    @FXML
    private Button btn_abrirCaso;
    @FXML
    private TableView<?> tabla_casos;
    @FXML
    private TableColumn<?, ?> col_idCaso;
    @FXML
    private TableColumn<?, ?> col_fecha;
    @FXML
    private TableColumn<?, ?> col_estudiante;
    @FXML
    private TableColumn<?, ?> col_espe;
    @FXML
    private TableColumn<?, ?> col_curso;
    @FXML
    private TableColumn<?, ?> col_estado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void volver(ActionEvent event) {
    }

    @FXML
    private void buscar(KeyEvent event) {
    }

    @FXML
    private void abrirCaso(ActionEvent event) {
    }
    
}
