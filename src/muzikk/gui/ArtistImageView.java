package muzikk.gui;

import javafx.scene.image.ImageView;

import java.awt.*;

/**
 * Created by IvanLiljeqvist on 12/05/15.
 */
public class ArtistImageView {

    private ImageView iv;
    private String artistId;

    public ArtistImageView(String artistId,ImageView iv){
        this.artistId=artistId;
        this.iv=iv;
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
