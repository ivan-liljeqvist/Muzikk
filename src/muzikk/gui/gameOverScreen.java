package muzikk.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import muzikk.MuzikkGlobalInfo;
import muzikk.Player;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Filip on 2015-05-12.
 */
public class gameOverScreen implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        for (Player p : MuzikkGlobalInfo.getPlayers()){
            nameList.add(p.getName());
            scoreList.add(p.getScore());
        }
        highScoreNamesListView.setItems(nameList);
        highScorePointsListView.setItems(scoreList);
        //backToMainMenuButton.setOnAction(event -> backtoMainMenu());
        //playAgainButton.setOnAction(event -> playAgain());
    }
}
