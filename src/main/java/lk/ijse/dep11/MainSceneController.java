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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
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
    public Button btnBrowse;
    public Button btnCloseBrowse;
    public TextField txtBrowse;
    public HBox browseRoot;

    MediaPlayer mediaPlayer;
    Duration duration;

    public void initialize() {
        browseRoot.setVisible(false);

    }


    public void btnAddOnAction(ActionEvent actionEvent) {
//        browseRoot.setVisible(true);
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Media Files", "*.mp3", "*.wav"));
        File mediaFile = fileChooser.showOpenDialog(root.getScene().getWindow());

        if (mediaFile != null) {
            txtBrowse.setText(mediaFile.getAbsolutePath());
            Media media = new Media(mediaFile.toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            lblDisplay.setText(mediaFile.getName());

            mediaPlayer.setOnReady(() -> {
                duration = media.getDuration();
                lblEndTime.setText(formatDuration(duration));

            });



        } else txtBrowse.clear();
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
        }
    }

    public void btnBackOnAction(ActionEvent actionEvent) {
    }

    public void btnForwardOnAction(ActionEvent actionEvent) {
        mediaPlayer.pause();
    }

    public void btnStopOnAction(ActionEvent actionEvent) {
        mediaPlayer.stop();
    }

    public void btnBrowseOnAction(ActionEvent actionEvent) {


    }

    public void btnCloseBrowseOnAction(ActionEvent actionEvent) {
        browseRoot.setVisible(false);
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
