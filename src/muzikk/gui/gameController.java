package muzikk.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
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


    /**
     * List with tracks from the playlists that is chosen for this game.
     */
    private List<Track> tracksToPlayWith;

    private Random randomGenerator;

    /**
     * Track that is randomly chosen from tracksToPlayWith
     */
    private Track currentlyPlayingTrack;

    private Timer countdown_timer;

    /**
     * Controllers defined in the fxml file.
     */

    @FXML
    private ImageView artistImage0;
    @FXML
    private ImageView artistImage1;
    @FXML
    private ImageView artistImage2;
    @FXML
    private ImageView artistImage3;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label artistLabel0;
    @FXML
    private Label artistLabel1;
    @FXML
    private Label artistLabel2;
    @FXML
    private Label artistLabel3;
    @FXML
    private ListView nameListView;
    @FXML
    private ListView scoreListView;
    @FXML
    private Pane pane;

    /**
     * Artist view containers containing the ImageView and artist Id.
     * Used to decide if the ImageView clicked is the correct one.
     */
    private ArtistImageView aiv0;
    private ArtistImageView aiv1;
    private ArtistImageView aiv2;
    private ArtistImageView aiv3;

    private List<ImageView> imageViews;

    /**
     * Lists for populating the table with the scores and the players.
     */

    private ObservableList<String> playerObsList= FXCollections.observableArrayList();
    private ObservableList<Integer> scoreObsList= FXCollections.observableArrayList();

    private boolean PLAYER_ANSWERING =false;

    /**
     * The player that has pressed his actionbutton and is answerting.
     */
    private Player answeringPlayer;

    private List<Player> playersInGame;

    /**
     * Initialize the controller.
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Label label = new Label("hej");
        label.setLayoutX(100);
        label.setLayoutY(200);

        randomGenerator=new Random();
        currentlyPlayingTrack=new Track();


        /*
            Connect the image views with click listeners
         */
        artistImage0.setOnMouseClicked((event) -> artistViewClicked(artistImage0));
        artistImage1.setOnMouseClicked((event) -> artistViewClicked(artistImage1));
        artistImage2.setOnMouseClicked((event) -> artistViewClicked(artistImage2));
        artistImage3.setOnMouseClicked((event) -> artistViewClicked(artistImage3));

        countdown_timer=new Timer();

        nameListView.setItems(playerObsList);
        scoreListView.setItems(scoreObsList);

        answeringPlayer=null;

        /*
            Connect the pane with the keyboard listener.
         */
        pane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {

                /*
                    Check if the button pressed matches any action button
                 */
                for (Player p : playersInGame) {
                    if (ke.getCode().toString().toUpperCase().equals(p.getActionButton().toUpperCase())) {
                        System.out.println(p.getName()+" PRESSEDAD!!");
                        //action button detected!
                        PLAYER_ANSWERING =true;
                        answeringPlayer=p;
                        //reset progress bar to 1 - it will quickly go down from one.
                        progressBar.setProgress(1.0);
                    }
                }
            }
        });



    }


    /**
     * Initializes the game and imports the players from the multiplayer setup.
     * @param playerList List of players
     */
    public void initData(ArrayList<Player> playerList){
        this.onShowWindow();

        this.playersInGame=playerList;

        /*
            Populate the UI table with players names and scores.
         */
        for(Player p:playersInGame){
            playerObsList.add(p.getName());
            scoreObsList.add(p.getScore());
        }


    }

    /**
     * Initializes the game and import the player from the singleplayer setup
     * @param player The player
     */
    public void initData(Player player){

        this.onShowWindow();
        this.playersInGame=new ArrayList<>();
        playersInGame.add(player);

        /*
            Populate the UI list with name and score.
         */
        playerObsList.add(player.getName());
        scoreObsList.add(player.getScore());

    }

    /**
     * Thread listener.
     * When a Notifying thread that has this class as listener
     * has finished running this method will be called.
     *
     * @param thread - the thread that has finished running.
     */

    @Override
    public void notifyOfThreadComplete(final NotifyingThread thread){

        /*
            A thread fetching the tracks has finished.
         */
        if(thread.getName().equals("getAllTracksThread")){
            //print out some info
            System.out.println("All trakcs received");
            System.out.println(thread.extractParams().size()+" songs extracted");

            //save extracted tracks.
            this.tracksToPlayWith=thread.extractParams();

            //shuffle tracks
            Collections.shuffle(this.tracksToPlayWith, new Random(System.nanoTime()));

            //start new question
            this.startNewQuestion();
        }
        /*
            Some other thread has finished.
         */
        else{
            //other threads that just finished
        }
    }

    /**
        Resets values from the last question and starts a new question.
     */

    public void startNewQuestion(){

        /*
            Reset values.
         */
        PLAYER_ANSWERING =false;
        answeringPlayer=null;

        System.out.println("SIZE: "+tracksToPlayWith.size());

        int rand=0;

        URL url=null;

        /*
            Start making new question
         */

        /*
            Choose a song. Sometimes the URL will be invalid.
            Don't proceed if the URL is invalid, choose another song.
         */
        while(url==null){
            rand = randomGenerator.nextInt(tracksToPlayWith.size());
            this.currentlyPlayingTrack = tracksToPlayWith.get(rand);
            try{
                url=new URL(currentlyPlayingTrack.preview_url);
            }catch(Exception e){
                e.printStackTrace();
            }

        }

        System.out.println("START NEW QUESTION");
        System.out.println("URL : "+currentlyPlayingTrack.preview_url);

        /*
            Start playing the track and showing the artist images.
         */
        MuzikkGlobalInfo.SpotifyAPI.playTrack(currentlyPlayingTrack);
        this.startPopulatingArtistImages();

        /*
            Reset progress bar.
         */

        progressBar.setProgress(1.0);




    }

    /**
        Runs when the controller has initalized and received game data.
     */

    private void onShowWindow(){

        /*
           Create a list of playlists used in the game.
           We will only put one playlist - the chosen one.
           It's easy to expand this and make so it's possible to play with many
           playlists in the future.
         */

        List<PlaylistSimple> playlists= new ArrayList<>();
        playlists.add(MuzikkGlobalInfo.getChosenPlaylist());

        System.out.println(playlists.get(0).name);

        /*
            Start getting the tracks for the playlists.
         */
        NotifyingThread<Track> getAllTracksThread=MuzikkGlobalInfo.SpotifyAPI.getAllTracksFromPlaylists(playlists);
        /*
            Set this class as a listener.
            It will be notified when the tracks have been fetched.
         */
        getAllTracksThread.addListener(this);

        /*
            Create and start the thread that will manage the countdown.
            This will update the progress bar at the bottom.
            Used to set a timer before a player has to answer.
         */

        NotifyingThread countdownThread=new NotifyingThread() {
            @Override
            public List extractParams() {
                //this thread doesn't return anything.
                return null;
            }

            @Override
            public void doRun() {
                //create a timer task that will be run each time
                TimerTask task=new TimerTask() {
                    @Override
                    public void run() {

                        /*
                            Go to the UI thread.
                         */

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                /*
                                    If we can still decrease progress bar -  dot.
                                 */
                                if(progressBar.getProgress() - 1.0 / 960>=0){

                                    double remove=1.0 / 600;

                                    //move fast if a player is answering.
                                    if(PLAYER_ANSWERING){
                                        remove*=7;
                                    }
                                    progressBar.setProgress(progressBar.getProgress() - remove);


                                }
                                /*
                                    If the progress bar is too low we can't decrease it.
                                    THE TIME FOR THIS QUESTION HAS RUN OUT!
                                 */
                                else{
                                    progressBar.setProgress(0.0);

                                    if(progressBar.getProgress()<=0){

                                        //if any player has pressed his/her action button - decrease score and update UI table.
                                        if(answeringPlayer!=null){
                                            answeringPlayer.decreaseScore();
                                            System.out.println(answeringPlayer.getScore()+ " SCOREEE");
                                            refreshObservablePlayerLists();
                                        }
                                        //start new question
                                        startNewQuestion();

                                    }
                                }


                            }
                        });



                    }
                };
                //call the task often-
                countdown_timer.scheduleAtFixedRate(task,0,36);
            }
        };


        countdownThread.start();



    }

    /**
     * Decides whether the clicked image view is right or wrong and act accordingly.
     * If right - increase score and start new question.
     * If wrong - decrease score and start new question.
     * @param iv - the ImageView that has been clicked.
     */

    private void artistViewClicked(ImageView iv){

        /*
            Ignore this is no one has pressed an action button.
         */
        if(PLAYER_ANSWERING ==false){
            return;
        }

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

        /*
            Decide whether right or wrong.
         */

        if(aiv0.getArtistId().equals(clickedOn.getArtistId())){
            System.out.println("CORRECT!!");
            System.out.println("AIV0 id "+aiv0.getArtistId()+"   artist id: "+clickedOn.getArtistId());

            //INCREASE SCORE
            answeringPlayer.increaseScore();
            this.startNewQuestion();
            //UPDATE THE UI TABLE
            this.refreshObservablePlayerLists();
        }else{
            System.out.println("WRONG");
            answeringPlayer.decreaseScore();
            this.startNewQuestion();

            //UPDATE UI TABLE
            this.refreshObservablePlayerLists();
        }
    }


     /**
        Refreshes observable lists so that the player and score table refreshes.
        Should run on the UI thread.
     */

    private void refreshObservablePlayerLists(){


        for(int i=0; i<playersInGame.size();i++){
            scoreObsList.set(i,playersInGame.get(i).getScore());
        }
    }


    /**
        Decides which artist is right/wrong.
        Starts threads to populate images.
        Places the artist labels.
     */

    private void startPopulatingArtistImages(){

        /*
            Chose the wrong artists and the right artist.
            Randomly choose wrong artists.
         */

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

        /*
            Add all image views that will contain the artist images in a list
         */

        imageViews=new ArrayList<ImageView>();
        imageViews.add(artistImage0);
        imageViews.add(artistImage1);
        imageViews.add(artistImage2);
        imageViews.add(artistImage3);

        /*
            Shuffle the list. WE want the artists to appear in random order.
         */
        Collections.shuffle(imageViews);

        /*
            Populate the Image Views with the images.
         */
        populateArtistImage(rightArtist, imageViews.get(0));
        aiv0=new ArtistImageView(rightArtist.id,imageViews.get(0));

        this.correctImageView=imageViews.get(0);

        populateArtistImage(wrongArtist1,imageViews.get(1));
        aiv1=new ArtistImageView(wrongArtist1.id,imageViews.get(1));
        populateArtistImage(wrongArtist2,imageViews.get(2));
        aiv2=new ArtistImageView(wrongArtist2.id,imageViews.get(2));
        populateArtistImage(wrongArtist3,imageViews.get(3));
        aiv3=new ArtistImageView(wrongArtist3.id,imageViews.get(3));

        String[] artistNames={rightArtist.name,wrongArtist1.name,wrongArtist2.name,wrongArtist3.name};

        /*
            Place the artist names as labels above the Image Views.
         */
        placeLabels(artistNames);

    }

    /**
     *
     * @param names - the names of the artist that will be placed out as labels.
     */

    private void placeLabels(String[] names){

        /*
            We want to change the labels.
            Do it on the UI thread.
         */

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                for(int i=0;i<=3;i++){
                    if(artistImage0==imageViews.get(i)){
                        artistLabel0.setText(names[i]);
                    }
                    else if(artistImage1==imageViews.get(i)){
                        artistLabel1.setText(names[i]);
                    }
                    else if(artistImage2==imageViews.get(i)){
                        artistLabel2.setText(names[i]);
                    }
                    else if(artistImage3==imageViews.get(i)){
                        artistLabel3.setText(names[i]);
                    }
                }

            }
        });

    }

    /**
     *
     * @param artist - artist Object that the image will represent
     * @param imgView - the view which will contain the image
     */

    private void populateArtistImage(ArtistSimple artist, ImageView imgView){

        /*
            We'll need two thread to accomplish this.
            One thread will fetch the Artist using ArtistSimple.

            If succeed - we start another thread that will down load the image
            using the URL from Artist.

            We then go to UI thread and update the image.
         */

        NotifyingThread<String> thread=new NotifyingThread<String>() {

            //this thread won't return anything.
            @Override
            public List<String> extractParams() {
                return null;
            }

            @Override
            public void doRun() {

                //waiter object that makes so this thread waits for the image to be fetched.
                Object artistWaiter=new Object();

                //get artist with the right id
                MuzikkGlobalInfo.SpotifyAPI.getService().getArtist(artist.id, new Callback<Artist>() {
                    boolean succeeded = false;

                    @Override
                    public void success(Artist artistReturned, Response response) {
                        System.out.println("Fetched artist!");

                        //notify the waiter so that this thread can continue.
                        synchronized (artistWaiter) {
                            artistWaiter.notify();
                        }

                        succeeded = true;

                        /*
                            We've now fetched the artist and we have the URL to the image.
                            Start a new thread and download the image.
                         */
                        Image artistImage=null;
                        NotifyingThread thread=new NotifyingThread() {
                            @Override
                            public List extractParams() {
                                return null;
                            }

                            @Override
                            public void doRun() {

                                //download the image.
                                Image artistImage=new Image(artistReturned.images.get(0).url);

                                //go to UI thread and update the image
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {

                                        imgView.setImage(artistImage);
                                        System.out.println("placing artist image in ImageView");
                                    }
                                });
                            }
                        };

                        thread.start();
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

    /**
     * Sets previous stage.
     * @param stage - the stage to set as previous.
     */
    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }



}
