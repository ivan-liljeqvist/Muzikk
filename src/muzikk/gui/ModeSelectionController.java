package muzikk.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import muzikk.MuzikkGlobalInfo;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by filip on 2015-05-07.
 */
public class ModeSelectionController implements Initializable {
    private Stage prevStage;
    private FXMLLoader myLoader;
    private Pane myPane;
    @FXML
    private Button singlePlayerButton;
    @FXML
    private Button multiPlayerButton;

    public void setPrevStage(Stage stage){
        this.prevStage = stage;

    }


    @Override
    public void initialize(URL url, ResourceBundle rb){
        singlePlayerButton.setOnAction((event) -> goToSinglePlayer());

        multiPlayerButton.setOnAction((event) -> goToMultiPlayer());

    }
    private void goToSinglePlayer(){
        singlePlayerScreenController spC=SceneLoader.spLoader.getController();
        spC.initData();
        spC.resetForms();
        MuzikkGlobalInfo.globalStage.setScene(SceneLoader.spScene);


    }
    private void goToMultiPlayer(){
        multiPlayerScreenController mpC=SceneLoader.mpLoader.getController();
        mpC.initData();
        mpC.resetForms();
        MuzikkGlobalInfo.globalStage.setScene(SceneLoader.mpScene);
    }


}
