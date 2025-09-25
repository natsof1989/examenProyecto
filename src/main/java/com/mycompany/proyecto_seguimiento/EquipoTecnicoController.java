package com.mycompany.proyecto_seguimiento;

import com.mycompany.proyecto_seguimiento.clases.ControladorUtils;
import com.mycompany.proyecto_seguimiento.clases.ET_singleton;
import com.mycompany.proyecto_seguimiento.clases.ProfesorDAO;
import com.mycompany.proyecto_seguimiento.clases.SessionManager;
import com.mycompany.proyecto_seguimiento.clases.conexion;
import com.mycompany.proyecto_seguimiento.clases.equipoTecnicoDAO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class EquipoTecnicoController implements Initializable {

    @FXML
    private Button btn_configCuenta;
    @FXML
    private Button btn_logout;
    @FXML
    private Button btn_asignar;
    @FXML
    private Button btn_CASOS;
    @FXML
    private Text txt_nombre;
    @FXML
    private Text txt_apellido;

    private int ci;
    private boolean jefa;
    private final conexion dbConexion = new conexion();
    
    private final equipoTecnicoDAO equipoTecDAO = new equipoTecnicoDAO(dbConexion.getConnection());
    @FXML
    private Button btn_ORIENTA;
    @FXML
    private Button btn_ayuda;
    @FXML
    private Button btn_admi;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ET_singleton.getInstancia().setEquipos(equipoTecDAO.obtenerEquipos());
        txt_apellido.setText(SessionManager.getInstance().getUsuarioDatos().getApellido());
        txt_nombre.setText(SessionManager.getInstance().getUsuarioDatos().getNombre());
        SessionManager.getInstance().setPagina_anterior("equipoTecnico");
        try {
            
            ci = Integer.parseInt(SessionManager.getInstance().getCiUsuario());

            // Crear DAO y verificar departamento
            
            jefa = equipoTecDAO.perteneceDepartamento1(ci);

        } catch (NumberFormatException e) {
            Logger.getLogger(EquipoTecnicoController.class.getName()).log(Level.SEVERE, "CI inválido", e);
            jefa = false;
        } catch (SQLException e) {
            Logger.getLogger(EquipoTecnicoController.class.getName()).log(Level.SEVERE, null, e);
            jefa = false;
        }

        // Configurar botones según si es jefa o no
        if (!jefa) {
            btn_asignar.setVisible(false);
        }
    }

    @FXML
    private void configurarCuenta(ActionEvent event) {
        ControladorUtils.cambiarVista("configurarCuenta");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        if(ControladorUtils.mostrarConfirmacion("Confirmar logout", "¿Desea cerrar sesión?")){
                // Limpiar todo el estado de la sesión
           SessionManager.getInstance().limpiarSesion();

           // Cambiar a la vista de inicio de sesión
           ControladorUtils.cambiarVista("inicioSesion");
        }
       
    }


    @FXML
    private void asignarCaso(ActionEvent event) {
        ControladorUtils.cambiarVista("equipoTecnico1");
    }

   

    @FXML
    private void abrirOrientaciones(ActionEvent event) {
        ControladorUtils.cambiarVista("equipoTecnico2");
        
    }

    @FXML
    private void abrirCasos(ActionEvent event) {
        ControladorUtils.cambiarVista("equipoTecnico4");
    }

    @FXML
    private void ayuda(ActionEvent event) {
        String AyudaPath = getClass().getResource("/Ayuda/Seguimiento.chm").getPath(); 
        File file = new File(AyudaPath); 
        if(file.exists()){
            try{
                java.awt.Desktop.getDesktop().open(file);
            } catch(IOException ex) {
                Logger.getLogger(EquipoTecnicoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else{
            System.out.println("El archivo CHM no existe");
        }
    }

    @FXML
    private void admi(ActionEvent event) {
        ControladorUtils.cambiarVista("configDB");
    }
}
