module org.example.jose_pr51 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;

    requires javafx.web;

    opens org.example.jose_pr51 to javafx.fxml;
    opens org.example.jose_pr51.controller to javafx.fxml;
    opens org.example.jose_pr51.model to javafx.base;

    exports org.example.jose_pr51;
    exports org.example.jose_pr51.controller;
    exports org.example.jose_pr51.model;
}