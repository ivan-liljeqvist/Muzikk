package muzikk.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTracksInformation;
import muzikk.MuzikkGlobalInfo;
import muzikk.Player;
import muzikk.backend.NotifyingThread;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Filip on 2015-05-12.
 */
public class gameOverScreen implements Initializable {

    private Stage prevStage;
    private ObservableList<String> nameList = FXCollections.observableArrayList();
    private ObservableList<Integer> scoreList = FXCollections.observableArrayList();
    private ArrayList<Player> playerList = new ArrayList<Player>(10);
    @FXML
    private ListView<String> highScoreNamesListView;
    @FXML
    private ListView<Integer> highScorePointsListView;
    @FXML
    private Button backToMainMenuButton;
    @FXML
    private Button playAgainButton;
    @FXML
    private Button quitButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        highScoreNamesListView.setItems(nameList);
        highScorePointsListView.setItems(scoreList);
        backToMainMenuButton.setOnAction(event -> backtoModeSelection());
        playAgainButton.setOnAction(event -> goTogame());
        quitButton.setOnAction(event -> quit());

    }

    /**
     * Will initialize the players once again if there are multiple players
     * @param players list of players
     */
    public void initData(List<Player> players){
        nameList = FXCollections.observableArrayList();
        scoreList = FXCollections.observableArrayList();
        playerList = new ArrayList<Player>(10);

        for (Player p :players){
            playerList.add(p);
            scoreList.add(p.getScore());

            nameList.add(p.getName());
        }

        highScoreNamesListView.setItems(nameList);
        highScorePointsListView.setItems(scoreList);
    }

    /**
     * Will initialize the player once again if there are just one player
     * @param player the player
     */
    public void initData(Player player){
        nameList = FXCollections.observableArrayList();
        scoreList = FXCollections.observableArrayList();
        playerList = new ArrayList<Player>(10);

        playerList.add(player);
        scoreList.add(player.getScore());

        nameList.add(player.getName());

        highScoreNamesListView.setItems(nameList);
        highScorePointsListView.setItems(scoreList);
    }

    /**
     * Takes the user back to the mode selection
     */
    public void backtoModeSelection(){

        MuzikkGlobalInfo.globalStage.setScene(SceneLoader.modeSelectionScene);

    }

    /**
     * Restarts the game, same players, same keys, same playlist.
     */
    public void goTogame(){

        MuzikkGlobalInfo.shouldResetPlayerTable=true;

        if (MuzikkGlobalInfo.getIngoMode()){
            gameController c=SceneLoader.ingoGameLoader.getController();
            c.startNewQuestion();


            MuzikkGlobalInfo.globalStage.setScene(SceneLoader.ingoScene);
        }
        else{
            gameController c=SceneLoader.gameLoader.getController();
            c.startNewQuestion();

            MuzikkGlobalInfo.globalStage.setScene(SceneLoader.gameScene);
        }

    }

    /**
     * Exits the game
     */
    private void quit(){
        prevStage.setOnCloseRequest(e -> Platform.exit());
    }

    /**
     * Sets previous stage.
     * @param stage - the stage to set as previous.
     */
    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

}
