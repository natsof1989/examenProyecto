package com.mycompany.proyecto_seguimiento.clases;

import java.util.List;

public class SessionManager {
    private static SessionManager instance;
    private String ciUsuario;
    private List<String> rolesUsuario;
    private String rolSeleccionado;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Getters y Setters
    public String getCiUsuario() { return ciUsuario; }
    public void setCiUsuario(String ciUsuario) { this.ciUsuario = ciUsuario; }
    
    public List<String> getRolesUsuario() { return rolesUsuario; }
    public void setRolesUsuario(List<String> rolesUsuario) { this.rolesUsuario = rolesUsuario; }
    
    public String getRolSeleccionado() { return rolSeleccionado; }
    public void setRolSeleccionado(String rolSeleccionado) { this.rolSeleccionado = rolSeleccionado; }
    
    public void limpiarSesion() {
        this.ciUsuario = null;
        this.rolesUsuario = null;
        this.rolSeleccionado = null;
    }
}