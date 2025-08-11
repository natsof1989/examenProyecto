package com.mycompany.proyecto_seguimiento;

import com.mycompany.proyecto_seguimiento.clases.ControladorUtils;
import com.mycompany.proyecto_seguimiento.clases.SessionManager;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class SeleccionRolController {

    @FXML
    private VBox contenedorRoles;
    
    private final SessionManager session = SessionManager.getInstance();
    
    public void initialize() throws IOException {
        cargarRolesUsuario();
    }
    
    private void cargarRolesUsuario() throws IOException {
        List<String> roles = session.getRolesUsuario();
        
        if (roles == null || roles.isEmpty()) {
            ControladorUtils.mostrarAlerta("Error", "No se encontraron roles asignados");
            try {
                ControladorUtils.cambiarFormularioDesdeNodo(contenedorRoles, "/com/empresa/proyecto/inicioSesion.fxml");
            } catch (IOException ex) {
                ControladorUtils.mostrarError("Error", "No se pudo cargar la vista", ex);
            }
            
            return;
        }
        
        generarBotonesRoles(roles);
    }
    
    private void generarBotonesRoles(List<String> roles) {
        contenedorRoles.getChildren().clear();
        
        for (String rol : roles) {
            Button btnRol = crearBotonRol(rol);
            contenedorRoles.getChildren().add(btnRol);
        }
    }
    
    private Button crearBotonRol(String rol) {
        Button btn = new Button(obtenerTextoBoton(rol));
        
        // Configuración del botón
        btn.setUserData(rol);
        btn.getStyleClass().addAll("boton-rol", "boton-grande");
        btn.setMaxWidth(Double.MAX_VALUE); // Para que se expanda al ancho del VBox
        
        // Acción del botón
        btn.setOnAction(event -> {
            session.setRolSeleccionado(rol);
            try {
                redirigirSegunRol(rol);
            } catch (IOException ex) {
                Logger.getLogger(SeleccionRolController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        return btn;
    }
    
    private String obtenerTextoBoton(String rol) {
        switch(rol) {
            case "PROFESOR":
                return "Acceder como Profesor";
            case "EQUIPO_TECNICO":
                return "Acceder como Equipo Técnico";
            case "ADMINISTRADOR":
                return "Acceder como Administrador";
            default:
                return "Acceder como " + rol;
        }
    }
    
    private void redirigirSegunRol(String rol) throws IOException {
        try {
            String archivoFxml = obtenerArchivoFxml(rol);
            try {
                ControladorUtils.cambiarFormularioDesdeNodo(contenedorRoles, archivoFxml);
            } catch (IOException ex) {
                ControladorUtils.mostrarError("Error", "No se pudo cargar la vista", ex);
            }
        } catch (Exception e) {
            ControladorUtils.mostrarAlerta("Error", "Ocurrión un error al redirigir");
            try {
                ControladorUtils.cambiarFormularioDesdeNodo(contenedorRoles, "/com/empresa/proyecto/inicioSesion.fxml");
            } catch (IOException ex) {
                ControladorUtils.mostrarError("Error", "No se pudo cargar la vista", ex);
            }
           
        }
    }
    
    private String obtenerArchivoFxml(String rol) {
        switch(rol) {
            case "PROFESOR":
                return "/com/mycompany/proyecto_seguimiento/teacher1.fxml";
            case "EQUIPO_TECNICO":
                return "/com/mycompany/proyecto_seguimiento/equipo_tecnico.fxml";
            case "ADMINISTRADOR":
                return "/com/mycompany/proyecto_seguimiento/admi.fxml";
            default:
                return "/com/mycompany/proyecto_seguimiento/inicioSesion.fxml"; // Redirigir a login si el rol no es reconocido
        }
    }
}