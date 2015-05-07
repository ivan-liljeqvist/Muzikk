package muzikk;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * This class has static methods for fetching the access token needed by Spotify.
 * It calls http://simkoll.com/spotifyAccess.php which is a service I've written in PHP
 * that has the app ID and secret key for our app. The web service contacts Spotify and gives
 * us the access token.
 *
 * Created by IvanLiljeqvist on 07/05/15.
 */
public class SpotifyAccessFetcher {

    /*
        This method fetches the access token to access Spotify API for this app.
        @return Spotify Access Token for this app. The token will be active for 3600 seconds.
     */

    public static String getSpotifyAccessToken(){

        String JSONResponse=excutePost("http://simkoll.com/spotifyAccess.php","");
        JsonElement responseJson=new JsonParser().parse(JSONResponse);

        return responseJson.getAsJsonObject().get("access_token").getAsString();

    }

    /*
        This private method executes a post request.
     */
    private static String excutePost(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length",Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
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

}
