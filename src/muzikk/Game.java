package muzikk;

import java.util.ArrayList;

/**
 * Created by filip on 2015-05-07.
 */
public class Game {
    private ArrayList<Player> players = new ArrayList<Player>(10);
    private ArrayList<String> genres = new ArrayList<String>(10);
    private ArrayList<String> songURLs = new ArrayList<String>(10);
    private int numSongs;
    public Game(){

    }
    public ArrayList<Player> getPlayers(){
        return players;
    }
    public void setPlayers(ArrayList<Player> players){
        this.players = players;
    }
    public ArrayList<String> getGenres(){
        return genres;
    }
    public void setGenres(ArrayList<String> genres){
        this.genres = genres;
    }
    public ArrayList<String> getSongURLs(){
        return songURLs;
    }
    public void setSongURLs(ArrayList<String> songURLs){
        this.songURLs = songURLs;
    }
    public int getNumSongs(){
        return numSongs;
    }
    public void setNumSongs(int numSongs){
        this.numSongs = numSongs;
    }



}
