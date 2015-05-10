package muzikk;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

/**
 * Created by filip on 2015-05-07.
 */
public class SceneLoader {
    public static FXMLLoader firstScreenLoader;
    public static Pane firstScreenPane;
    public static FXMLLoader spLoader;
    public static FXMLLoader mpLoader;
    public static FXMLLoader gameLoader;
    public static Pane spPane;
    public static Pane mpPane;
    public static Pane gamePane;

    public SceneLoader(){

    }

    public static void initializeScene() throws IOException{
        firstScreenLoader = new FXMLLoader(firstScreenController.class.getResource("firstScreen.fxml"));
        firstScreenPane = firstScreenLoader.load();

        spLoader = new FXMLLoader(singlePlayerScreenController.class.getResource("singlePlayerScreen.fxml"));
        spPane = spLoader.load();

        mpLoader = new FXMLLoader(multiPlayerScreenController.class.getResource("multiPlayerScreen.fxml"));
        mpPane = mpLoader.load();

        gameLoader = new FXMLLoader(gameController.class.getResource("gameScreen.fxml"));
        gamePane = gameLoader.load();
    }
}
