package muzikk;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by filip on 2015-05-07.
 */
public class singlePlayerScreenController implements Initializable {
    Stage prevStage;
    @FXML
    private Button startGameButton;

    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        startGameButton.setOnAction((event) -> goTogame());

    }
    public void goTogame(){
        System.out.println("GAME");
    }


}
