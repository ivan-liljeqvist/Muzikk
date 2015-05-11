package muzikk;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        SceneLoader.initializeScene();
        primaryStage.setTitle("main");

        LoginScreenController controller = (LoginScreenController) SceneLoader.loginScreenLoader.getController();
        controller.setPrevStage(primaryStage);
        primaryStage.setScene(new Scene(SceneLoader.loginScreenPane));
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);
    }
}
