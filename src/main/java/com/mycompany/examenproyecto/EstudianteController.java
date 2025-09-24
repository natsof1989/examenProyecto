/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.examenproyecto;

import com.mycompany.examenproyecto.clases.ControladorUtils;
import com.mycompany.examenproyecto.clases.conexion;
import com.mycompany.examenproyecto.modelo.Alumno;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author natha
 */
public class EstudianteController implements Initializable {

    @FXML
    private TextField txt_dni;
    @FXML
    private TextField txt_edad;
    @FXML
    private Button btn_borrar;
    @FXML
    private Button btn_enviar;
    @FXML
    private ComboBox<String> cmb_opciones;
    
    private Alumno dao;  

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        conexion conBD = new conexion();
        dao = new Alumno(conBD.getConnection());
        cargarOpciones();

       
        //
    }    

    
    
    private void cargarOpciones(){
        List<String> opciones = new ArrayList<>(); 
       opciones.add("Eliminar"); 
       opciones.add("insertar"); 
       cmb_opciones.getItems().addAll(opciones);
    }

    @FXML
    private void ejecutar(ActionEvent event) {
        if(ControladorUtils.hayCamposVacios(txt_dni, txt_edad) || cmb_opciones.getSelectionModel().getSelectedItem()==null){
            ControladorUtils.mostrarAlerta("Informamos", "No puede dejar campos vacios ni dejar la operacion sin seleccionar");
            return; 
        }
        
        if(ControladorUtils.validarNumero(txt_dni.getText(), "CI")==false && ControladorUtils.validarNumero(txt_edad.getText(), "EDAD")==false){
           ControladorUtils.mostrarAlerta("Informamos", "Error en la introducción de datos");
           return; 
        
            
        }
        int edad = Integer.parseInt(txt_edad.getText()); 
        String dni = txt_dni.getText(); 
        
    
            
            String seleccion = cmb_opciones.getSelectionModel().getSelectedItem(); 
            if("Eliminar".equals(seleccion)){
                if(dao.existeAlumno(dni, edad)){
                        int age = dao.verificarEdad(dni, edad); 
                        if(age!=edad){
                            ControladorUtils.mostrarAlerta("Informamos", "Edad del alumno introducida incorrecta. ");
                            return; 
                        }
                        if(!dao.existeCI(dni)){
                            ControladorUtils.mostrarAlerta("informamos", "ese numero de cedula no existe en la base de datos");
                            return; 
                        }
                        if(dao.eliminarAlumno(dni)){
                           ControladorUtils.mostrarAlertaChill("Informamos", "Eliminación exitosa");
                        
                        } else{
                           ControladorUtils.mostrarAlerta("Informamos", "Ocurrión un error al intentar eliminar");
                        }
                } else{
                    ControladorUtils.mostrarAlerta("Informamos", "El alumno no existe en la base de datos");
                }
                

            } else if("insertar".equals(seleccion)){
                if(!dao.existeCI(dni)){
                    if(dao.insertarAlumno(dni, edad)){
                       ControladorUtils.mostrarAlertaChill("Informamos", "Se insertó con éxito");

                   } else{
                       ControladorUtils.mostrarAlerta("Informamos", "Ocurrión un error al intentar insertar");
                   } 
                } else{
                    ControladorUtils.mostrarAlerta("informamos", "Ese alumno ya existe");
                }
                

            }
            
        
        
        
    }

    @FXML
    private void limpiarCamṕos(ActionEvent event) {
         txt_dni.clear();
        txt_edad.clear();
        cmb_opciones.getSelectionModel().clearSelection();
        
    }
    
}
