package muzikk.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import muzikk.backend.MuzikkAccessFetcher;
import muzikk.MuzikkGlobalInfo;
import muzikk.backend.NotifyingThread;
import muzikk.backend.ThreadCompleteListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by filip on 2015-05-11.
 */
public class LoginScreenController implements Initializable, ThreadCompleteListener<String> {
    Stage prevStage;
    @FXML
    private ImageView spotifyLoginImageView;
    @FXML
    private Button noLoginButton;
    @FXML
    private Label titleLabel;

    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        spotifyLoginImageView.setOnMouseClicked((event) -> loginSpotify()); //start the game
        noLoginButton.setOnAction(event -> goToModeSelection());

        //Font.loadFont(LoginScreenController.class.getResource("../gui/HACKED:TTF").toExternalForm(), 80);
        //titleLabel.setStyle("-fx-font-family: HACKED");

    }
    @Override
    public void notifyOfThreadComplete(final NotifyingThread<String> thread){
        //extract the results from the thread
        String user_id=thread.extractParams().get(0);
        String user_email=thread.extractParams().get(1);
        MuzikkGlobalInfo.setUserId(user_id);
        MuzikkGlobalInfo.setUserEmail(user_email);
        //when the user has successfully logged in in the browser
        //we want to focus the Muzikk window, but we can't do it
        //on this secondary thread. All UI operations have to run on Main thread.
        //therefore we call runLater and request focus for the window inside a Runnable.
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                prevStage.requestFocus();
                MuzikkGlobalInfo.setLoggedIn(true);
                goToModeSelection();
            }
        });

    }

    private void goToModeSelection(){
        ModeSelectionController controller = SceneLoader.modeSelectionLoader.getController();
        controller.setPrevStage(prevStage);
        prevStage.hide();
        prevStage.setScene(new Scene(SceneLoader.modeSelectionPane));
        controller.initData();
        prevStage.show();

    }
    private void loginSpotify(){
        MuzikkAccessFetcher.initiateLogin();
        NotifyingThread thread = MuzikkAccessFetcher.keepPingingServerUntilUserLoggedIn();
        thread.addListener(this);
    }
    public void initData(){
        prevStage.setMinWidth(380);
        prevStage.setMinHeight(496);

    }

}
