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
        firstScreenController controller = (firstScreenController) SceneLoader.firstScreenLoader.getController();
        controller.setPrevStage(primaryStage);
        primaryStage.setScene(new Scene(SceneLoader.firstScreenPane));
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);
    }
}
