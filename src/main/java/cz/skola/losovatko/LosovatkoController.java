package cz.skola.losovatko;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Random;

public class LosovatkoController {

    // Stejné názvy jako fx:id ve FXML. Objekty doplní FXMLLoader.
    @FXML
    private Label jmenoLabel;

    @FXML
    private Label pocetLabel;

    private final String[] jmena = {
            "Mates", "Péťa", "Fanda", "Šáry", "Oli", "Ali", "Míra",
            "Dan", "Marťas", "David", "Kája", "Pepa", "Martin", "Kornel"
    };

    private final Random nahoda = new Random();
    private int pocetLosovani = 0;

    // Ve FXML tlačítko odkazuje na tuto metodu: onAction="#losuj".
    @FXML
    private void losuj() {
        // nextInt vrací index od 0 včetně do jmena.length bez této hranice.
        int index = nahoda.nextInt(jmena.length);
        jmenoLabel.setText(jmena[index]);

        pocetLosovani++;
        pocetLabel.setText("Počet losování: " + pocetLosovani);
    }
}
