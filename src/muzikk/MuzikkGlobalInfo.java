package muzikk;

import kaaes.spotify.webapi.android.models.PlaylistSimple;
import muzikk.backend.MuzikkHelper;

/**
 * Created by IvanLiljeqvist on 08/05/15.
 *
 * A static class represeting the logged in user.
 */
public class MuzikkGlobalInfo {

    public static MuzikkHelper SpotifyAPI=new MuzikkHelper();
    private static int numberOfQuestions;
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
}
