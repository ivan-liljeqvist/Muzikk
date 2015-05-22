package muzikk.gui;

import jaco.mp3.player.MP3Player;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;
import muzikk.Main;
import muzikk.backend.MuzikkHelper;
import muzikk.backend.ThreadCompleteListener;
import muzikk.Player;
import muzikk.MuzikkGlobalInfo;
import muzikk.backend.NotifyingThread;

import javafx.scene.image.ImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

/**
 * Authors Filip Martinsson and Ivan Liljeqvist.
 * Version 10-05-2015
 *
 *
 * This is a controller represeting the in-game window.
 *
 */
public class gameController implements Initializable, ThreadCompleteListener {


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
    private NotifyingThread countdownThread;

    /**
     * Controllers defined in the fxml file.
     */

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
    @FXML
    private Label questionNumberLabel;
    @FXML
    private Label personWhoAnsweredLabel;
    @FXML
    private VBox artistBox0;
    @FXML
    private VBox artistBox1;
    @FXML
    private VBox artistBox2;
    @FXML
    private VBox artistBox3;


    /**
     * Artist view containers containing the ImageView and artist Id.
     * Used to decide if the ImageView clicked is the correct one.
     */
    private ArtistBoxView abv0;
    private ArtistBoxView abv1;
    private ArtistBoxView abv2;
    private ArtistBoxView abv3;

    private List<VBox> artistBoxViews;

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
     * The number of questions the player has answered.
     */

    private int number_of_answered_questions=0;

    private boolean timerStopped=true;

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
        artistBox0.setOnMouseClicked((event) -> artistBoxClicked(artistBox0));
        artistBox1.setOnMouseClicked((event) -> artistBoxClicked(artistBox1));
        artistBox2.setOnMouseClicked((event) -> artistBoxClicked(artistBox2));
        artistBox3.setOnMouseClicked((event) -> artistBoxClicked(artistBox3));

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

                        personWhoAnsweredLabel.setText(answeringPlayer.getName() + " pressed!");
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

        playerObsList.remove(0,playerObsList.size());
        scoreObsList.remove(0,scoreObsList.size());

        /*
            Populate the UI table with players names and scores.
         */
        for(Player p:playersInGame){
            playerObsList.add(p.getName());
            scoreObsList.add(p.getScore());
        }

        number_of_answered_questions=0;
    }

    /**
     * Initializes the game and import the player from the singleplayer setup
     * @param player The player
     */
    public void initData(Player player){

        this.onShowWindow();
        this.playersInGame=new ArrayList<>();

        playerObsList.remove(0,playerObsList.size());
        scoreObsList.remove(0,scoreObsList.size());

        playersInGame.add(player);

        /*
            Populate the UI list with name and score.
         */
        playerObsList.add(player.getName());
        scoreObsList.add(player.getScore());

        number_of_answered_questions=0;

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
            Set loading animations on all artist boxes.
            Do this on the UI thread.
         */
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setLoadingAnimationToAllBoxes();
            }
        });

        resetValuesAfterGameOver();

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
                System.out.println("Caught URL == null exception. Try with another song.");
            }

        }

        /*
            Update GUI element on the UI thread
         */
            Platform.runLater(new Runnable() {
                  @Override
                  public void run() {
                      personWhoAnsweredLabel.setText("");
                      questionNumberLabel.setText("Question "+(number_of_answered_questions+1)+" of "+MuzikkGlobalInfo.getNumberOfQuestions());
                  }
            });


            System.out.println("START NEW QUESTION");
            System.out.println("URL : "+currentlyPlayingTrack.preview_url);

        /*
            Start playing the track and showing the artist images.

            Surround with try-catch because it will sometimes fail even if the URL
            object is not null.
         */

            try

            {
                //try to play the track.
                //if not ingo - play real track
                if(MuzikkGlobalInfo.getIngoMode()==false){
                    MuzikkGlobalInfo.SpotifyAPI.playTrack(currentlyPlayingTrack);
                }
                //if ingo - play one of the ingo tracks
                else{
                    int track_index=randomGenerator.nextInt(5)+1;
                    MuzikkGlobalInfo.SpotifyAPI.playTrack("http://simkoll.com/muzikk/k"+track_index+".mp3");
                }

            }

            catch(
            Exception e
            )

            {

                System.out.println("Caught URL is Malformed exception! start new question");

                //the tracks couldn't be played.
                //we can't proceed with this question.
                //start a new question
                this.startNewQuestion();
            }

            this.

            startPopulatingArtistImages();

        /*
            Reset progress bar.
         */

            progressBar.setProgress(1.0);


        }

    /**
     * Called when player hits play again in game over.
     */
    private void resetValuesAfterGameOver(){
        /*
         * This part handles the resetting of the variables when the player hits Play Again in Game Over screen
         */
        if(MuzikkGlobalInfo.shouldResetPlayerTable){


            /*
             * make the ui tables empty.
             */

             for(Player p : playersInGame){
                 p.setScore(0);
             }

            this.refreshObservablePlayerLists();




            MuzikkGlobalInfo.shouldResetPlayerTable=false;
        }

        if(this.timerStopped){
            startTimer();
            System.out.println("STARTING TIMER");
        }
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







    }

    /**
        Create and start the thread that will manage the countdown.
        This will update the progress bar at the bottom.
        Used to set a timer before a player has to answer.
    */
    private void startTimer(){
        countdown_timer=new Timer();
        timerStopped=false;

        countdownThread=new NotifyingThread() {
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
                                        questionComplete();

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

    private void setLoadingAnimationToAllBoxes(){
        ImageView imgV0=(ImageView)artistBox0.getChildren().get(1);
        ImageView imgV1=(ImageView)artistBox1.getChildren().get(1);
        ImageView imgV2=(ImageView)artistBox2.getChildren().get(1);
        ImageView imgV3=(ImageView)artistBox3.getChildren().get(1);

        imgV0.setImage(new Image(getClass().getResourceAsStream("/muzikk/assets/loading.png")));
        imgV1.setImage(new Image(getClass().getResourceAsStream("/muzikk/assets/loading.png")));
        imgV2.setImage(new Image(getClass().getResourceAsStream("/muzikk/assets/loading.png")));
        imgV3.setImage(new Image(getClass().getResourceAsStream("/muzikk/assets/loading.png")));
    }

    /**
     * Decides whether the clicked artist box is right or wrong and acts accordingly.
     * If right - increase score and start new question.
     * If wrong - decrease score and start new question.
     * @param ab - the artist box that has been clicked.
     */

    private void artistBoxClicked(VBox ab){

        /*
            Ignore this is no one has pressed an action button.
         */
        if(PLAYER_ANSWERING ==false){
            return;
        }

        ArtistBoxView clickedOn=null;
        /*
            get ArtistImageView for this ImageView.
         */
        if(abv0.getBox()==ab){
            clickedOn=abv0;
        }
        else if(abv1.getBox()==ab){
            clickedOn=abv1;
        }
        else if(abv2.getBox()==ab){
            clickedOn=abv2;
        }
        else if(abv3.getBox()==ab){
            clickedOn=abv3;
        }

        /*
            Decide whether right or wrong.
         */

        //if ingo mode - everything is right
        if(abv0.getArtistId().equals(clickedOn.getArtistId()) || MuzikkGlobalInfo.getIngoMode()){
            toogleColorBackAndForth(ab,true);
            userAnsweredRightResponse();
        }else{
            toogleColorBackAndForth(ab,false);
            userAnsweredWrongResponse();
        }
    }

    /**
     * Will toggle the color of the artistbox when an answer is correct or incorrect.
     * @param ab the VBox containing the artist
     * @param green true if the answer was correct
     */
    private void toogleColorBackAndForth(VBox ab,boolean green){

        String greenColor="#99FF99";
        String redColor="#FF5050";

        /*
            change color of the box
         */
        if(green){
            ab.setStyle(" -fx-background-color:"+greenColor);
        }else{
            ab.setStyle(" -fx-background-color:"+redColor);
        }

        /*
            Start a thread that will change the color back in one second.
         */
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    Thread.sleep(400);
                }catch(Exception e){
                    System.out.println("Couldn't sleep..");
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        ab.setStyle("-fx-background-color:transparent");
                        ab.setStyle("");
                        System.out.println(ab.getStyleClass());
                    }
                });

            }
        });

        thread.start();


    }

    /**
     * Will decrease the players score and start next question if the answer was incorrect.
     */
    private void userAnsweredWrongResponse(){
        answeringPlayer.decreaseScore();
        this.startNewQuestion();

        //UPDATE UI TABLE
        this.refreshObservablePlayerLists();

        questionComplete();
    }

    /**
     * Will increase the players score and start next question if the answer was correct.
     */
    private void userAnsweredRightResponse(){
        //INCREASE SCORE
        answeringPlayer.increaseScore();
        this.startNewQuestion();
        //UPDATE THE UI TABLE
        this.refreshObservablePlayerLists();

        questionComplete();
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

        artistBoxViews =new ArrayList<VBox>();
        artistBoxViews.add(artistBox0);
        artistBoxViews.add(artistBox1);
        artistBoxViews.add(artistBox2);
        artistBoxViews.add(artistBox3);

        /*
            Shuffle the list. WE want the artists to appear in random order.
         */
        Collections.shuffle(artistBoxViews);

        /*
            Populate the Image Views with the images.
         */
        populateArtistImage(rightArtist, artistBoxViews.get(0));
        abv0=new ArtistBoxView(rightArtist.id, artistBoxViews.get(0));


        populateArtistImage(wrongArtist1, artistBoxViews.get(1));
        abv1=new ArtistBoxView(wrongArtist1.id, artistBoxViews.get(1));
        populateArtistImage(wrongArtist2, artistBoxViews.get(2));
        abv2=new ArtistBoxView(wrongArtist2.id, artistBoxViews.get(2));
        populateArtistImage(wrongArtist3, artistBoxViews.get(3));
        abv3=new ArtistBoxView(wrongArtist3.id, artistBoxViews.get(3));

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

                //if ingo mode is false - place the real artist names
                if(MuzikkGlobalInfo.getIngoMode()==false) {
                    for (int i = 0; i <= 3; i++) {
                        if (artistBox0 == artistBoxViews.get(i)) {
                            artistLabel0.setText(names[i]);
                        } else if (artistBox1 == artistBoxViews.get(i)) {
                            artistLabel1.setText(names[i]);
                        } else if (artistBox2 == artistBoxViews.get(i)) {
                            artistLabel2.setText(names[i]);
                        } else if (artistBox3 == artistBoxViews.get(i)) {
                            artistLabel3.setText(names[i]);
                        }
                    }
                }
                //otherwise put ingo everywhere
                else{
                    artistLabel0.setText("INGO");
                    artistLabel1.setText("INGO");
                    artistLabel2.setText("INGO");
                    artistLabel3.setText("INGO");
                }

            }
        });

    }

    /**
     *
     * @param artist - artist Object that the image will represent
     * @param artistBox - the view which will contain the image
     */

    private void populateArtistImage(ArtistSimple artist, VBox artistBox){

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

                                //download the image. HÄR ÄR FEL IBLAND!!
                                Image artistImage;

                                /*
                                    If not ingo mode - set the artist image
                                 */
                                if(MuzikkGlobalInfo.getIngoMode()==false){
                                    artistImage=new Image(artistReturned.images.get(0).url);
                                }
                                /*
                                    If ingo mode - set ingo in all images
                                 */
                                else{
                                    int imgNum=randomGenerator.nextInt(5)+1;
                                    artistImage=new Image("http://simkoll.com/muzikk/ingo"+imgNum+".jpg");
                                }


                                //go to UI thread and update the image
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {


                                        ImageView artistImageView=(ImageView)artistBox.getChildren().get(1);
                                        artistImageView.setImage(artistImage);

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
     * Increases the number of answered questions and goes to game over screen
     * if the number of answered questions exceeds the number of questions set for this game.
     */

    private void questionComplete(){
        number_of_answered_questions++;

        if(number_of_answered_questions+1>MuzikkGlobalInfo.getNumberOfQuestions()){
            goToGameOver();
        }
    }

    private void goToGameOver(){

        number_of_answered_questions=0;
        countdown_timer.cancel();
        countdownThread.stop();
        timerStopped=true;

        gameOverScreen controller = SceneLoader.gameOverLoader.getController(); //create the game controller


        if(playersInGame.size()==1){
            controller.initData(playersInGame.get(0));
        }
        else{
            controller.initData(playersInGame);
        }

        MuzikkGlobalInfo.SpotifyAPI.stopMusic();
        MuzikkGlobalInfo.globalStage.setScene(SceneLoader.gameOverScene);
    }




}
