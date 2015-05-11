package muzikk;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * This class has static methods for fetching the access token and logging the user in.
 *
 * Created by IvanLiljeqvist on 07/05/15.
 */
public class MuzikkAccessFetcher {

    /*
        This method fetches the access token to access Spotify API for this app.
        @return Spotify Access Token for this app. The token will be active for 3600 seconds.
     */

    public static String getSpotifyAccessToken(){

        final String GET_TOKEN_URL="http://simkoll.com/spotifyAccess.php";

        //send request to server
        String JSONResponse=executePost(GET_TOKEN_URL,"");
        JsonElement responseJson=new JsonParser().parse(JSONResponse);

        //get 'access_token' element in the JSON returned by the server
        return responseJson.getAsJsonObject().get("access_token").getAsString();

    }

    /*
        Sends a request to hasUserInfo.php and checks if there are any user details.
        @return an array of String where the first element is the id of the user and the second is the email of the user.
        Returns null if no user information is found.
     */
    public static String[] attemptToGetLoggedInUser(){

        final String USER_INFO_URL="http://simkoll.com/muzikk/hasUserInfo.php";

        //send request to the server
        String loggedInUser=executePost(USER_INFO_URL, "");

        //if the server returned 'no user' - no user is logged in
        if(loggedInUser.trim().equals("no user")){
            return null;
        }
        else{
            //the response will be in the form: "ID EMAIL". Therefore we split the response string.
            String [] results= loggedInUser.split(" ");
            return results;
        }
    }

    /*
        Starts a thread and keeps pinging server until a user is logged in.
        @return a Notifying Thread instance that runs the pinging.
     */

    public static NotifyingThread keepPingingServerUntilUserLoggedIn(){

        /*
            This method will ping the server forever until a user is logged in.
            We don't want the entire program to freeze during that time.
            We start a thread.
         */

        NotifyingThread thread=new NotifyingThread() {
            private String [] user_info;

            @Override
            public String[] extractParams() {
                return user_info;
            }

            @Override
            public void doRun() {
                    //keep checking if logged in
                    boolean logged_in=false;

                    //if not logged in
                    while(!logged_in){
                        //keep pinging
                        user_info=attemptToGetLoggedInUser();

                        //check if logged in
                        if(user_info!=null){
                            //this flag will stop the loop
                            logged_in=true;


                        }

                    }

            }
        };

        thread.start();

        return thread;


    }

    /*
        Sends user to the login webpage.
     */
    public static void initiateLogin(){

        cleanUpUserInfoOnServer();

        final String LOGIN_URL="http://simkoll.com/muzikk";

        try{
            //we need to pass URL object.
            //make one with the URL string.
            openWebpage(new URL(LOGIN_URL));

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    /*
        Cleans up old information on the server.
        Should be used before trying to log in.
        So the old user information wont be mistaken for new user info.
     */

    private static void cleanUpUserInfoOnServer(){

        final String CLEAN_UP_URL="http://simkoll.com/muzikk/cleanup.php";
        executePost(CLEAN_UP_URL,"");
    }

    /*
        This private method executes an HTTP post request.
     */
    private static String executePost(String targetURL, String urlParameters) {

        //connection object that will help us with the request
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");

            //give information about the HTTP request
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length",Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Now we've set up the information about the request
            //Here we make the actual request
            DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Now we've sent the request
            //Here we'll receive the response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    /*
        Opens a webpage in the default browser on the system.
        @param uri - the URI you want to open in the browser
     */
    private static void openWebpage(URL url) {
        String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();
        try{

            if (os.indexOf( "win" ) >= 0) {

                // this doesn't support showing urls in the form of "page.html#nameLink"
                rt.exec( "rundll32 url.dll,FileProtocolHandler " + url);

            } else if (os.indexOf( "mac" ) >= 0) {

                rt.exec( "open " + url);

            } else if (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0) {

                // Do a best guess on unix until we get a platform independent way
                // Build a list of browsers to try, in this order.
                String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                        "netscape","opera","links","lynx"};

                // Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
                StringBuffer cmd = new StringBuffer();
                for (int i=0; i<browsers.length; i++)
                    cmd.append( (i==0  ? "" : " || " ) + browsers[i] +" \"" + url + "\" ");

                rt.exec(new String[] { "sh", "-c", cmd.toString() });

            } else {
                return;
            }
        }catch (Exception e){
            return;
        }
        return;

    }

















/*
        //try to initiate a Desktop object that has the browser funcitonality
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;

        //if Desktop object is initiated and Browser is supported - try opening the page
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                //this needs URI. convert URL to URI
                desktop.browse(url.toURI());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

}
