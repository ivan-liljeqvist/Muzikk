package muzikk;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import muzikk.gui.LoginScreenController;
import muzikk.gui.SceneLoader;

import java.awt.event.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        MuzikkGlobalInfo.globalStage=primaryStage;

        SceneLoader.initializeScene();



        primaryStage.setTitle("MUZIKK");


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
