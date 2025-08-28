/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.proyecto_seguimiento;

import com.mycompany.proyecto_seguimiento.clases.CasoDAO;
import com.mycompany.proyecto_seguimiento.clases.CasoSeleccionado;
import com.mycompany.proyecto_seguimiento.clases.ControladorUtils;

import com.mycompany.proyecto_seguimiento.clases.conexion;
import com.mycompany.proyecto_seguimiento.clases.equipoTecnicoDAO;
import com.mycompany.proyecto_seguimiento.modelo.CasoResumen;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author natha
 */
public class EquipoTecnico1Controller implements Initializable {

    @FXML
    private Label label_title;
    @FXML
    private TableView<CasoResumen> tabla_caso;
    @FXML
    private TableColumn<CasoResumen, Integer> col_idCaso;
    @FXML
    private TableColumn<CasoResumen, LocalDateTime> col_fecha;
    @FXML
    private TableColumn<CasoResumen, String> col_estudiante;
    @FXML
    private TableColumn<CasoResumen, String> col_especialidad;
    @FXML
    private TableColumn<CasoResumen, String> col_curso;
    @FXML
    private TextField txt_buscar;
    

    /**
     * Initializes the controller class.
     */
    private final conexion dbConexion = new conexion();
    
    private final equipoTecnicoDAO equipoTecDao = new equipoTecnicoDAO(dbConexion.getConnection());

    private final CasoDAO casoDAO = new CasoDAO(dbConexion.getConnection()); 
     
    private ObservableList<CasoResumen> registros;
    @FXML
    private Button btn_abrir;
    @FXML
    private Button btn_volver;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        col_idCaso.setCellValueFactory(new PropertyValueFactory<>("id_caso"));
        col_fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        col_estudiante.setCellValueFactory(new PropertyValueFactory<>("estudiante"));
        col_especialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        col_curso.setCellValueFactory(new PropertyValueFactory<>("curso"));

        // 🔹 Formato personalizado para la columna fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        col_fecha.setCellFactory(column -> new TableCell<CasoResumen, LocalDateTime>() {
        @Override
        protected void updateItem(LocalDateTime fecha, boolean empty) {
            super.updateItem(fecha, empty);
              if (empty || fecha == null) {
                    setText(null);
               } else {
                    setText(fecha.format(formatter));
               }
            }
        });
        btn_abrir.setDisable(true);

        
        mostrarDatos();
    }

    @FXML
    private void mostrarFila(MouseEvent event) {
        btn_abrir.setDisable(tabla_caso.getSelectionModel().getSelectedItem() == null);
    }

    @FXML
    private void buscar(KeyEvent event) {
         String busqueda = txt_buscar.getText().toLowerCase().trim();

        if (busqueda.isEmpty()) {
            tabla_caso.setItems(registros); // Mostrar todos los registros
        } else {
            ObservableList<CasoResumen> registrosFiltrados = FXCollections.observableArrayList();

            for (CasoResumen caso : registros) {
                if ((caso.getEstudiante() != null && caso.getEstudiante().toLowerCase().contains(busqueda)) ||
                    (caso.getEspecialidad() != null && caso.getEspecialidad().toLowerCase().contains(busqueda)) ||
                    (caso.getCurso() != null && caso.getCurso().toLowerCase().contains(busqueda)) ||
                    (caso.getFecha() != null && caso.getFecha().toString().toLowerCase().contains(busqueda))) {
                    registrosFiltrados.add(caso);
                }
            }

            tabla_caso.setItems(registrosFiltrados);
        }
    }

    @FXML
    private void abrirCaso(ActionEvent event) {
         CasoResumen seleccionado = tabla_caso.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            
            CasoSeleccionado.getInstancia().setIdCaso(seleccionado.getId_caso());
            CasoSeleccionado.getInstancia().setFecha(seleccionado.getFecha());
            CasoSeleccionado.getInstancia().setEstudiante(seleccionado.getEstudiante());
            CasoSeleccionado.getInstancia().setCurso(seleccionado.getCurso());
            CasoSeleccionado.getInstancia().setFxmlAnterior("equipoTecnico1"); 
            casoDAO.cargarDetalleCaso(seleccionado.getId_caso());

            ControladorUtils.cambiarVista("AbrirCaso"); 
        }

    
    }
        
    
    private void mostrarDatos(){
         registros = FXCollections.observableArrayList(
                equipoTecDao.obtenerCasosSinEquipo()
         ); 
        
        tabla_caso.setItems(registros);
    }

    @FXML
    private void volver(ActionEvent event) {
        ControladorUtils.cambiarVista("equipoTecnico");
    }

   
    
    
}
