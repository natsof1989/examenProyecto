package com.mycompany.proyecto_seguimiento;

import com.mycompany.proyecto_seguimiento.clases.SessionManager;
import com.mycompany.proyecto_seguimiento.clases.UsuarioDAO;
import com.mycompany.proyecto_seguimiento.clases.conexion;
import com.mycompany.proyecto_seguimiento.modelo.UsuarioDatos;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class App extends Application {

    public static Scene scene;

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        // Ciclo de configuración y test de conexión
        while (true) {
            File configFile = new File("config1.properties");
            boolean needsConfig = !configFile.exists();

            if (needsConfig || !testConexion()) {
                FXMLLoader configLoader = new FXMLLoader(App.class.getResource("/com/mycompany/proyecto_seguimiento/ConfigDB.fxml"));
                Parent configRoot = configLoader.load();

                Stage configStage = new Stage();
                configStage.setTitle("Configurar Base de Datos");
                configStage.setScene(new Scene(configRoot));
                configStage.initModality(Modality.APPLICATION_MODAL);
                configStage.setResizable(false);
                configStage.showAndWait();

                // Si sigue sin haber archivo, salir
                if (!configFile.exists()) {
                    System.exit(0);
                }
            } else {
                break; // conexión exitosa
            }
        }

        // Lógica original de sesión y roles
        SessionManager session = SessionManager.getInstance();
        boolean haySesion = session.cargarSesionDeArchivo();

        String vistaAFXML;

        if (haySesion && session.getCiUsuario() != null) {
            // Mapear usuario automáticamente
            try {
                UsuarioDAO usuarioDao = new UsuarioDAO(new conexion().getConnection());
                UsuarioDatos datos = usuarioDao.obtenerDatosUsuario(session.getCiUsuario());
                session.setUsuarioDatos(datos);
            } catch (SQLException ex) {
                ex.printStackTrace();
                // Si hay error, se puede volver al login
                vistaAFXML = "inicioSesion";
            }

            // Determinar la vista según roles
            List<String> roles = session.getRolesUsuario();
            if (roles != null && roles.size() == 1) {
                vistaAFXML = roles.get(0).equals("PROFESOR") ? "teacher1" : "equipoTecnico";
            } else if (roles != null && roles.size() > 1) {
                vistaAFXML = "SeleccionRol";
            } else {
                vistaAFXML = "inicioSesion";
            }

        } else {
            // No hay sesión → mostrar login
            vistaAFXML = "inicioSesion";
        }

        scene = new Scene(loadFXML(vistaAFXML), 800, 600);
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setResizable(true);
        stage.show();
    }

    /**
     * Prueba la conexión a la base de datos usando la clase conexion adaptada
     */
    private boolean testConexion() {
        try (Connection conn = conexion.getConnection()) {
            System.out.println("Conectado a la BD.");
            String sql = "SHOW TABLES";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                System.out.println("Tablas en la base de datos:");
                while (rs.next()) {
                    System.out.println(" - " + rs.getString(1));
                }
            }
            return true;
        } catch (SQLException ex) {
            System.out.println("Error al conectar o al listar tablas: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Cambia la vista actual
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        Stage stage = (Stage) scene.getWindow();
        stage.setMinWidth(800);
        stage.setMinHeight(600);
    }

    /**
     * Carga un FXML desde resources
     */
    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/mycompany/proyecto_seguimiento/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}