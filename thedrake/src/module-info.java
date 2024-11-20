module thedrake {
    requires javafx.controls;
    requires javafx.fxml;


    opens thedrake.view to javafx.fxml;
    exports thedrake.view;
    exports thedrake.ui;
}