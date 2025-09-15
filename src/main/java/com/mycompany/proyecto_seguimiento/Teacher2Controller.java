package com.mycompany.proyecto_seguimiento;

import com.mycompany.proyecto_seguimiento.clases.CasoDAO;
import com.mycompany.proyecto_seguimiento.clases.CasoSeleccionado;
import com.mycompany.proyecto_seguimiento.clases.ControladorUtils;
import com.mycompany.proyecto_seguimiento.clases.ProfesorDAO;
import com.mycompany.proyecto_seguimiento.clases.SessionManager;
import com.mycompany.proyecto_seguimiento.clases.UsuarioDAO;
import com.mycompany.proyecto_seguimiento.clases.conexion;
import com.mycompany.proyecto_seguimiento.modelo.CasoResumen;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Teacher2Controller implements Initializable {

    @FXML
    private TableView<CasoResumen> tabla_casos;
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
    private TableColumn<CasoResumen, Boolean> col_seleccion;
    @FXML
    private TextField txt_buscar;
    @FXML
    private Button btn_abrir;
    @FXML
    private Button btn_informe;
    @FXML
    private Button btn_volver;

    private final SessionManager session = SessionManager.getInstance();
    private final conexion dbConexion = new conexion();
    private final ProfesorDAO profesorDao = new ProfesorDAO(dbConexion.getConnection());
    private final CasoDAO casoDAO = new CasoDAO(dbConexion.getConnection());

    private ObservableList<CasoResumen> registros = FXCollections.observableArrayList();
    private ObservableList<CasoResumen> registrosFiltrados = FXCollections.observableArrayList();
    // NUEVO: set para checkboxes seleccionados
    private Set<CasoResumen> seleccionados = new HashSet<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configuración de columnas
        col_idCaso.setCellValueFactory(new PropertyValueFactory<>("id_caso"));
        col_fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        col_estudiante.setCellValueFactory(new PropertyValueFactory<>("estudiante"));
        col_especialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        col_curso.setCellValueFactory(new PropertyValueFactory<>("curso"));

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

        // Columna de checkboxes
        col_seleccion.setCellValueFactory(data -> new SimpleBooleanProperty(false));
        col_seleccion.setCellFactory(tc -> new TableCell<CasoResumen, Boolean>() {
            private final CheckBox check = new CheckBox();
            {
                check.setOnAction(e -> {
                    CasoResumen caso = getTableRow().getItem();
                    if (caso != null) {
                        if (check.isSelected()) {
                            seleccionados.add(caso);
                        } else {
                            seleccionados.remove(caso);
                        }
                        actualizarBotones();
                    }
                });
            }
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    CasoResumen caso = getTableRow().getItem();
                    check.setSelected(seleccionados.contains(caso));
                    setGraphic(check);
                }
            }
        });

        // Cargar datos
        mostrarDatos();

        registrosFiltrados.setAll(registros);
        tabla_casos.setItems(registrosFiltrados);

        btn_abrir.setDisable(true);
        btn_informe.setDisable(true);
    }

    private void mostrarDatos() {
        String profesorCI = session.getCiUsuario();
        registros.setAll(profesorDao.obtenerCasosPorProfesor(profesorCI));
        /*
        private void mostrarDatos() {
        String espe = session.getEspe().getNombre();
        registros = FXCollections.observableArrayList(
                profesorDao.obtenerCasosPorEspe(espe)
        );
        tabla_casos.setItems(registros);
    }
        */
    }

    // Igual que en ReadCasosController
    private void actualizarBotones() {
        int sel = seleccionados.size();
        btn_informe.setDisable(sel == 0);
        btn_abrir.setDisable(sel != 1);
    }

    @FXML
    private void mostrarFila(MouseEvent event) {
        actualizarBotones();
    }

    @FXML
    private void buscarCaso(KeyEvent event) {
        String busqueda = txt_buscar.getText().toLowerCase().trim();
        registrosFiltrados.clear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (CasoResumen caso : registros) {
            String fechaStr = (caso.getFecha() != null) ? caso.getFecha().format(formatter).toLowerCase() : "";
            String estudiante = (caso.getEstudiante() != null) ? caso.getEstudiante().toLowerCase() : "";
            String especialidad = (caso.getEspecialidad() != null) ? caso.getEspecialidad().toLowerCase() : "";
            String curso = (caso.getCurso() != null) ? caso.getCurso().toLowerCase() : "";
            String idCaso = String.valueOf(caso.getId_caso());
            if (busqueda.isEmpty()
                    || estudiante.contains(busqueda)
                    || especialidad.contains(busqueda)
                    || curso.contains(busqueda)
                    || fechaStr.contains(busqueda)
                    || idCaso.contains(busqueda)) {
                registrosFiltrados.add(caso);
            }
        }
        tabla_casos.setItems(registrosFiltrados);
    }

    @FXML
    private void volver(ActionEvent event) {
        ControladorUtils.cambiarVista("teacher1");
    }

    @FXML
    private void AbrirCaso(ActionEvent event) {
        CasoResumen seleccionado;
        if (!seleccionados.isEmpty()) {
            seleccionado = seleccionados.iterator().next(); // Prioridad al checkbox
        } else {
            seleccionado = tabla_casos.getSelectionModel().getSelectedItem();
        }
        if (seleccionado != null) {
            CasoSeleccionado cs = CasoSeleccionado.getInstancia();
            cs.setIdCaso(seleccionado.getId_caso());
            cs.setFecha(seleccionado.getFecha());
            cs.setEstudiante(seleccionado.getEstudiante());
            cs.setCurso(seleccionado.getCurso());
            cs.setFxmlAnterior("teacher2");
            casoDAO.cargarDetalleCaso(seleccionado.getId_caso());
            ControladorUtils.cambiarVista("AbrirCaso");
        }
    }

    @FXML
    private void generarInforme(ActionEvent event) {
        if (seleccionados.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Seleccione al menos un caso para generar el informe.");
            alert.showAndWait();
            return;
        }

        // Tu lógica de JasperReports aquí
        // JasperReportUtils.generarInformeCasos(seleccionados);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Informe generado para " + seleccionados.size() + " casos (aquí iría Jasper).");
        alert.showAndWait();
    }
}