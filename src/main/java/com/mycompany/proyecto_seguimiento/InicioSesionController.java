package com.mycompany.proyecto_seguimiento;

import com.mycompany.proyecto_seguimiento.clases.ControladorUtils;

import com.mycompany.proyecto_seguimiento.clases.conexion;
import com.mycompany.proyecto_seguimiento.modelo.Seguridad;
import com.mycompany.proyecto_seguimiento.clases.SessionManager;
import com.mycompany.proyecto_seguimiento.clases.UsuarioDatos;
import com.mycompany.proyecto_seguimiento.modelo.UsuarioDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InicioSesionController implements Initializable {

    @FXML private TextField txtCI;
    @FXML private PasswordField txtContrasenhia;
    @FXML private Button btInicioSesion;
    
    private UsuarioDAO usuarioDao;
    private final SessionManager session = SessionManager.getInstance();
    private final conexion dbConexion = new conexion(); // Instancia de tu clase conexion
    UsuarioDatos datos; 
    @FXML
    private Hyperlink link1;
    @FXML
    private Hyperlink link2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Usar la instancia dbConexion para obtener la conexión
        this.usuarioDao = new UsuarioDAO(dbConexion.getConnection());
    }

    @FXML
    
    private void iniciarSesion(ActionEvent event) {
        if (ControladorUtils.hayCamposVacios(txtCI, txtContrasenhia)) {
            ControladorUtils.mostrarAlerta("Error", "Todos los campos son obligatorios");
            return;
        }
        
        
        String ci = txtCI.getText().trim();
        String contrasenia = txtContrasenhia.getText();

        try {
            if(!usuarioDao.existeRegistroCompleto(ci)){
                ControladorUtils.mostrarAlertaChill("Aviso", "Usted aún no se ha registrado. \nCree su cuenta antes de inciar sesión.");
                txtCI.clear();
                txtContrasenhia.clear();
                return;
            }
            if (!usuarioDao.validarCredenciales(ci, contrasenia)) {
                ControladorUtils.mostrarAlerta("Error", "Credenciales incorrectas");
                return;
            }
            

            session.setCiUsuario(ci);
            session.setRolesUsuario(usuarioDao.obtenerRoles(ci));
            datos = usuarioDao.obtenerDatosUsuario(ci); 
            session.setUsuarioDatos(datos);
           

            List<String> roles = session.getRolesUsuario();

            if (roles.size() == 1) {
                session.setRolSeleccionado(roles.get(0));
                ControladorUtils.abrirVentana(
                    roles.get(0).equals("PROFESOR") 
                        ? "teacher.fxml" 
                        : "EquipoTecnicoDashboard.fxml",
                    "Panel Principal",
                    btInicioSesion
                );
            } else if (roles.size() > 1) {
                ControladorUtils.abrirVentana("SeleccionRol.fxml", "Seleccione su rol", btInicioSesion);
            } else {
                ControladorUtils.mostrarAlerta("Error", "Usuario sin roles asignados");
            }

        } catch (SQLException | IOException ex) {
            ControladorUtils.mostrarError("Error", "Ocurrió un error en el login", ex);

        }
    }

    @FXML
    private void registrarse(ActionEvent event) throws IOException {
        ControladorUtils.abrirVentana("registro.fxml", "Registrese", link1); 
    }

    @FXML
    private void recuperarAcceso(ActionEvent event) throws IOException {
        ControladorUtils.abrirVentana("recuperarAcceso.fxml", "Recuperar Acceso", link2);
    }
}
