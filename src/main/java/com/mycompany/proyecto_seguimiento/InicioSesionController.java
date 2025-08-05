package com.mycompany.proyecto_seguimiento;

import com.mycompany.proyecto_seguimiento.clases.conexion;
import com.mycompany.proyecto_seguimiento.modelo.Seguridad;
import com.mycompany.proyecto_seguimiento.clases.SessionManager;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Usar la instancia dbConexion para obtener la conexión
        this.usuarioDao = new UsuarioDAO(dbConexion.getConnection());
    }

    @FXML
    private void iniciarSesion(ActionEvent event) {
        String ci = txtCI.getText().trim();
        String contrasenia = txtContrasenhia.getText().trim();

        try {
            if (!usuarioDao.validarCredenciales(ci, contrasenia)) {
                mostrarAlerta("Error", "Credenciales incorrectas");
                return;
            }

            SessionManager session = SessionManager.getInstance();
            session.setCiUsuario(ci);
            session.setRolesUsuario(usuarioDao.obtenerRoles(ci));

            List<String> roles = session.getRolesUsuario();

            if (roles.size() == 1) {
                session.setRolSeleccionado(roles.get(0));
                abrirVentana(
                    roles.get(0).equals("PROFESOR") 
                        ? "teacher1.fxml" 
                        : "EquipoTecnicoDashboard.fxml",
                    "Panel Principal"
                );
            } else if (roles.size() > 1) {
                abrirVentana("SeleccionRol.fxml", "Seleccione su rol");
            } else {
                mostrarAlerta("Error", "Usuario sin roles asignados");
            }

        } catch (SQLException | IOException ex) {
            mostrarError("Error durante el login", ex);
        }
    }

    private boolean validarCampos(String ci, String contrasenia) {
        if (ci.isEmpty() || contrasenia.isEmpty()) {
            mostrarAlerta("Error", "CI y contraseña son obligatorios");
            return false;
        }
        return true;
    }

    private void configurarSesion(String ci) throws SQLException {
        session.setCiUsuario(ci);
        session.setRolesUsuario(usuarioDao.obtenerRoles(ci));
        
        if (!usuarioDao.registroCompleto(ci)) {
            session.setRolSeleccionado("REGISTRO_PENDIENTE");
        }
    }

    private void redirigirSegunRoles() throws IOException {
        List<String> roles = session.getRolesUsuario();
        
        if (session.getRolSeleccionado().equals("REGISTRO_PENDIENTE")) {
            abrirVentana("registro.fxml", "Completar Registro");
            return;
        }

        switch (roles.size()) {
            case 1:
                session.setRolSeleccionado(roles.get(0));
                abrirVentana(
                    roles.get(0).equals("PROFESOR") ? 
                        "ProfesorDashboard.fxml" : "EquipoTecnicoDashboard.fxml", 
                    "Panel Principal"
                );
                break;
            case 2:
                abrirVentana("SeleccionRol.fxml", "Seleccione su rol");
                break;
            default:
                mostrarAlerta("Error", "Usuario sin roles asignados");
        }
    }

    private void abrirVentana(String fxml, String titulo) throws IOException {
        Stage stage = (Stage) btInicioSesion.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        stage.setScene(new Scene(root));
        stage.setTitle(titulo);
        stage.centerOnScreen();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        new Alert(Alert.AlertType.ERROR, mensaje).showAndWait();
    }

    private void mostrarError(String mensaje, Exception ex) {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, mensaje, ex);
        mostrarAlerta("Error", mensaje);
    }

    @FXML
    private void registrarse(ActionEvent event) throws IOException {
        abrirVentana("registro.fxml", "Registro de Usuario");
    }

    @FXML
    private void recuperarAcceso(ActionEvent event) throws IOException {
        abrirVentana("RecuperarAcceso.fxml", "Recuperar Acceso");
    }
}