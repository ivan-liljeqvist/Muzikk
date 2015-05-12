package muzikk.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;
import muzikk.backend.ThreadCompleteListener;
import muzikk.Player;
import muzikk.MuzikkGlobalInfo;
import muzikk.backend.NotifyingThread;

import javafx.scene.image.ImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.net.URL;
import java.util.*;

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

    private Track currentlyPlayingTrack;

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
        currentlyPlayingTrack=new Track();

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

            Collections.shuffle(this.tracksToPlayWith, new Random(System.nanoTime()));

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

        int rand = randomGenerator.nextInt(tracksToPlayWith.size());
        this.currentlyPlayingTrack = tracksToPlayWith.get(rand);

        System.out.println("START NEW QUESTION");
        //MuzikkGlobalInfo.SpotifyAPI.playTrack(track);

        /*Image img = new Image("http://mikecann.co.uk/wp-content/uploads/2009/12/javafx_logo_color_1.jpg");
        artistImage0.setImage(img);*/

        this.startPopulatingArtistImages();

    }

    private void startPopulatingArtistImages(){



        ArtistSimple rightArtist=this.currentlyPlayingTrack.artists.get(0);
        System.out.println("Right artist:  "+rightArtist.name);

        randomGenerator=new Random(System.nanoTime());

        int rand = randomGenerator.nextInt(tracksToPlayWith.size());
        ArtistSimple wrongArtist1=tracksToPlayWith.get(rand).artists.get(0);

        while(wrongArtist1.id==rightArtist.id){
            wrongArtist1=tracksToPlayWith.get(rand).artists.get(0);
        }

        System.out.println("wrong artist1:  "+wrongArtist1.name);


        rand = randomGenerator.nextInt(tracksToPlayWith.size());
        ArtistSimple wrongArtist2=tracksToPlayWith.get(rand).artists.get(0);

        while(wrongArtist2.id==rightArtist.id || wrongArtist2.id==wrongArtist1.id){
            wrongArtist2=tracksToPlayWith.get(rand).artists.get(0);
        }

        System.out.println("wrong artist2:  "+wrongArtist2.name);


        rand = randomGenerator.nextInt(tracksToPlayWith.size());
        ArtistSimple wrongArtist3=tracksToPlayWith.get(rand).artists.get(0);

        while(wrongArtist3.id==rightArtist.id || wrongArtist3.id==wrongArtist1.id || wrongArtist3.id==wrongArtist2.id){
            wrongArtist3=tracksToPlayWith.get(rand).artists.get(0);
        }

        System.out.println("wrong artist3:  "+wrongArtist3.name+" list size: "+tracksToPlayWith.size());




        populateArtistImage(rightArtist,artistImage0);
        populateArtistImage(wrongArtist1,artistImage1);
        populateArtistImage(wrongArtist2,artistImage2);
        populateArtistImage(wrongArtist3,artistImage3);

    }



    private void populateArtistImage(ArtistSimple artist, ImageView imgView){


        NotifyingThread<String> thread=new NotifyingThread<String>() {

            @Override
            public List<String> extractParams() {
                return null;
            }

            @Override
            public void doRun() {
                Object artistWaiter=new Object();


                MuzikkGlobalInfo.SpotifyAPI.getService().getArtist(artist.id, new Callback<Artist>() {
                    boolean succeeded=false;

                    @Override
                    public void success(Artist artistReturned, Response response) {
                        System.out.println("Fetched artist!");

                        synchronized (artistWaiter){
                            artistWaiter.notify();
                            succeeded=true;

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    imgView.setImage(new Image(artistReturned.images.get(0).url));
                                    System.out.println("placing artist image in ImageView");
                                }
                            });
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        System.out.println("Couldn't get artist.");

                        synchronized (artistWaiter){
                            artistWaiter.notify();
                        }
                    }
                });

                synchronized (artistWaiter){
                    try{
                        artistWaiter.wait();
                    }catch(Exception e){
                        System.out.println("Error while waiting for artist.");
                    }

                }
            }
        };

        thread.start();



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
