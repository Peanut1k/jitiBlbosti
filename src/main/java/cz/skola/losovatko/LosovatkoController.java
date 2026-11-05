package cz.skola.losovatko;

import javafx.animation.KeyFrame;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LosovatkoController {

    private static final Logger LOGGER = Logger.getLogger(LosovatkoController.class.getName());
    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(44100, 16, 1, true, false);

    @FXML
    private Label jmenoLabel;

    @FXML
    private Label reel1;

    @FXML
    private Label reel2;

    @FXML
    private Label reel3;

    @FXML
    private HBox slotMachine;

    @FXML
    private Label slotStatus;

    @FXML
    private Pane celebrationLayer;

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
    private Timeline spinTimeline;
    private final ExecutorService audioExecutor = Executors.newSingleThreadExecutor(task -> {
        Thread thread = new Thread(task, "slot-machine-audio");
        thread.setDaemon(true);
        return thread;
    });

    @FXML
    private void initialize() {
        jmenoLabel.setText("Jíťa");
        reel1.setText("Jíťa");
        reel2.setText("Jíťa");
        reel3.setText("Jíťa");
        slotStatus.setText("Zkus své štěstí!");
        aktualizovatOceneni();
    }

    @FXML
    private void losuj() {
        roztoctSlot();
    }

    @FXML
    private void udelitPochvalu() {
        aktualizovatOceneni();
    }

    private void roztoctSlot() {
        if (spinTimeline != null) {
            spinTimeline.stop();
        }
        celebrationLayer.getChildren().clear();
        slotStatus.setText("Točíme…");
        slotMachine.setEffect(new DropShadow(28, Color.GOLD));

        int[] stopAt = {12, 15, 18};
        Label[] reels = {reel1, reel2, reel3};
        String[] outcomes = {
                jmena[nahoda.nextInt(jmena.length)],
                jmena[nahoda.nextInt(jmena.length)],
                jmena[nahoda.nextInt(jmena.length)]
        };
        spinTimeline = new Timeline();
        for (int i = 0; i <= 18; i++) {
            final int frameIndex = i;
            spinTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(65 * i), event -> {
                for (int reelIndex = 0; reelIndex < reels.length; reelIndex++) {
                    if (frameIndex < stopAt[reelIndex]) {
                        reels[reelIndex].setText(jmena[nahoda.nextInt(jmena.length)]);
                        reels[reelIndex].setRotate(nahoda.nextDouble() * 8 - 4);
                        reels[reelIndex].setScaleX(0.94 + nahoda.nextDouble() * 0.12);
                        reels[reelIndex].setScaleY(0.94 + nahoda.nextDouble() * 0.12);
                        reels[reelIndex].setEffect(new DropShadow(14, Color.web("#ffe58a")));
                    } else if (frameIndex == stopAt[reelIndex]) {
                        reels[reelIndex].setText(outcomes[reelIndex]);
                        reels[reelIndex].setRotate(0);
                        reels[reelIndex].setScaleX(1);
                        reels[reelIndex].setScaleY(1);
                        reels[reelIndex].setEffect(new DropShadow(26, Color.GOLD));
                        if (reelIndex == 1) {
                            jmenoLabel.setText(outcomes[reelIndex]);
                            aktualizovatOceneni();
                        }
                        ScaleTransition bounce = new ScaleTransition(Duration.millis(180), reels[reelIndex]);
                        bounce.setFromX(1);
                        bounce.setFromY(1);
                        bounce.setToX(1.12);
                        bounce.setToY(1.12);
                        bounce.setAutoReverse(true);
                        bounce.setCycleCount(2);
                        bounce.play();
                        zahrajTon(520 + reelIndex * 130, 55);
                    }
                }
                if (frameIndex < stopAt[2]) {
                    zahrajTon(250 + frameIndex * 22, 24);
                }
            }));
        }

        spinTimeline.setOnFinished(event -> {
            for (Label reel : reels) {
                reel.setEffect(new DropShadow(24, Color.GOLD));
                reel.setRotate(0);
                reel.setScaleX(1);
                reel.setScaleY(1);
            }
            slotStatus.setText("✨ JACKPOT! ✨");
            zahrajFanfaru();
            showCelebration();
            ScaleTransition pulse = new ScaleTransition(Duration.millis(250), slotMachine);
            pulse.setFromX(1);
            pulse.setFromY(1);
            pulse.setToX(1.035);
            pulse.setToY(1.035);
            pulse.setAutoReverse(true);
            pulse.setCycleCount(4);
            pulse.play();
            Timeline glow = new Timeline(
                    new KeyFrame(Duration.ZERO, e -> slotMachine.setEffect(new DropShadow(38, Color.GOLD))),
                    new KeyFrame(Duration.millis(500), e -> slotMachine.setEffect(new DropShadow(14, Color.ORANGE))));
            glow.setCycleCount(4);
            glow.setAutoReverse(true);
            glow.play();
        });
        spinTimeline.play();
    }

    private void showCelebration() {
        celebrationLayer.getChildren().clear();
        celebrationLayer.setPrefSize(560, 300);
        Color[] colors = {
                Color.GOLD, Color.HOTPINK, Color.AQUA, Color.LIME, Color.ORANGE, Color.MEDIUMPURPLE
        };

        for (int burst = 0; burst < 3; burst++) {
            double centerX = 90 + nahoda.nextDouble() * 380;
            double centerY = 45 + nahoda.nextDouble() * 130;
            Color burstColor = colors[nahoda.nextInt(colors.length)];

            Circle flash = new Circle(centerX, centerY, 5, burstColor);
            flash.setEffect(new DropShadow(18, burstColor));
            celebrationLayer.getChildren().add(flash);
            ScaleTransition flashScale = new ScaleTransition(Duration.millis(420), flash);
            flashScale.setFromX(0.2);
            flashScale.setFromY(0.2);
            flashScale.setToX(4.5);
            flashScale.setToY(4.5);
            FadeTransition flashFade = new FadeTransition(Duration.millis(420), flash);
            flashFade.setFromValue(1);
            flashFade.setToValue(0);
            ParallelTransition flashEffect = new ParallelTransition(flashScale, flashFade);
            flashEffect.setDelay(Duration.millis(burst * 180L));
            flashEffect.setOnFinished(event -> celebrationLayer.getChildren().remove(flash));
            flashEffect.play();

            for (int i = 0; i < 18; i++) {
                double angle = Math.PI * 2 * i / 18;
                double distance = 55 + nahoda.nextDouble() * 65;
                Circle spark = new Circle(centerX, centerY, 4 + nahoda.nextDouble() * 3,
                        i % 2 == 0 ? burstColor : colors[nahoda.nextInt(colors.length)]);
                spark.setEffect(new DropShadow(9, burstColor));
                celebrationLayer.getChildren().add(spark);
                animateParticle(spark, Math.cos(angle) * distance, Math.sin(angle) * distance,
                        650 + nahoda.nextInt(300), burst * 160L);
            }
        }

        for (int i = 0; i < 65; i++) {
            Shape confetti = i % 3 == 0
                    ? new Circle(3 + nahoda.nextDouble() * 3)
                    : i % 3 == 1
                    ? new Rectangle(5 + nahoda.nextDouble() * 6, 9 + nahoda.nextDouble() * 8)
                    : new Polygon(0, 0, 7, 0, 4, 9);
            confetti.setFill(colors[nahoda.nextInt(colors.length)]);
            confetti.setLayoutX(nahoda.nextDouble() * 550);
            confetti.setLayoutY(-10 - nahoda.nextDouble() * 100);
            celebrationLayer.getChildren().add(confetti);
            animateParticle(confetti, nahoda.nextDouble() * 160 - 80,
                    280 + nahoda.nextDouble() * 100, 1100 + nahoda.nextInt(900),
                    nahoda.nextInt(450));
        }
    }

    private void animateParticle(Shape particle, double xDistance, double yDistance,
                                 int durationMillis, long delayMillis) {
        TranslateTransition translate = new TranslateTransition(Duration.millis(durationMillis), particle);
        translate.setByX(xDistance);
        translate.setByY(yDistance);
        RotateTransition rotate = new RotateTransition(Duration.millis(durationMillis), particle);
        rotate.setByAngle(360 + nahoda.nextInt(720));
        FadeTransition fade = new FadeTransition(Duration.millis(durationMillis), particle);
        fade.setFromValue(1);
        fade.setToValue(0);
        ParallelTransition animation = new ParallelTransition(translate, rotate, fade);
        animation.setDelay(Duration.millis(delayMillis));
        animation.setOnFinished(event -> celebrationLayer.getChildren().remove(particle));
        animation.play();
    }

    private void zahrajTon(double frequency, int durationMillis) {
        audioExecutor.execute(() -> {
            SourceDataLine line = null;
            try {
                line = AudioSystem.getSourceDataLine(AUDIO_FORMAT);
                line.open(AUDIO_FORMAT);
                line.start();
                int sampleCount = (int) (AUDIO_FORMAT.getSampleRate() * durationMillis / 1000);
                byte[] samples = new byte[sampleCount * 2];
                for (int i = 0; i < sampleCount; i++) {
                    double envelope = Math.min(1, i / 180.0)
                            * Math.min(1, (sampleCount - i) / 300.0);
                    short sample = (short) (Short.MAX_VALUE * 0.22 * envelope
                            * Math.sin(2 * Math.PI * frequency * i / AUDIO_FORMAT.getSampleRate()));
                    samples[i * 2] = (byte) sample;
                    samples[i * 2 + 1] = (byte) (sample >> 8);
                }
                line.write(samples, 0, samples.length);
                line.drain();
            } catch (LineUnavailableException | IllegalArgumentException exception) {
                LOGGER.log(Level.FINE, "Slot-machine sound is unavailable.", exception);
            } finally {
                if (line != null) {
                    line.close();
                }
            }
        });
    }

    private void zahrajFanfaru() {
        int[] notes = {523, 659, 784, 1047};
        for (int note : notes) {
            zahrajTon(note, 110);
        }
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
