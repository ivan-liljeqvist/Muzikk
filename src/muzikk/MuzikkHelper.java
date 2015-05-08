package muzikk;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.List;

/**
 *
 * This is a class that has all the functionality for talking to SpotifyAPI.
 * It includes all the functions we need.
 * Created by IvanLiljeqvist on 07/05/15.
 *
 */

public class MuzikkHelper {

    //initiate API object. Contains all functionality
    private static SpotifyApi api = new SpotifyApi();
    private static SpotifyService spotify = api.getService();


    MuzikkHelper(){

        updateAccessToken();

    }



    /*
        Takes a string as a parameter and returns a list with Track objects.
        @param name - a string yuou want to use to search
        @return List with Tracks. null if the search fails.
     */
    public List<Track> searchTrack(String name){

        //this is a class written for fetching results
        final MuzikkTracksResult result=new MuzikkTracksResult();

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
        String at = MuzikkAccessFetcher.getSpotifyAccessToken();
        this.api.setAccessToken(at);
    }


}
