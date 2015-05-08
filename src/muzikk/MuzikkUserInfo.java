package muzikk;

/**
 * Created by IvanLiljeqvist on 08/05/15.
 *
 * A static class represeting the logged in user.
 */
public class MuzikkUserInfo {

    private static String user_id;
    private static String user_email;

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
}
