package muzikk.gui;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;



/**
 * Created by IvanLiljeqvist on 12/05/15.
 */
public class ArtistImageView {

    private ImageView iv;
    private String artistId;
    private Label artistLabel;

    public ArtistImageView(String artistId,ImageView iv){
        this.artistId=artistId;
        this.iv=iv;
       // this.artistLabel=artistLabel;
    }

    public Label getArtistLabel(){
        return this.artistLabel;
    }

    public void setArtistLabel(Label l){
        this.artistLabel=l;
    }


    public void setIv(ImageView iv){
        this.iv=iv;
    }

    public ImageView getIv(){
        return iv;
    }

    public void setArtistId(String id){
        this.artistId=id;
    }

    public String getArtistId(){
        return artistId;
    }
}
