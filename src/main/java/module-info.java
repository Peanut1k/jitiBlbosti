module cz.skola.losovatko {
    requires javafx.controls;
    requires javafx.fxml;

    // FXMLLoader smí propojit i private pole a metody označené @FXML.
    opens cz.skola.losovatko to javafx.fxml;
    exports cz.skola.losovatko;
}
