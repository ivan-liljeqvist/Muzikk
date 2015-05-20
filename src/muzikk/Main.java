package muzikk;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import muzikk.gui.LoginScreenController;
import muzikk.gui.SceneLoader;



public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        MuzikkGlobalInfo.globalStage=primaryStage;

        SceneLoader.initializeScene();

        primaryStage.getIcons().add(new Image("http://simkoll.com/muzikk/icon.png"));

        primaryStage.setTitle("MUZIKK");

        primaryStage.setResizable(false);

        LoginScreenController c = SceneLoader.loginScreenLoader.getController();
        c.initData();


        primaryStage.setScene(SceneLoader.loginScene);
        primaryStage.show();


        /*
            This makes so that all threads are killed when you close the application.
         */
        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
            com.sun.javafx.application.PlatformImpl.tkExit();
        });

    }



    public static void main(String[] args) {

        launch(args);
    }
}
