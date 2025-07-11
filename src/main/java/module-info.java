module com.mycompany.proyecto_seguimiento {
    requires javafx.controls;
    requires javafx.fxml;
    
    requires java.sql; 
    requires java.base;  
    opens com.mycompany.proyecto_seguimiento to javafx.fxml;
    exports com.mycompany.proyecto_seguimiento;
}
