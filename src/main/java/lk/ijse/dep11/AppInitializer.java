package lk.ijse.dep11;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

//        AnchorPane root = FXMLLoader.load(getClass().getResource("/view/MainScene.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScene.fxml"));
        AnchorPane root = loader.load();
        Scene mainScene = new Scene(root);

        MainSceneController controller = loader.getController();
        controller.setRootScene(mainScene);

        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Lumix");
        primaryStage.setResizable(false);

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        root.setBackground(Background.fill(Color.TRANSPARENT));
        mainScene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
