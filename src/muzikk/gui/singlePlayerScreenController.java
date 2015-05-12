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
import java.util.HashMap;
import java.util.List;
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
        String genreKey=playListListView.getSelectionModel().getSelectedItem();
        String playlistID=observableMapGenres.get(genreKey);

        Object playlistWaiter=new Object();

        /*
            LOGGED IN - use the playlist user chose
         */
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

        synchronized (playlistWaiter){
            try{
                playlistWaiter.wait();
            }catch (Exception e){
                e.printStackTrace();
            }
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
            playListListView.getSelectionModel().select(0);
            playListLabel.setText("Choose genre");
        }
    }

}
