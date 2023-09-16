package lk.ijse.dep11;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public Slider sldVolume;
    public Label lblVolume;
    public Button btnAbout;
    public AnchorPane panelVideoRoot;
    public Slider sldVolumeVideo;
    public Label lblVolumeVideo;
    public MediaView mvView;
    public Button btnAboutVideo;
    public AnchorPane panelBoardRoot;
    public ProgressBar pbTimelineVideo;
    public Label lblStartTimeVideo;
    public Label lblEndTimeVideo;
    public Button btnAddVideo;
    public Button btnPlaylistVideo;
    public Button btnBackVideo;
    public Button btnPlayVideo;
    public Button btnForwardVideo;
    public Button btnPauseVideo;

    MediaPlayer mediaPlayer;
    Duration duration;

    double xPos;
    double yPos;

    public static final List<String> AUDIO_EXTENSIONS = Arrays.asList(".mp3",".wav");
    public static final List<String> VIDEO_EXTENSIONS = Arrays.asList(".mp4", ".avi",".mkv");

    public void initialize() {

        Font fontDisplay = Font.loadFont(getClass().getResourceAsStream("/asset/font/FrozenCrystal-Z34x.otf"),22);
        lblDisplay.setFont(fontDisplay);

        Platform.runLater(()->{
            Image playImage = new Image(getClass().getResourceAsStream("/asset/img/play.png"));
            ImageView playImageView = new ImageView(playImage);
            btnPlay.setGraphic(playImageView);

            ImageView playVideoImageView = new ImageView(playImage);
            btnPlayVideo.setGraphic(playVideoImageView);

            Image pauseImage = new Image(getClass().getResourceAsStream("/asset/img/pause.png"));
            ImageView pauseImageView = new ImageView(pauseImage);
            btnPause.setGraphic(pauseImageView);

            Image playPreviousImage = new Image(getClass().getResourceAsStream("/asset/img/skip-previous.png"));
            ImageView playPreviousImageView = new ImageView(playPreviousImage);
            playPreviousImageView.setFitWidth(16);
            playPreviousImageView.setFitHeight(16);
            btnBack.setGraphic(playPreviousImageView);

            ImageView playPreviousVideoImageView = new ImageView(playPreviousImage);
            playPreviousVideoImageView.setFitWidth(16);
            playPreviousVideoImageView.setFitHeight(16);
            btnBackVideo.setGraphic(playPreviousVideoImageView);

            Image playNextImage = new Image(getClass().getResourceAsStream("/asset/img/skip-next.png"));
            ImageView playNextImageView = new ImageView(playNextImage);
            playNextImageView.setFitWidth(16);
            playNextImageView.setFitHeight(16);
            btnForward.setGraphic(playNextImageView);

            ImageView playNextVideoImageView = new ImageView(playNextImage);
            playNextVideoImageView.setFitWidth(16);
            playNextVideoImageView.setFitHeight(16);
            btnForwardVideo.setGraphic(playNextVideoImageView);

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

        root.setPrefHeight(panelRoot.getHeight());

        panelRoot.setVisible(true);
        panelVideoRoot.setVisible(false);

        btnPlay.setVisible(true);
        btnPlayVideo.setVisible(true);

        btnPause.setVisible(false);
        btnPauseVideo.setVisible(false);

        btnAdd.requestFocus();

        sldVolume.valueProperty().addListener(e->{
            double volume = sldVolume.getValue();
            System.out.println(volume);

            mediaPlayer.setVolume(volume/100);
            System.out.println(mediaPlayer.getVolume());
            lblVolume.setText(String.format("%.0f",volume).concat("%"));
        });

        sldVolumeVideo.valueProperty().addListener(e->{
            double volume = sldVolumeVideo.getValue();
            System.out.println(volume);

            mediaPlayer.setVolume(volume/100);
            System.out.println(mediaPlayer.getVolume());
            lblVolumeVideo.setText(String.format("%.0f",volume).concat("%"));
        });
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            btnStop.fire();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Media Files", "*.mp3", "*.wav", "*.mp4", "*.avi","*.mkv"));
        File mediaFile = fileChooser.showOpenDialog(root.getScene().getWindow());

        String filePath = "";
        if (mediaFile != null) {
            filePath = mediaFile.toURI().toString();
            lblDisplay.setText(mediaFile.getName());
        }
        else filePath = lblDisplay.getText();

        Media media = new Media(filePath);
        mediaPlayer = new MediaPlayer(media);

        final String FILEPATH = filePath;
        mediaPlayer.setOnReady(() -> {

            Stage stage = (Stage) root.getScene().getWindow();
            if (VIDEO_EXTENSIONS.contains(FILEPATH.substring(FILEPATH.length()-4,FILEPATH.length()))) {
                System.out.println("Video file detected");
                panelRoot.setVisible(false);
                panelVideoRoot.setVisible(true);

                double preferredHeight = (panelVideoRoot.getHeight());
//                double preferredHeight = (9 * panelVideoRoot.getWidth() /16.0);
                System.out.println(preferredHeight);
                System.out.println(panelVideoRoot.getWidth());

                stage.setHeight(panelVideoRoot.getWidth() * 9/16.0 + 89);
                stage.setResizable(true);
//                Platform.runLater(()->{
//                    mvView.setFitHeight(preferredHeight);
//                    panelVideoRoot.setPrefHeight(preferredHeight + 64);
//                    root.setPrefHeight(panelVideoRoot.getPrefHeight());
//                });
                mvView.setMediaPlayer(mediaPlayer);
            } else {
                stage.setHeight(panelRoot.getHeight());
                panelRoot.setVisible(true);
                panelVideoRoot.setVisible(false);
            }


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

                    System.out.println(mediaPlayer.getVolume());
                    sldVolume.setValue(mediaPlayer.getVolume() * 100);

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
            }
        }
        if (keyEvent.getCode() == KeyCode.RIGHT) {
            if (mediaPlayer != null) {
                mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(5)));
            }
        }
        if ((keyEvent.getCode() == KeyCode.SPACE) || (keyEvent.getCode() == KeyCode.ENTER)) {
            if (mediaPlayer != null) {
                MediaPlayer.Status status = mediaPlayer.getStatus();
                if (status == MediaPlayer.Status.PLAYING) {
                    btnPause.fire();
                }
                if (status == MediaPlayer.Status.PAUSED) {
                    btnPlay.fire();
                }
            } else {
                btnAdd.fire();
            }
        }
        if (keyEvent.getCode() == KeyCode.UP) {
            if (mediaPlayer != null) sldVolume.setValue(sldVolume.getValue() + 5 - (sldVolume.getValue() % 5));
        }

        if (keyEvent.getCode() == KeyCode.DOWN) {
            if (mediaPlayer != null) sldVolume.setValue(sldVolume.getValue() - ((sldVolume.getValue() % 5 == 0) ? 5 : sldVolume.getValue() % 5));
        }

    }

    public void setRootScene(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
    }

    public void btnAboutOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane root = FXMLLoader.load(getClass().getResource("/view/AboutScene.fxml"));
        Scene aboutScene = new Scene(root);

        Stage aboutStage = new Stage();
        aboutStage.setScene(aboutScene);

        aboutStage.initModality(Modality.APPLICATION_MODAL);
        aboutStage.initStyle(StageStyle.TRANSPARENT);
        root.setBackground(Background.fill(Color.TRANSPARENT));
        aboutScene.setFill(Color.TRANSPARENT);

        aboutStage.centerOnScreen();
        aboutStage.setResizable(false);
        aboutStage.show();
    }

    public void btnAboutVideoOnAction(ActionEvent actionEvent) {
        btnAbout.fire();
    }

    public void btnAddVideoOnAction(ActionEvent actionEvent) {
        btnAdd.fire();
    }

    public void btnPlaylistVideoOnAction(ActionEvent actionEvent) {
        btnPlaylist.fire();
    }

    public void btnBackVideoOnAction(ActionEvent actionEvent) {
        btnBack.fire();
    }

    public void btnPlayVideoOnAction(ActionEvent actionEvent) {
        btnPlay.fire();
        btnPlayVideo.setVisible(false);
        btnPauseVideo.setVisible(true);
    }

    public void btnForwardVideoOnAction(ActionEvent actionEvent) {
        btnForward.fire();
    }

    public void btnPauseVideoOnAction(ActionEvent actionEvent) {
        btnPause.fire();
        btnPlayVideo.setVisible(true);
        btnPauseVideo.setVisible(false);
    }
}
