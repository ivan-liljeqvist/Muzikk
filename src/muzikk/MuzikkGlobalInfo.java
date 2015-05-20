package muzikk;

import javafx.stage.Stage;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import muzikk.backend.MuzikkHelper;

import java.util.ArrayList;

/**
 * Created by IvanLiljeqvist on 08/05/15.
 *
 * A static class represeting the logged in user.
 */
public class MuzikkGlobalInfo {

    public static MuzikkHelper SpotifyAPI=new MuzikkHelper();
    private static int numberOfQuestions;
    private static boolean ingoMode = false;

    private static ArrayList<Player> players;

    private static PlaylistSimple chosenPlaylist;

    private static String user_id;
    private static String user_email;
    private static boolean loggedIn=false;

    public static void setUserId(String id){
        user_id=id;
    }

    public static String getUserId(){
        return user_id;
    }

    public static void setUserEmail(String email){
        user_email=email;
    }

    public static String getUserEmail(){
        return user_email;
    }

    public static boolean isLoggedIn(){
        return loggedIn;
    }

    public static void setLoggedIn(boolean li){
        loggedIn=li;
    }

    public static void setChosenPlaylist(PlaylistSimple pl){
        chosenPlaylist=pl;
    }

    public static PlaylistSimple getChosenPlaylist(){
        return chosenPlaylist;
    }

    public static int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public static void setNumberOfQuestions(int numberOfQuestions) {
        MuzikkGlobalInfo.numberOfQuestions = numberOfQuestions;
    }
    public static void setPlayers(ArrayList<Player> pl){
        players = pl;
    }
    public static ArrayList<Player> getPlayers(){
        return players;
    }
    public static void setIngoMode(boolean b){
        ingoMode = b;
    }
    public static boolean getIngoMode(){
        return ingoMode;
    }

    public static Stage globalStage;

    public static boolean shouldResetPlayerTable=false;

}
