package com.mycompany.proyecto_seguimiento;

import com.mycompany.proyecto_seguimiento.clases.SessionManager;
import com.mycompany.proyecto_seguimiento.clases.ControladorUtils;
import com.mycompany.proyecto_seguimiento.clases.conexion;
import com.mycompany.proyecto_seguimiento.modelo.CasoDAO;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class Teacher3Controller implements Initializable {

    @FXML private MenuButton espe;
    @FXML private MenuButton curso;
    @FXML private MenuButton seccion;
    @FXML private ComboBox<AlumnoItem> comboAlumno;
    @FXML private TextArea txt_caso;
    @FXML private Text txt_nombre;
    @FXML private Text txt_apellido;
    @FXML private Button btn_guardar;

    private String especialidadSeleccionada;
    private String cursoSeleccionado;
    private String seccionSeleccionada;
    private AlumnoItem alumnoSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        espe.getItems().forEach(item -> item.setOnAction(e -> {
            especialidadSeleccionada = ((MenuItem) e.getSource()).getText();
            espe.setText(especialidadSeleccionada);
            cargarAlumnos();
        }));
        curso.getItems().forEach(item -> item.setOnAction(e -> {
            cursoSeleccionado = ((MenuItem) e.getSource()).getText();
            curso.setText(cursoSeleccionado);
            cargarAlumnos();
        }));
        seccion.getItems().forEach(item -> item.setOnAction(e -> {
            seccionSeleccionada = ((MenuItem) e.getSource()).getText();
            seccion.setText(seccionSeleccionada);
            cargarAlumnos();
        }));

        comboAlumno.valueProperty().addListener((obs, oldVal, newVal) -> {
            alumnoSeleccionado = newVal;
            if (newVal != null) {
                txt_nombre.setText(newVal.nombre);
                txt_apellido.setText(newVal.apellido);
            } else {
                txt_nombre.setText("");
                txt_apellido.setText("");
            }
        });
    }

    // Carga la lista de alumnos según filtros seleccionados
    private void cargarAlumnos() {
        comboAlumno.getItems().clear();
        txt_nombre.setText("");
        txt_apellido.setText("");
        alumnoSeleccionado = null;

        if (especialidadSeleccionada == null || cursoSeleccionado == null || seccionSeleccionada == null)
            return;

        try (Connection conn = new conexion().getConnection()) {
            String sql = "SELECT e.CI, e.nombre, e.apellido " +
                         "FROM estudiante e " +
                         "JOIN inscripcion i ON e.CI = i.estudiante_CI " +
                         "JOIN curso c ON i.curso_id_curso = c.id_curso " +
                         "JOIN especialidad es ON c.especialidad_id_especialidad = es.id_especialidad " +
                         "WHERE es.nombre = ? AND c.anhio = ? AND c.seccion = ? " +
                         "ORDER BY e.apellido, e.nombre";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, especialidadSeleccionada);
                ps.setString(2, cursoSeleccionado);
                ps.setString(3, seccionSeleccionada);
                try (ResultSet rs = ps.executeQuery()) {
                    List<AlumnoItem> alumnos = new ArrayList<>();
                    while (rs.next()) {
                        alumnos.add(new AlumnoItem(
                            rs.getInt("CI"),
                            rs.getString("nombre"),
                            rs.getString("apellido")
                        ));
                    }
                    comboAlumno.setItems(FXCollections.observableArrayList(alumnos));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al cargar alumnos.").showAndWait();
        }
    }

    @FXML
    private void onGuardarCaso() {
        if (especialidadSeleccionada == null || cursoSeleccionado == null || seccionSeleccionada == null
                || alumnoSeleccionado == null || txt_caso.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Completa todos los campos.").showAndWait();
            return;
        }

        Integer profesorCI = obtenerCedulaProfesor();
        if (profesorCI == null) {
            new Alert(Alert.AlertType.ERROR, "No se pudo obtener la cédula del profesor desde la sesión.").showAndWait();
            return;
        }

        try (Connection conn = new conexion().getConnection()) {
            CasoDAO dao = new CasoDAO(conn);
            boolean ok = dao.insertarCaso(
                profesorCI,
                alumnoSeleccionado.ci,
                txt_caso.getText().trim(),
                especialidadSeleccionada,
                cursoSeleccionado,
                seccionSeleccionada
            );
            if (ok) {
                new Alert(Alert.AlertType.INFORMATION, "Caso creado correctamente.").showAndWait();
                limpiarFormulario();
            } else {
                new Alert(Alert.AlertType.ERROR, "No se pudo crear el caso.").showAndWait();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al conectar a la base de datos.").showAndWait();
        }
    }

    /**
     * Intenta obtener la cédula del profesor desde SessionManager buscando en:
     * 1) session.getUsuario()  -> varios getters posibles en Usuario (getCi, getCI, getCedula, getId)
     * 2) session.getUsuarioDatos().getUsuario() -> idem
     * 3) session.getUsuarioDatos() directamente (si guarda la cédula ahí con alguno de los getters/fields)
     *
     * Usa reflexión para no depender de firmas exactas.
     */
    private Integer obtenerCedulaProfesor() {
        SessionManager session = SessionManager.getInstance();
        if (session == null) return null;

        // 1) Intento directo: session.getUsuario()
        try {
            Method mGetUsuario = session.getClass().getMethod("getUsuario");
            Object usuario = mGetUsuario.invoke(session);
            Integer ci = extraerCI(usuario);
            if (ci != null) return ci;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            // sigue intentando otras rutas
        }

        // 2) Intento desde session.getUsuarioDatos()
        try {
            Method mGetUsuarioDatos = session.getClass().getMethod("getUsuarioDatos");
            Object usuarioDatos = mGetUsuarioDatos.invoke(session);
            if (usuarioDatos != null) {
                // 2a) usuarioDatos.getUsuario()
                try {
                    Method mGetUsuario2 = usuarioDatos.getClass().getMethod("getUsuario");
                    Object usuario = mGetUsuario2.invoke(usuarioDatos);
                    Integer ci = extraerCI(usuario);
                    if (ci != null) return ci;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}

                // 2b) tal vez usuarioDatos tiene la cédula directamente (getCedula/getCi...)
                Integer ciDirecto = extraerCI(usuarioDatos);
                if (ciDirecto != null) return ciDirecto;
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            // si no existe getUsuarioDatos o falla, seguimos
        }

        // 3) Como último recurso, si SessionManager tiene algún campo público o getter alternativo que contenga usuario,
        // lo intentamos (ej. campo "usuario" o "usuarioDatos")
        Integer ciFromField = buscarCampoCIEnObjeto(session);
        if (ciFromField != null) return ciFromField;

        return null;
    }

    // Intenta extraer la cédula (int) desde un objeto (Usuario o UsuarioDatos) probando varios getters y fields comunes
    private Integer extraerCI(Object obj) {
        if (obj == null) return null;

        // Intenta métodos comunes
        String[] methodNames = { "getCi", "getCI", "getCedula", "getCedulaInt", "getId", "getUsuarioId", "getCedulaProfesor" };
        for (String name : methodNames) {
            try {
                Method m = obj.getClass().getMethod(name);
                Object val = m.invoke(obj);
                Integer ci = parseNumberLike(val);
                if (ci != null) return ci;
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}
        }

        // Intenta campos directos
        String[] fieldNames = { "ci", "CI", "cedula", "id", "cedulaProfesor" };
        for (String fName : fieldNames) {
            try {
                Field f = obj.getClass().getDeclaredField(fName);
                f.setAccessible(true);
                Object val = f.get(obj);
                Integer ci = parseNumberLike(val);
                if (ci != null) return ci;
            } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        }

        return null;
    }

    // Si encuentra un número o string numérico lo devuelve como Integer
    private Integer parseNumberLike(Object val) {
        if (val == null) return null;
        if (val instanceof Number) {
            return ((Number) val).intValue();
        }
        if (val instanceof String) {
            try {
                return Integer.parseInt(((String) val).trim());
            } catch (NumberFormatException ignored) {}
        }
        return null;
    }

    // Busca en campos del objeto session (campo "usuario" o "usuarioDatos") por si acaso
    private Integer buscarCampoCIEnObjeto(Object session) {
        String[] sessionFieldNames = { "usuario", "usuarioDatos", "user", "userData" };
        for (String fld : sessionFieldNames) {
            try {
                Field f = session.getClass().getDeclaredField(fld);
                f.setAccessible(true);
                Object val = f.get(session);
                Integer ci = extraerCI(val);
                if (ci != null) return ci;
            } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        }
        return null;
    }

    private void limpiarFormulario() {
        espe.setText("Especialidad");
        curso.setText("Curso");
        seccion.setText("Sección");
        especialidadSeleccionada = null;
        cursoSeleccionado = null;
        seccionSeleccionada = null;
        comboAlumno.getItems().clear();
        txt_nombre.setText("");
        txt_apellido.setText("");
        txt_caso.clear();
        alumnoSeleccionado = null;
    }

    @FXML
    private void volver() {
        ControladorUtils.cambiarVista("teacher1");
    }

    // Clase interna simple para el ComboBox de alumnos
    public static class AlumnoItem {
        public final int ci;
        public final String nombre;
        public final String apellido;

        public AlumnoItem(int ci, String nombre, String apellido) {
            this.ci = ci;
            this.nombre = nombre;
            this.apellido = apellido;
        }

        @Override
        public String toString() {
            return apellido + ", " + nombre + " (" + ci + ")";
        }
    }
}
