/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.proyecto_seguimiento;

import com.mycompany.proyecto_seguimiento.clases.ControladorUtils;
import com.mycompany.proyecto_seguimiento.clases.EmailUtils;
import com.mycompany.proyecto_seguimiento.clases.SessionManager;
import com.mycompany.proyecto_seguimiento.clases.conexion;
import com.mycompany.proyecto_seguimiento.clases.UsuarioDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author natha y mauri
 */

public class RecuperarAccesoController implements Initializable {

    @FXML
    private TextField txt_ci;
    @FXML
    private TextField txt_correo;

    /**
     * Initializes the controller class.
     */
    private final conexion dbConexion = new conexion();
    private UsuarioDAO usuarioDao;
    private final SessionManager session = SessionManager.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.usuarioDao = new UsuarioDAO(dbConexion.getConnection());
        // TODO
    }    

    @FXML
    private void enviarCodigo(ActionEvent event) throws IOException  {
        if (ControladorUtils.hayCamposVacios(txt_ci, txt_correo)) {
            ControladorUtils.mostrarAlerta("Error", "Todos los campos son obligatorios");
            return;
        }
        String cedula = txt_ci.getText().trim(); 
        String correo = txt_correo.getText(); 
         if(!ControladorUtils.validarNumero(cedula, "CI")){
            ControladorUtils.mostrarAlerta("Error", "Introducción del número de cédula erronea"); 
            return; 
        }
         
        //Aca se debe validar la entrada del correo
        //validación de correo del mauri
        
        if (!ControladorUtils.validarCorreo(correo)) {
            return; // Detenemos el registro
        }
        //Esta seria la validacion con consultas sql 
         try {
            // 1. Verificar si está en lista blanca
                if (!usuarioDao.existeCiCorreo(cedula, correo)) {
                    ControladorUtils.mostrarAlerta("Error", "Credenciales incorrectas o no está en lista blanca");
                    txt_ci.setText("");
                    txt_correo.setText(""); 
                    return;
                }

                // 2. Verificar si existe registro COMPLETO
                if (usuarioDao.existeRegistroCompleto(cedula)) {
                    //Aca se debe programar el envio del codigo de verificacion
                    ControladorUtils.cambiarVista( "recuperarAcceso1");
                    
                    // Generar código aleatorio de 6 dígitos
                    String codigoVerificacion = String.format("%06d", (int)(Math.random() * 1000000));

                    // Guardarlo en la sesión para verificar después
                    session.setCodigoVerificacion(codigoVerificacion);

                    // Enviar el correo
                    try {
                        EmailUtils.enviarCodigo(correo, codigoVerificacion);
                        ControladorUtils.mostrarAlertaChill("Código enviado", "Revisa tu correo para continuar");
                    } catch (Exception e) {
                        ControladorUtils.mostrarError("Error de envío", "No se pudo enviar el código", e);
                        return;
                    }

                    return;
                } else {
                    ControladorUtils.mostrarAlertaChill("Aviso", "Usted aun no se ha registrado. Cree su cuenta para registrarse");
                    boolean confirmar = ControladorUtils.mostrarConfirmacion("Registrarse", "¿Desea registrarse?");
                    if(confirmar){
                        //se guardan los datos en la clase singlenton
                        session.setCiUsuario(cedula);
                        session.setCorreoUsuario(correo); 
                        ControladorUtils.cambiarVista("registro2");
                    }
                }

            } catch (SQLException ex) {
                ControladorUtils.mostrarError("Error de verificación", "Ocurrió un error al verificar los datos", ex);

            }
    }

    
}
