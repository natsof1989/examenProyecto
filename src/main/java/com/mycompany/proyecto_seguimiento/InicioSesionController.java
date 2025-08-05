package com.mycompany.proyecto_seguimiento;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import com.mycompany.proyecto_seguimiento.clases.conexion;
import com.mycompany.proyecto_seguimiento.modelo.Seguridad;

public class InicioSesionController implements Initializable {

    @FXML
    private TextField txtCI;
    @FXML
    private PasswordField txtContrasenhia;
    @FXML
    private Label link;
    @FXML
    private Button btInicioSesion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Aquí puedes cargar configuraciones iniciales si las necesitas
    }

    @FXML
    private void recuperarAcceso(MouseEvent event) {
        // Aquí podrías abrir una nueva ventana para recuperar acceso
        mostrarAlerta("Info", "Funcionalidad en desarrollo.");
    }

    @FXML
    private void iniciarSesion(ActionEvent event) {
        String ciTexto = txtCI.getText().trim();
        String contrasenia = txtContrasenhia.getText();

        if (ciTexto.isEmpty() || contrasenia.isEmpty()) {
            mostrarAlerta("Error", "Por favor, completa todos los campos.");
            return;
        }

        try {
            int ci = Integer.parseInt(ciTexto);

            conexion miConexion = new conexion();
            Connection con = miConexion.getConnection();

            String sql = "SELECT password FROM profesor WHERE CI = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, ci);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashGuardado = rs.getString("password");
                if (Seguridad.verificarPassword(contrasenia, hashGuardado)) {
                    mostrarAlerta("Éxito", "Inicio de sesión exitoso.");
                    // Aquí puedes redirigir al usuario al menú principal o dashboard
                } else {
                    mostrarAlerta("Error", "Contraseña incorrecta.");
                }
            } else {
                mostrarAlerta("Error", "No se encontró un profesor con ese CI.");
            }

            con.close();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El CI debe ser numérico.");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al iniciar sesión: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
