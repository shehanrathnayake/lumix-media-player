package lk.ijse.dep11;

import javafx.application.Platform;
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

import static javafx.scene.media.MediaPlayer.*;

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

    public void initialize() {
        browseRoot.setVisible(false);

    }


    public void btnAddOnAction(ActionEvent actionEvent) {
        browseRoot.setVisible(true);
    }

    public void btnPlaylistOnAction(ActionEvent actionEvent) {
    }

    public void btnPlayOnAction(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            mediaPlayer.play();
            btnCloseBrowse.fire();

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
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Media Files", "*.mp3", "*.wav"));
        File mediaFile = fileChooser.showOpenDialog(root.getScene().getWindow());

        if (mediaFile != null) {
            txtBrowse.setText(mediaFile.getAbsolutePath());
            Media media = new Media(mediaFile.toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            lblDisplay.setText(String.valueOf(mediaPlayer.getBufferProgressTime()));
            mediaPlayer.setOnReady(() -> {
                Duration duration = media.getDuration();
                lblEndTime.setText(formatDuration(duration));
            });

        } else txtBrowse.clear();

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
