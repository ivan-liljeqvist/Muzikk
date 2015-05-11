package muzikk;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import javafx.scene.control.ListView;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Created by Filip on 2015-05-10.
 */
public class multiPlayerScreenController implements Initializable {

    Stage prevStage;
    String key;
    ObservableList<String> nameList = FXCollections.observableArrayList();
    ObservableList<String> keyList = FXCollections.observableArrayList();
    ArrayList<Player> playerList = new ArrayList<Player>(10);

    @FXML
    private Button startGameButton;
    @FXML
    private Button addPlayerButton;
    @FXML
    private ToggleButton setKeyToggleButton;
    @FXML
    private TextField nameTextField = new TextField();
    @FXML
    private TextField keyTextField = new TextField();
    @FXML
    private ListView<String> playerListView;
    @FXML
    private ListView<String> keyListView;


    public void setPrevStage(Stage stage) {
        this.prevStage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playerListView.setItems(nameList);
        keyListView.setItems(keyList);
        addPlayerButton.disableProperty().bind( //Binding for disabling button when nameTextField is empty and key not set
                Bindings.isEmpty(nameTextField.textProperty()).or(Bindings.isEmpty(keyTextField.textProperty()))
        );
        addPlayerButton.setOnAction(event1 -> addPlayer());
        startGameButton.setOnAction((event) -> goTogame()); //start the game
        setKeyToggleButton.setOnKeyTyped(event -> { //Sets the players button

            if (setKeyToggleButton.isSelected() && !(keyList.contains(event.getCharacter().toUpperCase()))) {
                setKeyToggleButton.setStyle("-fx-border-color: none");
                key = event.getCharacter().toLowerCase();
                setKeyToggleButton.setText("Key: " + key.toUpperCase());
                keyTextField.setText(String.valueOf(key));
                setKeyToggleButton.fire();
            }
            else if(keyList.contains(event.getCharacter().toUpperCase())){
                setKeyToggleButton.setText("Key not availible");
                setKeyToggleButton.setStyle("-fx-border-color: red");
            }
        });
    }

    /**
     * Starts the game
     */
    private void goTogame() {
        gameController controller = SceneLoader.gameLoader.getController();
        controller.initData(playerList);
        controller.setPrevStage(prevStage);
        prevStage.setScene(new Scene(SceneLoader.gamePane));
    }

    /**
     * Will create and add a new player to the game
     */
    private void addPlayer() {
        playerList.add(new Player(nameTextField.getText(), key));
        keyList.add(key.toUpperCase());
        nameList.add(nameTextField.getText());
        resetForm();
    }

    /**
     * Will reset the add player form
     */
    private void resetForm(){
        nameTextField.setText("");
        setKeyToggleButton.setText("Press to set key");
        key = null;
    }

}
