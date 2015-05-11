package muzikk;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.EventHandler;
import java.io.IOException;
import java.net.URL;
import java.util.EventListener;
import java.util.ResourceBundle;

/**
 * Created by filip on 2015-05-07.
 */
public class singlePlayerScreenController implements Initializable {
    Stage prevStage;
    String key;

    @FXML
    private Button startGameButton;
    private StackPane stackpane;
    @FXML
    private ToggleButton setKeyToggleButton;
    @FXML
    private TextField nameTextField = new TextField();
    @FXML
    private TextField keyTextField = new TextField();

    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startGameButton.disableProperty().bind( //Binding for disabling button when nameTextField is empty
                Bindings.isEmpty(nameTextField.textProperty()).or(Bindings.isEmpty(keyTextField.textProperty()))
        );
        startGameButton.setOnAction((event) -> goTogame()); //start the game
        setKeyToggleButton.setOnKeyTyped(event -> { //Sets the players button
            if (setKeyToggleButton.isSelected()) {
                key = event.getCharacter().toLowerCase();
                setKeyToggleButton.setText("Key: " + key.toUpperCase());
                keyTextField.setText(String.valueOf(key));
                setKeyToggleButton.fire();
            }
        });
    }
    public void goTogame(){
        Player player = new Player(nameTextField.getText(), key);
        gameController controller = SceneLoader.gameLoader.getController();
        controller.initData(player);
        controller.setPrevStage(prevStage);
        prevStage.setScene(new Scene(SceneLoader.gamePane));

    }

}
