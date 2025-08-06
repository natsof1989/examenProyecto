/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.proyecto_seguimiento;
import com.mycompany.proyecto_seguimiento.clases.ControladorUtils;
import com.mycompany.proyecto_seguimiento.clases.SessionManager;
import com.mycompany.proyecto_seguimiento.clases.conexion;
import com.mycompany.proyecto_seguimiento.modelo.UsuarioDAO;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author natha
 */
public class RegistroController implements Initializable {

    @FXML
    private TextField txt_CI;
    @FXML
    private TextField txt_correo;
    @FXML
    private Hyperlink link1;
    @FXML
    private Button btn_verificacion;

    /**
     * Initializes the controller class.
     */
    private UsuarioDAO usuarioDao;
    private final conexion dbConexion = new conexion();
    private final SessionManager session = SessionManager.getInstance();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.usuarioDao = new UsuarioDAO(dbConexion.getConnection());
        // TODO
    }    

    @FXML
    private void verificacion(ActionEvent event) throws IOException {
        if (ControladorUtils.hayCamposVacios(txt_CI, txt_correo)) {
            ControladorUtils.mostrarAlerta("Error", "Todos los campos son obligatorios"); 
            return;
        }
        String correo = txt_correo.getText(); 
        String cedula = txt_CI.getText(); 
        
        try 
        {
            if (!usuarioDao.existeCiCorreo(cedula, correo)) {
                ControladorUtils.mostrarAlerta("Error", "Credenciales incorrectas");
                txt_CI.setText("");
                txt_correo.setText(""); 
                return;
            }
            session.setCiUsuario(cedula);
            session.setCorreoUsuario(correo);
            ControladorUtils.abrirVentana("registro2.fxml", "Registro", btn_verificacion);
            
            
        } catch (SQLException ex){
            ControladorUtils.mostrarError("Error durante la verificacion", ex, getClass());
        }
        
        
    }

    @FXML
    private void inicaSesion(ActionEvent event) throws IOException {
        ControladorUtils.abrirVentana("inicioSesion.fxml", "Iniciar sesión", link1); 
    }
     
    
}
