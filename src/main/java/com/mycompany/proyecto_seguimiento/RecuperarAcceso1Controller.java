package com.mycompany.proyecto_seguimiento;

import com.mycompany.proyecto_seguimiento.clases.ControladorUtils;
import com.mycompany.proyecto_seguimiento.clases.SessionManager;
import com.mycompany.proyecto_seguimiento.clases.conexion;
import com.mycompany.proyecto_seguimiento.clases.UsuarioDAO;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class RecuperarAcceso1Controller implements Initializable {

    @FXML private TextField digit1;
    @FXML private TextField digit2;
    @FXML private TextField digit3;
    @FXML private TextField digit4;
    @FXML private TextField digit5;
    @FXML private TextField digit6;
    @FXML private Button bt_verificar;
    @FXML private TextField txt_newContra;
    @FXML private TextField txt_condirmarContra;
    @FXML private Button bt_confirmarCotra;

    private conexion dbConexion;
    private UsuarioDAO usuarioDao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // inicializar DB/DAO
        try {
            dbConexion = new conexion();
            Connection conn = dbConexion.getConnection();
            usuarioDao = new UsuarioDAO(conn);
        } catch (Exception e) {
            // si falla la conexión, mostrar error (y deshabilitar botones por seguridad)
            ControladorUtils.mostrarError("Error", "No se pudo conectar a la base de datos", e);
            bt_verificar.setDisable(true);
            bt_confirmarCotra.setDisable(true);
        }

        configurarCamposCodigo();

        // inicialmente deshabilitamos el formulario de cambio de contraseña hasta verificar código
        txt_newContra.setDisable(true);
        txt_condirmarContra.setDisable(true);
        bt_confirmarCotra.setDisable(true);

        // asignar handlers programáticamente
        bt_verificar.setOnAction(this::verificarCodigo);
        bt_confirmarCotra.setOnAction(this::confirmarNuevaContrasenia);
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

    private void verificarCodigo(ActionEvent event) {
        String codigoIngresado = (digit1.getText() + digit2.getText() + digit3.getText()
                                + digit4.getText() + digit5.getText() + digit6.getText()).trim();

        String codigoSesion = SessionManager.getInstance().getCodigoVerificacion();
        if (codigoSesion == null) {
            ControladorUtils.mostrarAlerta("Error", "No hay un código en sesión. Volvé al paso anterior.");
            return;
        }

        if (codigoIngresado.equals(codigoSesion)) {
            ControladorUtils.mostrarAlertaChill("Correcto", "Código verificado");
            // habilitar campos de nueva contraseña
            txt_newContra.setDisable(false);
            txt_condirmarContra.setDisable(false);
            bt_confirmarCotra.setDisable(false);

            // opcional: deshabilitar edición de código para evitar cambios
            digit1.setDisable(true); digit2.setDisable(true); digit3.setDisable(true);
            digit4.setDisable(true); digit5.setDisable(true); digit6.setDisable(true);
            bt_verificar.setDisable(true);
        } else {
            ControladorUtils.mostrarAlerta("Error", "Código incorrecto");
        }
    }

    private void confirmarNuevaContrasenia(ActionEvent event) {
        String nueva = txt_newContra.getText();
        String confirmar = txt_condirmarContra.getText();

        if (nueva == null || confirmar == null || nueva.isEmpty() || confirmar.isEmpty()) {
            ControladorUtils.mostrarAlerta("Error", "Ambos campos de contraseña son obligatorios");
            return;
        }

        if (!nueva.equals(confirmar)) {
            ControladorUtils.mostrarAlerta("Error", "Las contraseñas no coinciden");
            return;
        }

        // validación simple de política: al menos 8 caracteres (ajustar según necesites)
        if (nueva.length() < 8) {
            ControladorUtils.mostrarAlerta("Error", "La contraseña debe tener al menos 8 caracteres");
            return;
        }

        String ciProfe = SessionManager.getInstance().getCiUsuario();
        if (ciProfe == null || ciProfe.isEmpty()) {
            ControladorUtils.mostrarAlerta("Error", "No se encontró la cédula en sesión. Volvé al paso anterior.");
            return;
        }

        try {
            boolean ok = usuarioDao.actualizarPassword(ciProfe, nueva);
            if (ok) {
                ControladorUtils.mostrarAlertaChill("Éxito", "Contraseña actualizada correctamente");
                // limpiar datos sensibles
                SessionManager.getInstance().setCiUsuario(null);
                SessionManager.getInstance().setCodigoVerificacion(null);
                // redirigir a login (ajustá el nombre de vista si en tu proyecto se llama distinto)
                ControladorUtils.cambiarVista("inicioSesion");
            } else {
                ControladorUtils.mostrarAlerta("Error", "No se encontró un usuario con esa cédula.");
            }
        } catch (SQLException ex) {
            ControladorUtils.mostrarError("Error al actualizar", "Ocurrió un error al actualizar la contraseña", ex);
        }
    }
}
