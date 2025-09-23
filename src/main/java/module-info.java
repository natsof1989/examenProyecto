module com.mycompany.examenproyecto {
    
     requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;
    requires java.mail;
    requires jbcrypt;

    opens com.mycompany.examenproyecto to javafx.fxml;
    opens com.mycompany.examenproyecto.clases to javafx.fxml;
    opens com.mycompany.examenproyecto.modelo to javafx.base;

    exports com.mycompany.examenproyecto;
    
    exports com.mycompany.examenproyecto.clases;
    exports com.mycompany.examenproyecto.modelo;
    requires java.desktop; 
    
}
