package muzikk;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import muzikk.gui.LoginScreenController;
import muzikk.gui.SceneLoader;

import java.awt.event.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        SceneLoader.initializeScene();
        primaryStage.setTitle("main");

        LoginScreenController controller = (LoginScreenController) SceneLoader.loginScreenLoader.getController();
        controller.setPrevStage(primaryStage);
        controller.initData();
        primaryStage.setScene(new Scene(SceneLoader.loginScreenPane));
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
