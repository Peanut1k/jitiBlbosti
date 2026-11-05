package cz.skola.losovatko;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LosovatkoController {

    @FXML
    private Label jmenoLabel;

    @FXML
    private CheckBox bereNasVazneCheck;

    @FXML
    private CheckBox opraviBugCheck;

    @FXML
    private CheckBox vysvetliCheck;

    @FXML
    private VBox oceneniBox;

    @FXML
    private VBox dovodyBox;

    private final String[] jmena = {
            "Danisikova", "Burda", "Belsky", "Abrham", "Samanova"
    };

    private final Random nahoda = new Random();

    @FXML
    private void initialize() {
        aktualizovatOceneni();
    }

    @FXML
    private void losuj() {
        int index = nahoda.nextInt(jmena.length);
        jmenoLabel.setText(jmena[index]);
    }

    @FXML
    private void udelitPochvalu() {
        aktualizovatOceneni();
    }

    private void aktualizovatOceneni() {
        List<CheckBox> vybrane = new ArrayList<>();
        if (bereNasVazneCheck.isSelected()) {
            vybrane.add(bereNasVazneCheck);
        }
        if (opraviBugCheck.isSelected()) {
            vybrane.add(opraviBugCheck);
        }
        if (vysvetliCheck.isSelected()) {
            vybrane.add(vysvetliCheck);
        }

        String jmeno = jmenoLabel.getText();
        String labelText = jmeno + " si zaslouží ocenění.";
        Label uvod = (Label) oceneniBox.lookup("#oceneniUvod");
        if (uvod != null) {
            uvod.setText(labelText);
        }

        dovodyBox.getChildren().clear();
        for (CheckBox box : vybrane) {
            Label radek = new Label("• " + box.getText());
            radek.getStyleClass().add("odrazka");
            dovodyBox.getChildren().add(radek);
        }

        oceneniBox.setVisible(true);
        oceneniBox.setManaged(true);
    }
}
