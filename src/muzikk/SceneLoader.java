package muzikk;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Created by filip on 2015-05-07.
 */
public class SceneLoader {
    public static FXMLLoader modeSelectionLoader;
    public static Pane modeSelectionPane;
    public static FXMLLoader spLoader;
    public static FXMLLoader mpLoader;
    public static FXMLLoader gameLoader;
    public static FXMLLoader loginScreenLoader;
    public static Pane spPane;
    public static Pane mpPane;
    public static Pane gamePane;
    public static Pane loginScreenPane;


    public SceneLoader(){

    }

    public static void initializeScene() throws IOException{
        modeSelectionLoader = new FXMLLoader(ModeSelectionController.class.getResource("modeSelection.fxml"));
        modeSelectionPane = modeSelectionLoader.load();

        spLoader = new FXMLLoader(singlePlayerScreenController.class.getResource("singlePlayerScreen.fxml"));
        spPane = spLoader.load();

        mpLoader = new FXMLLoader(multiPlayerScreenController.class.getResource("multiPlayerScreen.fxml"));
        mpPane = mpLoader.load();

        gameLoader = new FXMLLoader(gameController.class.getResource("gameScreen.fxml"));
        gamePane = gameLoader.load();

        loginScreenLoader = new FXMLLoader(gameController.class.getResource("loginScreen.fxml"));
        loginScreenPane = loginScreenLoader.load();
    }
}
