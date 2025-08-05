package com.mycompany.proyecto_seguimiento;

import com.mycompany.proyecto_seguimiento.clases.SessionManager;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SeleccionRolController {
    @FXML
    private VBox contenedorRoles;
    
    private final SessionManager session = SessionManager.getInstance();
    
    @FXML
    public void initialize() {
        List<String> roles = session.getRolesUsuario();
        generarBotonesRoles(roles);
    }
    
    private void generarBotonesRoles(List<String> roles) {
        contenedorRoles.getChildren().clear();
        
        for (String rol : roles) {
            Button btnRol = new Button(obtenerTextoBoton(rol));
            btnRol.setUserData(rol);
            btnRol.getStyleClass().add("boton-rol");
            btnRol.setPrefWidth(200);
            
            btnRol.setOnAction(event -> {
                session.setRolSeleccionado(rol);
                redirigirSegunRol(rol);
            });
            
            contenedorRoles.getChildren().add(btnRol);
        }
    }
    
    private String obtenerTextoBoton(String rol) {
        switch(rol) {
            case "PROFESOR":
                return "Acceder como Profesor";
            case "EQUIPO_TECNICO":
                return "Acceder como Equipo Técnico";
            default:
                return rol;
        }
    }
    
    private void redirigirSegunRol(String rol) {
        try {
            String fxml = rol.equals("PROFESOR") 
                ? "ProfesorDashboard.fxml" 
                : "EquipoTecnicoDashboard.fxml";
            
            Stage stage = (Stage) contenedorRoles.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}