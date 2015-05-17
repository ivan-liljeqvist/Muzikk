package muzikk.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
        singlePlayerScreenController controller = SceneLoader.spLoader.getController();
        controller.setPrevStage(prevStage);
        prevStage.hide();
        prevStage.setScene(new Scene(SceneLoader.spPane));
        controller.initData();
        prevStage.show();


    }
    private void goToMultiPlayer(){
        multiPlayerScreenController controller = SceneLoader.mpLoader.getController();
        controller.setPrevStage(prevStage);
        controller.initData();
        prevStage.setScene(new Scene(SceneLoader.mpPane));
    }
    public void initData(){
        prevStage.setMinHeight(496);
        prevStage.setMinWidth(841);
        prevStage.setFullScreen(true);

    }

}
