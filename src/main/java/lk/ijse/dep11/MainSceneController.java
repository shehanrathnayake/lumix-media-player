package lk.ijse.dep11;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
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
    public TextField txtBrowse;
    public Button btnPause;

    MediaPlayer mediaPlayer;
    Duration duration;

    public void initialize() {

        Font font = Font.loadFont(getClass().getResourceAsStream("/asset/font/Orbitron-Regular.ttf"),12);
        lblDisplay.setFont(font);

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

//            Image playlistImage = new Image(getClass().getResourceAsStream("/asset/img/playlist.png"));
//            ImageView playlistImageView = new ImageView(playlistImage);
//            playlistImageView.setFitWidth(10);
//            playlistImageView.setFitHeight(10);
//            btnPlaylist.setGraphic(playlistImageView);
        });

        btnPlay.setVisible(true);
        btnPause.setVisible(false);
        btnPlay.requestFocus();

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
        else filePath = txtBrowse.getText();

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

    private String formatDuration(Duration duration) {
        long seconds = (long) duration.toSeconds();
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }


}
