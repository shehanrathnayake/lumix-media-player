package lk.ijse.dep11;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AboutSceneController {
    public Button btnClose;
    public AnchorPane root;

    double xPos;
    double yPos;

    public void btnCloseOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    public void rootOnMouseDragged(MouseEvent e) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setX(e.getScreenX() - xPos);
        stage.setY(e.getScreenY() - yPos);
    }

    public void rootOnMousePressed(MouseEvent e) {
        xPos = e.getX();
        yPos = e.getY();
    }
}
