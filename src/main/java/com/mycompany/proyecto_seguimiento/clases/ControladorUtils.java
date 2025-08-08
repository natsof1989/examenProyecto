package com.mycompany.proyecto_seguimiento.clases;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

public class ControladorUtils {

    public static void abrirVentana(String fxml, String titulo, Node nodoCualquiera) throws IOException {
        FXMLLoader loader = new FXMLLoader(ControladorUtils.class.getResource("/com/mycompany/proyecto_seguimiento/" + fxml));
        Parent root = loader.load();

        Stage stage = (Stage) nodoCualquiera.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(titulo);
        stage.centerOnScreen();
    }

    public static void mostrarAlerta(String titulo, String mensaje) {
        new Alert(Alert.AlertType.ERROR, mensaje).showAndWait();
    }
    public static void mostrarAlertaChill(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText("Aviso");  // Esto elimina el "Message" por defecto
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void mostrarError(String titulo, String mensaje, Exception ex) {
        // Log del error
        System.err.println("ERROR [" + titulo + "]: " + mensaje);
        if (ex != null) {
            ex.printStackTrace();
        }

        // Mostrar alerta
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
}
    // En tu clase ControladorUtils
    public static boolean mostrarConfirmacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        // Personalizar botones (opcional)
        ButtonType buttonTypeYes = new ButtonType("Sí", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Mostrar diálogo y esperar respuesta
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonTypeYes;
    }

    // Retorna true si alguno de los campos está vacío (trim para ignorar espacios)
    public static boolean hayCamposVacios(TextInputControl... campos) {
        for (TextInputControl campo : campos) {
            if (campo.getText() == null || campo.getText().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean validarNumero(String texto, String tipo) {
        
        // Eliminar espacios y guiones si los hubiera
        String textoLimpio = texto.replaceAll("[\\s-]", "");
        
        try {
            // Verificar que sean solo dígitos
            Long.parseLong(textoLimpio);
        } catch (NumberFormatException e) {
            return false;
        }
        
        // Validar según el tipo
        switch(tipo.toUpperCase()) {
            case "CI":
                // Validar cédula uruguaya (8 dígitos)
                return textoLimpio.length() == 8;
                
            case "TEL":
                // Validar teléfono uruguayo (8 o 9 dígitos)
                return textoLimpio.length() == 8 || textoLimpio.length() == 9;
                
            default:
                return false;
        }
    }
    
     public static boolean contieneSoloLetras(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        // Expresión regular que permite:
        // - Letras mayúsculas y minúsculas (a-z, A-Z)
        // - Vocales con acentos (áéíóúÁÉÍÓÚ)
        // - Letra ñ/Ñ
        // - Espacios en blanco
        return texto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
    }
     
    public static boolean validarCamposLetras(TextInputControl... campos) {
        for (TextInputControl campo : campos) {
            if (campo.getText() == null || !contieneSoloLetras(campo.getText())) {
                return false;
            }
        }
        return true;
    }
    

}


