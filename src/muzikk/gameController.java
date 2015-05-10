package muzikk;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by filip on 2015-05-07.
 */
public class gameController implements Initializable{
    private Stage prevStage;
    private ArrayList<Player> players;
    private ArrayList<String> genres = new ArrayList<String>(10);
    private ArrayList<String> songURLs = new ArrayList<String>(10);

    private int numSongs;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Label label = new Label("hej");
        label.setLayoutX(100);
        label.setLayoutY(200);
    }

    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }

    public void setPlayers(ArrayList<Player> players){
        this.players = players;
    }

    public ArrayList<String> getGenres(){
        return genres;
    }

    public void setGenres(ArrayList<String> genres){

        this.genres = genres;
    }
    public ArrayList<String> getSongURLs(){

        return songURLs;
    }

    public void setSongURLs(ArrayList<String> songURLs){

        this.songURLs = songURLs;
    }

    public int getNumSongs(){

        return numSongs;
    }

    public void setNumSongs(int numSongs){

        this.numSongs = numSongs;
    }

    /**
     * Initializes the game and imports the players from the multiplayer setup.
     * @param playerList List of players
     */
    public void initData(ArrayList<Player> playerList){
        players = new ArrayList<Player>(playerList);
        for (int i = 0;i<players.size();i++){
            Label label = new Label(players.get(i).getName());
            label.setLayoutX(100+50*i);
            label.setLayoutY(200);
        }
    }

    /**
     * Initializes the game and import the player from the singleplayer setup
     * @param player The player
     */
    public void initData(Player player){
        players = new ArrayList<Player>(1);
        players.add(player);
        for (Player p : players){
            System.out.println(p.getName());
        }
    }

}
