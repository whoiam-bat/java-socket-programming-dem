module com.example.server {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.naming;
    requires static lombok;


    opens com.example.server to javafx.fxml;
    exports com.example.server;
}