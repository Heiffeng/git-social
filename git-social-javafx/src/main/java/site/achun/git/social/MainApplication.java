package site.achun.git.social;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.eclipse.jgit.util.StringUtils;
import site.achun.git.social.local.Cache;
import site.achun.git.social.local.ConfigUtil;
import site.achun.git.social.views.HomeController;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            if(!StringUtils.isEmptyOrNull(ConfigUtil.get("repoUrl"))){
                Cache.repoUrl = ConfigUtil.get("repoUrl");
                openHomeView(stage);
            }else{
                openInitView(stage);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void openHomeView(Stage stage) throws IOException {
        stage.setTitle("Home");
        FXMLLoader fxmlLoader = new FXMLLoader(HomeController.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 700);
        stage.setScene(scene);
        stage.show();
    }

    public static void openInitView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}