package com.mycompany.examenproyecto;


import com.mycompany.examenproyecto.clases.ControladorUtils;
import com.mycompany.examenproyecto.clases.claseDAO;
import com.mycompany.examenproyecto.clases.conexion;
import com.mycompany.examenproyecto.modelo.Alumno;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class FormularioController implements Initializable {

    @FXML private TextField txt_id;
    @FXML private TextField txt_nombre;
    @FXML private TextField txt_apellido;
    @FXML private TextField txt_buscar;

    @FXML private Button btn_nuevo;
    @FXML private Button btn_modificar;
    @FXML private Button btn_eliminar;
    @FXML private Button btn_guardar;
    @FXML private Button btn_cancelar;

    @FXML private TableView<Alumno> tabla_registros;
    @FXML private TableColumn<Alumno, Integer> col_id;
    @FXML private TableColumn<Alumno, String> col_nombre;
    @FXML private TableColumn<Alumno, String> col_apellido;

    private claseDAO dao;
    private ObservableList<Alumno> listaAlumnos;
    private boolean editado = false; 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Conexión usando tu clase "conexion"
        conexion conBD = new conexion();
        dao = new claseDAO(conBD.getConnection());

        // Configurar columnas
        col_id.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        col_nombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        col_apellido.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getApellido()));

        cargarTabla();
        deshabilitarCampos();
    }

    private void cargarTabla() {
        listaAlumnos = FXCollections.observableArrayList(dao.obtenerTodos());
        tabla_registros.setItems(listaAlumnos);
    }

    private void limpiarCampos() {
        txt_id.clear();
        txt_nombre.clear();
        txt_apellido.clear();
    }

    private void deshabilitarCampos() {
        txt_id.setDisable(true);
        txt_nombre.setDisable(true);
        txt_apellido.setDisable(true);
        btn_guardar.setDisable(true);
        btn_cancelar.setDisable(true);
        btn_modificar.setDisable(true);
        btn_eliminar.setDisable(true);
    }

    @FXML
    private void nuevo(ActionEvent event) {
        limpiarCampos();
        txt_id.setDisable(false);
        txt_nombre.setDisable(false);
        txt_apellido.setDisable(false);
        btn_guardar.setDisable(false);
        btn_cancelar.setDisable(false);
        btn_modificar.setDisable(true);
        btn_eliminar.setDisable(true);
        btn_nuevo.setDisable(true);
    }

    @FXML
    private void guardar(ActionEvent event) {
        if(ControladorUtils.hayCamposVacios(txt_apellido, txt_nombre)){
                ControladorUtils.mostrarAlerta("Informampos", "No pueden haber campos vacíos");
                return; 
        }
        if(editado){
            
            int id = Integer.parseInt(txt_id.getText()); 
            String nombre = txt_nombre.getText(); 
            String apellido = txt_apellido.getText(); 
            
            if(dao.modificarAlumno(id, nombre, apellido)){
                ControladorUtils.mostrarAlertaChill("Aviso", "Modificación exitosa");
                editado = false; 
            }
        } else{
            
            try {
            int id = Integer.parseInt(txt_id.getText());
            String nombre = txt_nombre.getText();
            String apellido = txt_apellido.getText();

            if (dao.insertarAlumno(id, nombre, apellido)) {
                
                limpiarCampos();
                deshabilitarCampos();
                ControladorUtils.mostrarAlertaChill("Informamos", nombre + " " + apellido + " fue guardado con éxito.");
            } else {
                System.out.println("Error al guardar el alumno");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID debe ser un número");
        }
        }
        cargarTabla();
    }

    @FXML
    private void modificar(ActionEvent event) {
        txt_apellido.setDisable(false);
        txt_nombre.setDisable(false);
       btn_guardar.setDisable(false);
       btn_cancelar.setDisable(false);
       btn_modificar.setDisable(true);
       editado = true; 
    }

    @FXML
    private void eliminar(ActionEvent event) {
      if(ControladorUtils.mostrarConfirmacion("Confirme antes de eliminar", "¿Desea eliminar a " + txt_nombre.getText() + " " + txt_apellido.getText() + "?")){
            Alumno a = tabla_registros.getSelectionModel().getSelectedItem();
            if (a != null && dao.eliminarAlumno(a.getId())) {
                cargarTabla();
                limpiarCampos();
                deshabilitarCampos();
                ControladorUtils.mostrarAlertaChill("Informamos", "Eliminación exitosa");
            }
            
        } else{
          cancelar(event);
      }
       
    }

    @FXML
    private void cancelar(ActionEvent event) {
        limpiarCampos();
        deshabilitarCampos();
    }

    @FXML
    private void buscar(KeyEvent event) {
        String filtro = txt_buscar.getText().toLowerCase();
        ObservableList<Alumno> filtrados = FXCollections.observableArrayList();
        for (Alumno a : dao.obtenerTodos()) {
            if (String.valueOf(a.getId()).contains(filtro)
                    || a.getNombre().toLowerCase().contains(filtro)
                    || a.getApellido().toLowerCase().contains(filtro)) {
                filtrados.add(a);
            }
        }
        tabla_registros.setItems(filtrados);
    }

    @FXML
    private void mostrarFila(MouseEvent event) {
        Alumno a = tabla_registros.getSelectionModel().getSelectedItem();
        if (a != null) {
            txt_id.setText(String.valueOf(a.getId()));
            txt_nombre.setText(a.getNombre());
            txt_apellido.setText(a.getApellido());

            txt_id.setDisable(true);
            txt_nombre.setDisable(true);
            txt_apellido.setDisable(true);
            
            btn_modificar.setDisable(false);
            btn_eliminar.setDisable(false);
            btn_guardar.setDisable(true);
            btn_cancelar.setDisable(false);
        }
    }
}
