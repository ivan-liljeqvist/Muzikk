package muzikk;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOError;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by filip on 2015-05-07.
 */
public class firstScreenController implements Initializable {
    private Stage prevStage;
    private FXMLLoader myLoader;
    private Pane myPane;
    @FXML private Button singlePlayerButton;

    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        singlePlayerButton.setOnAction((event) -> goToSinglePlayer());

    }
    public void goToSinglePlayer(){
        /*Pane myPane = null;
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("singlePlayerScreen.fxml"));
        try {
            myPane = myLoader.load();
        }catch (IOException e) {
            System.err.println("FXML file missing");
        }*/
        singlePlayerScreenController controller = SceneLoader.spLoader.getController();
        controller.setPrevStage(prevStage);
        prevStage.setScene(new Scene(SceneLoader.spPane));

    }
}
