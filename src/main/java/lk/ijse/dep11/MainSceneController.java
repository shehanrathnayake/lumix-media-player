package lk.ijse.dep11;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class MainSceneController {
    public AnchorPane root;
    public AnchorPane panelRoot;
    public Button btnAdd;
    public Button btnPlaylist;
    public Button btnPlay;
    public Label lblDisplay;
    public Button btnBack;
    public Button btnForward;
    public ProgressBar pbTimeline;
    public Label lblEndTime;
    public Label lblStartTime;
    public Button btnStop;
    public Button btnPause;
    public Button btnMinimize;
    public Button btnClose;
    public HBox labelRoot;

    MediaPlayer mediaPlayer;
    Duration duration;

    double xPos;
    double yPos;

    public void initialize() {

        Font fontDisplay = Font.loadFont(getClass().getResourceAsStream("/asset/font/Digital Display.ttf"),22);
        lblDisplay.setFont(fontDisplay);
//        // Increase letter spacing for lblDisplay
//        lblDisplay.setStyle("-fx-letter-spacing: 100px;");


        Platform.runLater(()->{
            Image playImage = new Image(getClass().getResourceAsStream("/asset/img/play.png"));
            ImageView playImageView = new ImageView(playImage);
            btnPlay.setGraphic(playImageView);

            Image pauseImage = new Image(getClass().getResourceAsStream("/asset/img/pause.png"));
            ImageView pauseImageView = new ImageView(pauseImage);
            btnPause.setGraphic(pauseImageView);

            Image playPreviousImage = new Image(getClass().getResourceAsStream("/asset/img/skip-previous.png"));
            ImageView playPreviousImageView = new ImageView(playPreviousImage);
            playPreviousImageView.setFitWidth(16);
            playPreviousImageView.setFitHeight(16);
            btnBack.setGraphic(playPreviousImageView);

            Image playNextImage = new Image(getClass().getResourceAsStream("/asset/img/skip-next.png"));
            ImageView playNextImageView = new ImageView(playNextImage);
            playNextImageView.setFitWidth(16);
            playNextImageView.setFitHeight(16);
            btnForward.setGraphic(playNextImageView);

            Image stopImage = new Image(getClass().getResourceAsStream("/asset/img/stop.png"));
            ImageView stopImageView = new ImageView(stopImage);
            stopImageView.setFitWidth(16);
            stopImageView.setFitHeight(16);
            btnStop.setGraphic(stopImageView);

            Image closeImage = new Image(getClass().getResourceAsStream("/asset/img/close.png"));
            ImageView closeImageView = new ImageView(closeImage);
            closeImageView.setFitWidth(10);
            closeImageView.setFitHeight(10);
            btnClose.setGraphic(closeImageView);

            Image minimizeImage = new Image(getClass().getResourceAsStream("/asset/img/minimize.png"));
            ImageView minimizeImageView = new ImageView(minimizeImage);
            minimizeImageView.setFitWidth(10);
            minimizeImageView.setFitHeight(10);
            btnMinimize.setGraphic(minimizeImageView);
        });

        btnPlay.setVisible(true);
        btnPause.setVisible(false);
        btnAdd.requestFocus();

    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            btnStop.fire();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Media Files", "*.mp3", "*.wav"));
        File mediaFile = fileChooser.showOpenDialog(root.getScene().getWindow());

        String filePath = "";
        if (mediaFile != null) {
            filePath = mediaFile.toURI().toString();
            lblDisplay.setText(mediaFile.getName());
        }
        else filePath = lblDisplay.getText();

        Media media = new Media(filePath);
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnReady(() -> {
            duration = media.getDuration();
            lblEndTime.setText(formatDuration(duration));
        });

    }

    public void btnPlaylistOnAction(ActionEvent actionEvent) {
    }

    public void btnPlayOnAction(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            Task<Void> calculateProgress = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    mediaPlayer.play();
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                        Duration currentTime = mediaPlayer.getCurrentTime();
                        double progress = currentTime.toMillis()/ mediaPlayer.getTotalDuration().toMillis();

                        Platform.runLater(()->{
                            lblStartTime.setText(formatDuration(currentTime));
                            pbTimeline.setProgress(progress);
                        });

                    }));
                    timeline.setCycleCount(Timeline.INDEFINITE);
                    timeline.play();
                    return null;
                }
            };

            new Thread (calculateProgress).start();

            mediaPlayer.setOnPlaying(() -> {
                btnPause.setVisible(true);
                btnPlay.setVisible(false);
                btnPause.requestFocus();
            });
        }
    }

    public void btnBackOnAction(ActionEvent actionEvent) {
    }

    public void btnForwardOnAction(ActionEvent actionEvent) {

    }

    public void btnStopOnAction(ActionEvent actionEvent) {
        mediaPlayer.stop();

        mediaPlayer.setOnStopped(()->{
            btnPause.setVisible(false);
            btnPlay.setVisible(true);
            btnPlay.requestFocus();
        });
    }

    public void btnPauseOnAction(ActionEvent actionEvent) {
        mediaPlayer.pause();

        mediaPlayer.setOnPaused(()->{
            btnPause.setVisible(false);
            btnPlay.setVisible(true);
            btnPlay.requestFocus();
        });
    }

    public void rootOnMousePressed(MouseEvent e) {
        xPos = e.getX();
        yPos = e.getY();
    }

    public void rootOnMouseDragged(MouseEvent e) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setX(e.getScreenX()-xPos);
        stage.setY(e.getScreenY()-yPos);
    }

    public void btnMinimizeOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setIconified(true);
    }

    public void btnCloseOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void rootOnKeyPressed(KeyEvent keyEvent) {
//        if (keyEvent.getCode() == KeyCode.LEFT) if (mediaPlayer != null) mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-5)));
//        else if (keyEvent.getCode() == KeyCode.RIGHT) if (mediaPlayer != null) mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(5)));
    }

    private String formatDuration(Duration duration) {
        long seconds = (long) duration.toSeconds();
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void handleKeyPressed (KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.LEFT) {
            if (mediaPlayer != null) {
                mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-5)));
//                mediaPlayer.setOnPlaying(()->{btnPlay.requestFocus();});
//                mediaPlayer.setOnPaused(()->{btnPause.requestFocus();});
                btnPlay.requestFocus();
            }
        }
        if (keyEvent.getCode() == KeyCode.RIGHT) {
            if (mediaPlayer != null) {
                mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(5)));
//                mediaPlayer.setOnPlaying(()->{btnPlay.requestFocus();});
//                mediaPlayer.setOnPaused(()->{btnPause.requestFocus();});
                btnPlay.requestFocus();
            }
        }
        if (keyEvent.getCode() == KeyCode.SPACE) {
            btnPlay.requestFocus();
            mediaPlayer.setOnPlaying(()->{
                btnPause.requestFocus();
                btnPause.fire();
            });
            mediaPlayer.setOnPaused(()->{
//                btnPlay.requestFocus();
                btnPlay.fire();
            });
        }

    }

    public void setRootScene(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
    }

}
