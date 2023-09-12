package site.achun.git.social;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.eclipse.jgit.util.StringUtils;
import site.achun.git.social.local.Cache;
import site.achun.git.social.local.ConfigUtil;
import site.achun.git.social.views.HomeController;
import site.achun.git.social.views.InitController;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
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
        Scene scene = new Scene(fxmlLoader.load(), 600, 900);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void openInitView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InitController.class.getResource("init.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}