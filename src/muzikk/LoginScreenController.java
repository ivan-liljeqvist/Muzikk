package muzikk;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by filip on 2015-05-11.
 */
public class LoginScreenController implements Initializable {
    Stage prevStage;
    @FXML
    private Button spotifyLoginButton;
    @FXML
    private Button noLoginButton;
    @FXML
    private ImageView i;
    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //spotifyLoginButton.setOnAction((event) -> goTogame()); //start the game
        noLoginButton.setOnAction(event -> goToModeSelection());
        i.setOnMouseClicked(event -> goToModeSelection());
    }

    private void goToModeSelection(){
        ModeSelectionController controller = SceneLoader.modeSelectionLoader.getController();
        controller.setPrevStage(prevStage);
        prevStage.setScene(new Scene(SceneLoader.modeSelectionPane));
    }

}
