package muzikk.gui;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
 * Created by Filip on 2015-05-10.
 */
public class multiPlayerScreenController implements Initializable {

    private String key;
    private int counter;
    private ObservableList<String> nameList = FXCollections.observableArrayList();
    private ObservableList<String> keyList = FXCollections.observableArrayList();
    private ArrayList<Player> playerList = new ArrayList<Player>(10);
    private ArrayList<PlaylistSimple> playLists = new ArrayList<>();
    private ObservableList<String> observablePlayLists = FXCollections.observableArrayList();
    private ObservableMap<String, String> observableMapGenres = FXCollections.observableHashMap();

    @FXML
    private Button startGameButton;
    @FXML
    private Label playListLabel;
    @FXML
    private Button addPlayerButton;
    @FXML
    private ToggleButton setKeyToggleButton;
    @FXML
    private TextField nameTextField = new TextField();
    @FXML
    private ChoiceBox<Integer> numberOfQuestionsChoiceBox;
    @FXML
    private TextField keyTextField = new TextField();
    @FXML
    private ListView<String> playerListView;
    @FXML
    private ListView<String> keyListView;
    @FXML
    private ListView<String> playListListView;
    @FXML
    private Button backButton;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        numberOfQuestionsChoiceBox.getItems().addAll(5, 10, 15, 20);
        startGameButton.disableProperty().setValue(true);
        numberOfQuestionsChoiceBox.getSelectionModel().select(0);
        playListListView.setItems(observablePlayLists);
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
            } else if (keyList.contains(event.getCharacter().toUpperCase())) {
                setKeyToggleButton.setText("Key not availible");
                setKeyToggleButton.setStyle("-fx-border-color: red");
            }
        });
        backButton.setOnAction((event)->backButtonPressed());


    }

    private void backButtonPressed(){
        MuzikkGlobalInfo.globalStage.setScene(SceneLoader.modeSelectionScene);
    }

    /**
     * Starts the game
     */
    private void goTogame() {
       // gameController controller = SceneLoader.gameLoader.getController();
        MuzikkGlobalInfo.setNumberOfQuestions(numberOfQuestionsChoiceBox.getSelectionModel().getSelectedItem().intValue());
        String genreKey=playListListView.getSelectionModel().getSelectedItem();
        String playlistID=observableMapGenres.get(genreKey);

        Object playlistWaiter=new Object();

        if (MuzikkGlobalInfo.isLoggedIn()){ //Set chosen playlist if the user is logged in
            MuzikkGlobalInfo.setChosenPlaylist(playLists.get(playListListView.focusModelProperty().get().getFocusedIndex()));
        }
        /*
            NOT LOGGED IN - make a playlist from genre user picked
         */
        else
        {


            NotifyingThread thread=new NotifyingThread() {
                @Override
                public List extractParams() {
                    return null;
                }

                @Override
                public void doRun() {
                    System.out.println("PLAYLIST ID: "+playlistID);
                    MuzikkGlobalInfo.SpotifyAPI.getService().getPlaylist("spotify", playlistID, new Callback<Playlist>() {
                        @Override
                        public void success(Playlist playlist, Response response) {
                            PlaylistSimple pls=new PlaylistSimple();
                            pls.name=playlist.name;
                            pls.owner=playlist.owner;
                            pls.id=playlist.id;
                            PlaylistTracksInformation tracksInfo=new PlaylistTracksInformation();
                            tracksInfo.total=playlist.tracks.total;
                            pls.tracks=tracksInfo;


                            MuzikkGlobalInfo.setChosenPlaylist(pls);

                            System.out.println("Converted genre to playlist.");

                            synchronized (playlistWaiter){
                                playlistWaiter.notify();
                            }
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            System.out.println("Couldn't conver genre to playlisr");
                            synchronized (playlistWaiter){
                                playlistWaiter.notify();
                            }
                        }
                    });
                }



            };

            thread.start();

        }

        if(MuzikkGlobalInfo.isLoggedIn()==false) {
            synchronized (playlistWaiter) {
                try {
                    playlistWaiter.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        gameController controller;
        if (MuzikkGlobalInfo.getIngoMode()){
            controller = SceneLoader.ingoGameLoader.getController();
            System.out.println("ingemar");
        }
        else{
            controller = SceneLoader.gameLoader.getController();
        }


        controller.initData(playerList); //Initializes scene data

        MuzikkGlobalInfo.globalStage.setScene(SceneLoader.gameScene);
    }

    /**
     * Will create and add a new player to the game
     */
    private void addPlayer() {
        counter++;
        if (counter > 1){
            startGameButton.disableProperty().setValue(false);
        }
        playerList.add(new Player(nameTextField.getText(), key)); //Adds a new player to the list of players
        keyList.add(key.toUpperCase());
        nameList.add(nameTextField.getText()); //Adds the players name to the observable list
        resetForm();
    }

    /**
     * Will reset the add player form
     */
    private void resetForm(){
        nameTextField.setText("");
        keyTextField.setText("");
        setKeyToggleButton.setText("Press to set key");
        key = null;
    }

    /*
        Resets the filled in forms.
    */
    public void resetForms(){
        nameTextField.setText("");
        nameList = FXCollections.observableArrayList();
        playerListView.setItems(nameList);

    }

    /**
     * Initializes data for the this scene. Gets playlists from SpotifyAPI.
     */
    public void initData() {

        observablePlayLists = FXCollections.observableArrayList();
        playListListView.setItems(observablePlayLists);
        playListListView.getItems().remove(0,playListListView.getItems().size());

        if (MuzikkGlobalInfo.isLoggedIn()) {
            for (PlaylistSimple pl : MuzikkGlobalInfo.SpotifyAPI.getAllPlaylists()) {
                playLists.add(pl);
                observablePlayLists.add(pl.name);
            }
        }
        else{
            observableMapGenres.put("Rock", "2Qi8yAzfj1KavAhWz1gaem");
            observableMapGenres.put("Pop", "5FJXhjdILmRA2z5bvz4nzf");
            observableMapGenres.put("Hiphop", "5yolys8XG4q7YfjYGl5Lff");
            observableMapGenres.put("Metal", "2k2AuaynH7E2v8mwvhpeAO");
            observableMapGenres.put("Indie", "4wtLaWQcPct5tlAWTxqjMD");
            observableMapGenres.put("Soul", "0UUovM2yGwRThZSy9BvADQ");
            observableMapGenres.put("R&B", "06CemleTteSalaVGVMbgFy");
            observableMapGenres.put("Reggae", "0ifGUu1vx6PVcCASyG3t8m");
            observableMapGenres.put("Country", "4ecQaDJHF55Ls9m2lKIXbI");
            observableMapGenres.put("Jazz", "5O2ERf8kAYARVVdfCKZ9G7");
            observableMapGenres.put("Blues", "0BTnoKdfv5Y60EK5o5bGaq");
            observableMapGenres.put("Punk", "5TuWj7WbayVcr6KbwJ5sBQ");
            observableMapGenres.put("00-tal", "3UybCDm2O3JPQChfCG02EG");
            observableMapGenres.put("90-tal", "2uAichKSjJSyrmal8Kb3W9");
            observableMapGenres.put("80-tal", "6brdrOKuFPmcIlf3jwgV1n");
            observableMapGenres.put("70-tal", "1o4av4ikKDbiUFdTfPdBkL");
            observableMapGenres.put("60-tal", "2NFOUmp2wyR5CrXtKDkUkB");
            observableMapGenres.put("50-tal", "7xADHS7Ryc6oMdqBVhNVQ9");
            playListListView.getItems().addAll(observableMapGenres.keySet());
            playListLabel.setText("Choose genre");
        }
        playListListView.getSelectionModel().select(0); //select the top playlist by default
    }

}
