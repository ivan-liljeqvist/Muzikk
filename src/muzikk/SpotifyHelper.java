package muzikk;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 *
 * This is a class that has all the functionality for talking to SpotifyAPI.
 * It includes all the functions we need.
 * Created by IvanLiljeqvist on 07/05/15.
 *
 */

public class SpotifyHelper {

    //initiate API object. Contains all functionality
    private static SpotifyApi api = new SpotifyApi();
    private static SpotifyService spotify = api.getService();


    SpotifyHelper(){

        updateAccessToken();

        Runtime rt = Runtime.getRuntime();
        String url = "http://simkoll.com/muzikk";
        try{
            openWebpageURL(new URL(url));
        }
       catch(Exception e){

       }


        //spotify.getMe();

        System.out.println(spotify.getPlaylist("11138106368", "2RWuVk50HO8ygBccQRvvom").name);

    }

    public static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void openWebpageURL(URL url) {
        try {
            openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /*
        Takes a string as a parameter and returns a list with Track objects.
        @param name - a string yuou want to use to search
        @return List with Tracks. null if the search fails.
     */
    public List<Track> searchTrack(String name){

        //this is a class written for fetching results
        final SpotifyTracksResult result=new SpotifyTracksResult();

        spotify.searchTracks(name, new Callback<TracksPager>() {

            @Override
            public void success(TracksPager tracksPager, Response response) {
                //insert tracks into the result object
                result.setTracksReturned(tracksPager.tracks.items);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                System.out.println("Failed to search for tracks.");
            }
        });

        return result.getTrackReturned();

    }

    /*
        This method updates the access token so that the Spotify API can be used.
     */
    private void updateAccessToken(){
        String at = SpotifyAccessFetcher.getSpotifyAccessToken();
        this.api.setAccessToken(at);
    }


}
