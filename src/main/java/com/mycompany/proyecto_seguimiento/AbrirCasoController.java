/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.proyecto_seguimiento;

import com.mycompany.proyecto_seguimiento.clases.CasoSeleccionado;
import com.mycompany.proyecto_seguimiento.clases.ControladorUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author natha
 */
public class AbrirCasoController implements Initializable {

    @FXML
    private Text txt_idCaso;
    @FXML
    private Text txt_estudiante;
    @FXML
    private Text txt_espe;
    @FXML
    private Text txt_curso;
    @FXML
    private Text txt_fecha;
    @FXML
    private TextArea txt_descripcion;
    @FXML
    private Text txt_profe;
    @FXML
    private Text txt_CI;
    @FXML
    private Button btn_volver;
    @FXML
    private Button btn_descargar;

    /**
     * Initializes the controller class.
     */
    CasoSeleccionado datosCaso = CasoSeleccionado.getInstancia(); 
    LocalDateTime fecha = datosCaso.getFecha();
    @FXML
    private GridPane equipoT_content;
    @FXML
    private Hyperlink link_historial;
    @FXML
    private Label lb_1;
    @FXML
    private ScrollPane scroll_equipo;
    @FXML
    private Button btn_asignar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txt_idCaso.setText(String.valueOf(datosCaso.getIdCaso()));
        txt_estudiante.setText(datosCaso.getEstudiante());
        txt_espe.setText(datosCaso.getEspecialidad());
        txt_curso.setText(datosCaso.getCurso());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        txt_fecha.setText(fecha.format(formatter));
        txt_descripcion.setText(datosCaso.getDescripcion());
        txt_profe.setText(datosCaso.getNombreProfesor());
        txt_CI.setText(datosCaso.getCiProfesor());
        
        
         if (datosCaso.getArchivo() != null) {
            btn_descargar.setVisible(true);
            btn_descargar.setDisable(false);
        } else {
            btn_descargar.setVisible(false);
            btn_descargar.setDisable(true);
        }
        
        Tooltip tooltip = new Tooltip("Descargar archivo adjunto");
        btn_descargar.setTooltip(tooltip);

        // TODO
    }    

    @FXML
    private void volver(ActionEvent event) {
        ControladorUtils.cambiarVista(datosCaso.getFxmlAnterior());
    }

    @FXML
    private void descargarArchivo(ActionEvent event) {
        
        byte[] archivo = datosCaso.getArchivo();


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo del caso");

        // Obtener extensión y generar nombre por defecto
        String extension = datosCaso.getExtension();
        if (extension == null || extension.isEmpty()) {
            extension = "dat"; // fallback por si no hay extensión
        }

        fileChooser.setInitialFileName("archivo_caso_" + datosCaso.getIdCaso() + "." + extension);

        File file = fileChooser.showSaveDialog(btn_descargar.getScene().getWindow());
        if (file != null) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(archivo);
                ControladorUtils.mostrarAlertaChill("Guardado exitoso", "Archivo guardado en: " + file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
    }

    @FXML
    private void ver_historial(ActionEvent event) {
    }

    @FXML
    private void asignarCaso(ActionEvent event) {
    }
}
