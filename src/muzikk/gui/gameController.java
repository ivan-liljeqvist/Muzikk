package muzikk.gui;

import jaco.mp3.player.MP3Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;
import muzikk.backend.ThreadCompleteListener;
import muzikk.Player;
import muzikk.MuzikkGlobalInfo;
import muzikk.backend.NotifyingThread;

import javax.swing.text.html.ImageView;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Authors Filip Martinsson and Ivan Liljeqvist.
 * Version 10-05-2015
 *
 *
 * This is a controller represeting the in-game window.
 *
 */
public class gameController implements Initializable, ThreadCompleteListener {
    //the parent stage
    private Stage prevStage;

    private ArrayList<Player> players;
    private ArrayList<String> genres = new ArrayList<String>(10);
    private ArrayList<String> songURLs = new ArrayList<String>(10);

    private List<Track> tracksToPlayWith;

    private Random randomGenerator;

    private int numSongs;

    @FXML
    private ImageView artistImage0;
    @FXML
    private ImageView artistImage1;
    @FXML
    private ImageView artistImage2;
    @FXML
    private ImageView artistImage3;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Label label = new Label("hej");
        label.setLayoutX(100);
        label.setLayoutY(200);

        randomGenerator=new Random();
    }


    /**
     * Initializes the game and imports the players from the multiplayer setup.
     * @param playerList List of players
     */
    public void initData(ArrayList<Player> playerList){
        this.players = playerList;

        for (int i = 0;i<players.size();i++){
            Label label = new Label(players.get(i).getName());
            label.setLayoutX(100+50*i);
            label.setLayoutY(200);
        }

        this.onShowWindow();
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

        this.onShowWindow();

    }

    @Override
    public void notifyOfThreadComplete(final NotifyingThread thread){

        if(thread.getName().equals("getAllTracksThread")){
            //all tracks received
            System.out.println("All trakcs received");
            System.out.println(thread.extractParams().size()+" songs extracted");

            this.tracksToPlayWith=thread.extractParams();

            this.startNewQuestion();
        }
        else{

        }
    }

    private void onShowWindow(){

        List<PlaylistSimple> playlists= MuzikkGlobalInfo.SpotifyAPI.getAllPlaylists();

        NotifyingThread<Track> getAllTracksThread=MuzikkGlobalInfo.SpotifyAPI.getAllTracksFromPlaylists(playlists);
        getAllTracksThread.addListener(this);


    }

    public void startNewQuestion(){

        int index = randomGenerator.nextInt(tracksToPlayWith.size());
        Track track = tracksToPlayWith.get(index);

        System.out.println("START NEW QUESTION");
        MuzikkGlobalInfo.SpotifyAPI.playTrack(track);

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

}
