package muzikk;

import kaaes.spotify.webapi.android.models.Track;

import java.util.List;

/**
 *
 * Class representing a container for returning Tacks.
 * Used by functions that include a Callback.
 * When functions use Callback they can't modify local variables in the Callback.
 * A container object like this is needed.
 *
 * Created by IvanLiljeqvist on 07/05/15.
 */
public class MuzikkTracksResult {

    private List<Track> tracksReturned;

    MuzikkTracksResult(){
        tracksReturned=null;
    }

    public void setTracksReturned(List<Track> t){
        tracksReturned=t;
    }

    public List<Track> getTrackReturned(){
        return tracksReturned;
    }


}
