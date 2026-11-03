package cz.skola.losovatko;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LosovatkoApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // 1. FXML vytvoří rozhraní a propojí ho s controllerem.
        FXMLLoader loader = new FXMLLoader(
                LosovatkoApplication.class.getResource("losovatko-view.fxml"));
        Scene scene = new Scene(loader.load(), 560, 420);

        // 2. CSS určí vzhled prvků v načteném rozhraní.
        scene.getStylesheets().add(
                LosovatkoApplication.class.getResource("losovatko.css").toExternalForm());

        // 3. Scénu vložíme do hlavního okna.
        stage.setTitle("Losovátko Jíti");
        stage.setMinWidth(520);
        stage.setMinHeight(440);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
