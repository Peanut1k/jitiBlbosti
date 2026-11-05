package cz.skola.losovatko;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LosovatkoApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                LosovatkoApplication.class.getResource("losovatko-view.fxml"));
        Scene scene = new Scene(loader.load(), 520, 760);;

        scene.getStylesheets().add(
                LosovatkoApplication.class.getResource("losovatko.css").toExternalForm());

        stage.setTitle("Ocenění pro učitele");
        stage.setMinWidth(520);
        stage.setMinHeight(750);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
