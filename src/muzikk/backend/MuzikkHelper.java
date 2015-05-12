package muzikk.backend;

import jaco.mp3.player.MP3Player;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.*;
import muzikk.MuzikkGlobalInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.net.URL;
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


    public MuzikkHelper(){

        updateAccessToken();

    }

    /*
        @return a List of SimplePlaylist consisting of all of the logged in user's
         playlists that are public.
     */
    public List<PlaylistSimple>getAllPlaylists(){
        return spotify.getPlaylists(MuzikkGlobalInfo.getUserId()).items;
    }

    /*
        @return a List of Track with all the Tracks from all public Playlists
        that the user has.
     */
    public NotifyingThread getAllTracksFromPlaylists(List<PlaylistSimple> playlists){

        NotifyingThread<Track> thread=new NotifyingThread<Track>() {

            //container with all the tracks.
            final MuzikkTracksResult tracksToReturn=new MuzikkTracksResult();

            @Override
            public List<Track> extractParams() {
                return tracksToReturn.getTrackReturned();
            }

            @Override
            public void doRun() {

                for(PlaylistSimple pl : playlists){



                    Object playlistWaiter = new Object();

                    if(pl.tracks.total<=0){
                        synchronized (playlistWaiter){
                            playlistWaiter.notify();
                        }

                        continue;
                    }
                    //get all the tracks from this playlist
                    spotify.getPlaylistTracks(pl.owner.id, pl.id, new Callback<Pager<PlaylistTrack>>() {

                        @Override
                        public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                            List<PlaylistTrack> plTracks=playlistTrackPager.items;

                            for(PlaylistTrack plTrack:plTracks){

                                if(plTrack!=null && plTrack.track!=null){
                                    tracksToReturn.addTrack(plTrack.track);
                                }

                            }

                              /*
                                Notify the waiting object so that the thread can go on.
                             */

                            synchronized (playlistWaiter){
                                playlistWaiter.notify();
                            }


                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            System.out.println("Couldn't get tracks in playlist.");

                            /*
                                Notify the waiting object so that the thread can go on.
                             */

                            synchronized (playlistWaiter){
                                playlistWaiter.notify();
                            }
                        }
                    });

                    /*
                        Wait until all tracks from this playlists are fetched, or if the fetch fails
                     */
                    synchronized (playlistWaiter){
                        try{
                            playlistWaiter.wait();
                        }catch(Exception e){
                            System.out.println("error while waiting");
                        }
                    }


                }
            }
        };

        thread.setName("getAllTracksThread");
        thread.start();


        return thread;
    }

    /*
        Returns SpotifyService object.
     */
    public SpotifyService getService(){
        return spotify;
    }

    /*
        Takes a string as a parameter and returns a list with Track objects.
        @param name - a string yuu want to use to search
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
        Plays the 30-seconds preview of the Track you pass in.
        @param track - a Track object you want to play 30-second preview of.
     */

    public void playTrack(Track track){
        URL trackURL=null;
        try{
            trackURL=new URL(track.preview_url);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        MP3Player player=new MP3Player(trackURL);
        player.play();
    }

    /*
        This method updates the access token so that the Spotify API can be used.
     */
    private void updateAccessToken(){
        String at = MuzikkAccessFetcher.getSpotifyAccessToken();
        this.api.setAccessToken(at);
    }


}
