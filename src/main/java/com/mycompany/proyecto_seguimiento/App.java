package com.mycompany.proyecto_seguimiento;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Scene scene;

   @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("inicioSesion"), 800, 600); // tamaño inicial
        stage.setScene(scene);
        stage.setMinWidth(800);  // tamaño mínimo ancho
        stage.setMinHeight(800); // tamaño mínimo alto
        stage.setResizable(true); // permitir redimensionar
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        // Opcional: si querés forzar que al cambiar de vista la ventana siga con tamaño mínimo
        Stage stage = (Stage) scene.getWindow();
        stage.setMinWidth(800);
        stage.setMinHeight(600);
    }


    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}