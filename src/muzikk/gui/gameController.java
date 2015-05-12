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

    private ImageView correctImageView;

    @FXML
    private ImageView artistImage0;
    @FXML
    private ImageView artistImage1;
    @FXML
    private ImageView artistImage2;
    @FXML
    private ImageView artistImage3;

    ArtistImageView aiv0;
    ArtistImageView aiv1;
    ArtistImageView aiv2;
    ArtistImageView aiv3;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Label label = new Label("hej");
        label.setLayoutX(100);
        label.setLayoutY(200);

        randomGenerator=new Random();
        currentlyPlayingTrack=new Track();

        correctImageView=null;

        artistImage0.setOnMouseClicked((event) -> artistViewClicked(artistImage0));
        artistImage1.setOnMouseClicked((event) -> artistViewClicked(artistImage1));
        artistImage2.setOnMouseClicked((event) -> artistViewClicked(artistImage2));
        artistImage3.setOnMouseClicked((event) -> artistViewClicked(artistImage3));

    }

    private void artistViewClicked(ImageView iv){

        ArtistImageView clickedOn=null;
        /*
            get ArtistImageView for this ImageView.
         */
        if(aiv0.getIv()==iv){
            clickedOn=aiv0;
        }
        else if(aiv1.getIv()==iv){
            clickedOn=aiv1;
        }
        else if(aiv2.getIv()==iv){
            clickedOn=aiv2;
        }
        else if(aiv3.getIv()==iv){
            clickedOn=aiv3;
        }

        if(aiv0.getArtistId().equals(clickedOn.getArtistId())){
            System.out.println("CORRECT!!");
            System.out.println("AIV0 id "+aiv0.getArtistId()+"   artist id: "+clickedOn.getArtistId());
        }else{
            System.out.println("WRONG");
        }
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

        List<PlaylistSimple> playlists= new ArrayList<>();
        playlists.add(MuzikkGlobalInfo.getChosenPlaylist());

        System.out.println(playlists.get(0).name);

        NotifyingThread<Track> getAllTracksThread=MuzikkGlobalInfo.SpotifyAPI.getAllTracksFromPlaylists(playlists);
        getAllTracksThread.addListener(this);


    }

    public void startNewQuestion(){
        System.out.println("SIZE: "+tracksToPlayWith.size());
        int rand = randomGenerator.nextInt(tracksToPlayWith.size());
        this.currentlyPlayingTrack = tracksToPlayWith.get(rand);

        System.out.println("START NEW QUESTION");

        System.out.println("URL : "+currentlyPlayingTrack.preview_url);

        MuzikkGlobalInfo.SpotifyAPI.playTrack(currentlyPlayingTrack);
        this.startPopulatingArtistImages();

    }

    private void startPopulatingArtistImages(){



        ArtistSimple rightArtist=this.currentlyPlayingTrack.artists.get(0);
        System.out.println("Right artist:  "+rightArtist.name);

        randomGenerator=new Random(System.nanoTime());

        int rand = randomGenerator.nextInt(tracksToPlayWith.size());
        ArtistSimple wrongArtist1=tracksToPlayWith.get(rand).artists.get(0);

        System.out.println("wrong artist1:  "+wrongArtist1.name);


        rand = randomGenerator.nextInt(tracksToPlayWith.size());
        ArtistSimple wrongArtist2=tracksToPlayWith.get(rand).artists.get(0);

        System.out.println("wrong artist2:  "+wrongArtist2.name);


        rand = randomGenerator.nextInt(tracksToPlayWith.size());
        ArtistSimple wrongArtist3=tracksToPlayWith.get(rand).artists.get(0);

        System.out.println("wrong artist3:  "+wrongArtist3.name+" list size: "+tracksToPlayWith.size());


        List<ImageView> imageViews=new ArrayList<ImageView>();
        imageViews.add(artistImage0);
        imageViews.add(artistImage1);
        imageViews.add(artistImage2);
        imageViews.add(artistImage3);

        Collections.shuffle(imageViews);

        populateArtistImage(rightArtist, imageViews.get(0));
        aiv0=new ArtistImageView(rightArtist.id,imageViews.get(0));

        this.correctImageView=imageViews.get(0);

        populateArtistImage(wrongArtist1,imageViews.get(1));
        aiv1=new ArtistImageView(wrongArtist1.id,imageViews.get(1));
        populateArtistImage(wrongArtist2,imageViews.get(2));
        aiv2=new ArtistImageView(wrongArtist2.id,imageViews.get(2));
        populateArtistImage(wrongArtist3,imageViews.get(3));
        aiv3=new ArtistImageView(wrongArtist3.id,imageViews.get(3));

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
                    boolean succeeded = false;

                    @Override
                    public void success(Artist artistReturned, Response response) {
                        System.out.println("Fetched artist!");

                        synchronized (artistWaiter) {
                            artistWaiter.notify();
                            succeeded = true;

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

                        synchronized (artistWaiter) {
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


}
