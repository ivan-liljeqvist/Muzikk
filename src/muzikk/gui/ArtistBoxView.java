package muzikk.gui;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


/**
 * Created by IvanLiljeqvist on 12/05/15.
 */
public class ArtistBoxView {

    private VBox box;
    private String artistId;
    private Label artistLabel;

    public ArtistBoxView(String artistId, VBox box){
        this.artistId=artistId;
        this.box=box;
       // this.artistLabel=artistLabel;
    }

    public Label getArtistLabel(){
        return this.artistLabel;
    }

    public void setArtistLabel(Label l){
        this.artistLabel=l;
    }


    public void setBox(VBox box){
        this.box=box;
    }

    public VBox getBox(){
        return box;
    }

    public void setArtistId(String id){
        this.artistId=id;
    }

    public String getArtistId(){
        return artistId;
    }
}
