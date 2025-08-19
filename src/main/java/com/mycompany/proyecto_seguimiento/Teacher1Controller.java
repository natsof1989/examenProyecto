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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txt_ci.setText(SessionManager.getInstance().getCiUsuario());
        txt_nombre.setText(SessionManager.getInstance().getUsuarioDatos().getNombre() + " " + SessionManager.getInstance().getUsuarioDatos().getApellido());
        // TODO
    }

    @FXML
    private void casosEnviados(ActionEvent event) {
        // Implementa navegación si lo necesitas, ejemplo:
        ControladorUtils.cambiarVista("teacher2");
    }

    @FXML
    private void casoNuevo(ActionEvent event) {
        // Implementa navegación si lo necesitas, ejemplo:
        ControladorUtils.cambiarVista("teacher3");
    }

    @FXML
    private void orientaciones(ActionEvent event) {
        // Implementa navegación si lo necesitas, ejemplo:
        ControladorUtils.cambiarVista("teacher4");
    }

    @FXML
    private void configurar(ActionEvent event) {
        ControladorUtils.cambiarVista("configurarCuenta");
    }

    @FXML
    private void logout(ActionEvent event) {
        ControladorUtils.cambiarVista("inicioSesion");
    }
}