package muzikk;

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
    KeyCode key;
    @FXML
    private Button startGameButton;
    @FXML
    private ToggleButton setKeyToggleButton;
    @FXML
    private TextField nameTextField;

    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startGameButton.setOnAction((event) -> goTogame()); //start the game
        setKeyToggleButton.setOnKeyPressed(event -> { //Sets the players button
            if (setKeyToggleButton.isSelected()) {
                key = event.getCode();
                setKeyToggleButton.setText(String.valueOf(key));
                setKeyToggleButton.fire();
                if (nameTextField.getText().length() > 0)
                    startGameButton.setDisable(false);
            }
        });
    }
    public void goTogame(){
        if (key != null && nameTextField.getText().length() > 0){
            Player player = new Player(nameTextField.getText(), key);
            //starta spelet
        }


    }
    @FXML
    private void setKey(){




    }

}
