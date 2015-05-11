package muzikk;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application implements ThreadCompleteListener{

    private Stage window;
    private MuzikkHelper helper;

    @Override
    public void start(final Stage primaryStage) throws Exception{
        this.window=primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        this.window.setTitle("Hello World");
        this.window.setScene(new Scene(root, 300, 275));
        this.window.show();

        this.helper=new MuzikkHelper();

        //open the webpage
        MuzikkAccessFetcher.initiateLogin();

        //keep pinging and run the Callback .call when we have a result.
        NotifyingThread pingThread=MuzikkAccessFetcher.keepPingingServerUntilUserLoggedIn();
        //set this as a listener so we know when the pinging has finished
        pingThread.addListener(this);

    }


    public static void main(String[] args) {

        launch(args);
    }

    /*
        This method will run when the thread that pings the server for logged in user
        receives a user back.
     */
    @Override
    public void notifyOfThreadComplete(NotifyingThread thread) {

        //extract the results from the thread
        String user_id=thread.extractParams()[0];
        String user_email=thread.extractParams()[1];

        MuzikkUserInfo.setUserId(user_id);
        MuzikkUserInfo.setUserEmail(user_email);

        //when the user has successfully logged in in the browser
        //we want to focus the Muzikk window, but we can't do it
        //on this secondary thread. All UI operations have to run on Main thread.
        //therefore we call runLater and request focus for the window inside a Runnable.
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                window.requestFocus();
            }
        });
    }
}
