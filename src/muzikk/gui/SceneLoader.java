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
    public static FXMLLoader ingoGameLoader;
    public static FXMLLoader gameOverLoader;
    public static FXMLLoader mpLoader;
    public static FXMLLoader gameLoader;
    public static FXMLLoader loginScreenLoader;
    public static Pane spPane;
    public static Pane gameOverPane;
    public static Pane ingoGamePane;
    public static Pane mpPane;
    public static Pane gamePane;
    public static Pane loginScreenPane;

    public static Scene modeSelectionScene, spScene, mpScene, gameScene, loginScene,gameOverScene,ingoScene;

    public SceneLoader(){

    }

    public static void initializeScene() throws IOException{

        modeSelectionLoader = new FXMLLoader(ModeSelectionController.class.getResource("/muzikk/fxml/modeSelection.fxml"));
        modeSelectionPane = modeSelectionLoader.load();
        modeSelectionScene=new Scene(modeSelectionPane);

        spLoader = new FXMLLoader(singlePlayerScreenController.class.getResource("/muzikk/fxml/singlePlayerScreen.fxml"));
        spPane = spLoader.load();
        singlePlayerScreenController spC=spLoader.getController();
        spC.initData();
        spScene=new Scene(spPane);

        mpLoader = new FXMLLoader(multiPlayerScreenController.class.getResource("/muzikk/fxml/multiPlayerScreen.fxml"));
        mpPane = mpLoader.load();
        multiPlayerScreenController mpC=mpLoader.getController();
        mpC.initData();
        mpScene=new Scene(mpPane);

        gameLoader = new FXMLLoader(gameController.class.getResource("/muzikk/fxml/gameScreen.fxml"));
        gamePane = gameLoader.load();
        gameScene=new Scene(gamePane);

        loginScreenLoader = new FXMLLoader(gameController.class.getResource("/muzikk/fxml/loginScreen.fxml"));
        loginScreenPane = loginScreenLoader.load();
        loginScene=new Scene(loginScreenPane);

        ingoGameLoader = new FXMLLoader(gameController.class.getResource("/muzikk/fxml/ingoGameScreen.fxml"));
        ingoGamePane = ingoGameLoader.load();
        ingoScene=new Scene(ingoGamePane);

        gameOverLoader = new FXMLLoader(gameOverScreen.class.getResource("/muzikk/fxml/gameOverScreen.fxml"));
        gameOverPane = gameOverLoader.load();
        gameOverScene=new Scene(gameOverPane);
    }
}
