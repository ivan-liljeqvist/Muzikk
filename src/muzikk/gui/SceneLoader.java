package muzikk.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
    public static FXMLLoader gameOverLoader;
    public static Pane spPane;
    public static Pane mpPane;
    public static Pane gamePane;
    public static Pane loginScreenPane;
    public static Pane gameOverPane;

    public static Scene gameScene;



    public SceneLoader(){

    }

    public static void initializeScene() throws IOException{

        modeSelectionLoader = new FXMLLoader(ModeSelectionController.class.getResource("../fxml/modeSelection.fxml"));
        modeSelectionPane = modeSelectionLoader.load();

        spLoader = new FXMLLoader(singlePlayerScreenController.class.getResource("../fxml/singlePlayerScreen.fxml"));
        spPane = spLoader.load();

        mpLoader = new FXMLLoader(multiPlayerScreenController.class.getResource("../fxml/multiPlayerScreen.fxml"));
        mpPane = mpLoader.load();

        gameLoader = new FXMLLoader(gameController.class.getResource("../fxml/gameScreen.fxml"));
        gamePane = gameLoader.load();
        gameScene=new Scene(gamePane);

        loginScreenLoader = new FXMLLoader(gameController.class.getResource("../fxml/loginScreen.fxml"));
        loginScreenPane = loginScreenLoader.load();

        gameOverLoader = new FXMLLoader(gameOverScreen.class.getResource("../fxml/gameOverScreen.fxml"));
        gameOverPane = gameOverLoader.load();
    }


}
