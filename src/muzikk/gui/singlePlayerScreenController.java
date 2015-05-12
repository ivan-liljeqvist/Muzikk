package muzikk.gui;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import muzikk.MuzikkGlobalInfo;
import muzikk.Player;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Created by filip on 2015-05-07.
 */
public class singlePlayerScreenController implements Initializable {
    private Stage prevStage;
    private String key;
    private ArrayList<PlaylistSimple> playLists = new ArrayList<>();
    private ObservableList<String> observablePlayLists = FXCollections.observableArrayList();
    private ObservableMap<String, String> observableMapGenres = FXCollections.observableHashMap();

    @FXML
    private Button startGameButton;
    @FXML
    private ChoiceBox<Integer> numberOfQuestionsChoiceBox;
    @FXML
    private Label playListLabel;
    @FXML
    private ToggleButton setKeyToggleButton;
    @FXML
    private TextField nameTextField = new TextField();
    @FXML
    private TextField keyTextField = new TextField();
    @FXML
    private ListView<String> playListListView;

    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        numberOfQuestionsChoiceBox.getItems().addAll(5, 10, 15, 20);
        numberOfQuestionsChoiceBox.getSelectionModel().select(0);
        playListListView.setItems(observablePlayLists);
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
        MuzikkGlobalInfo.setNumberOfQuestions(numberOfQuestionsChoiceBox.getSelectionModel().getSelectedItem().intValue());

        if (MuzikkGlobalInfo.isLoggedIn()){ //Set chosen playlist if the user is logged in
            MuzikkGlobalInfo.setChosenPlaylist(playLists.get(playListListView.focusModelProperty().get().getFocusedIndex()));
        }
        else
        {

        }

        gameController controller = SceneLoader.gameLoader.getController(); //create the game controller
        controller.initData(player); //Initializes scene data
        controller.setPrevStage(prevStage);
        prevStage.setScene(new Scene(SceneLoader.gamePane));

    }
    /**
     * Initializes data for the this scene. Gets playlists from SpotifyAPI.
     */
    public void initData() {
        if (MuzikkGlobalInfo.isLoggedIn()) {

            for (PlaylistSimple pl : MuzikkGlobalInfo.SpotifyAPI.getAllPlaylists()) {
                playLists.add(pl);
                observablePlayLists.add(pl.name);
            }
            playListListView.getSelectionModel().select(0);
        }
        else{
            observableMapGenres.put("Rock", "2Qi8yAzfj1KavAhWz1gaem");
            observableMapGenres.put("Pop", "5FJXhjdILmRA2z5bvz4nzf");
            observableMapGenres.put("Hiphop", "5yolys8XG4q7YfjYGl5Lff");
            observableMapGenres.put("Metal", "2k2AuaynH7E2v8mwvhpeAO");
            observableMapGenres.put("Indie", "4wtLaWQcPct5tlAWTxqjMD");
            observableMapGenres.put("Soul", "0UUovM2yGwRThZSy9BvADQ");
            observableMapGenres.put("R&B", "36scvoM0cRA50MCZGhv3wo");
            observableMapGenres.put("Reggae", "0ifGUu1vx6PVcCASyG3t8m");
            observableMapGenres.put("Country", "4ecQaDJHF55Ls9m2lKIXbI");
            observableMapGenres.put("Jazz", "5O2ERf8kAYARVVdfCKZ9G7");
            observableMapGenres.put("Blues", "5TkTomPbQuSNDxdlWg2fCx");
            observableMapGenres.put("Punk", "5TuWj7WbayVcr6KbwJ5sBQ");
            observableMapGenres.put("00-tal", "2f6tXtN0XesjONxicAzMIw");
            observableMapGenres.put("90-tal", "3C64V048fGyQfCjmu9TIGA");
            observableMapGenres.put("80-tal", "1TkCnVCBt7HzhGaNzPj2Tg");
            observableMapGenres.put("70-tal", "5KmBulox9POMt9hOt3VV1x");
            observableMapGenres.put("60-tal", "5n6Qo8WNYc5oVBmGbO2iYG");
            observableMapGenres.put("50-tal", "7xADHS7Ryc6oMdqBVhNVQ9");
            playListListView.getItems().addAll(observableMapGenres.keySet());
            playListListView.getSelectionModel().select(0);
            playListLabel.setText("Choose genre");
        }
    }

}
