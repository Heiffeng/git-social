package site.achun.git.social;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import site.achun.git.social.compents.InitVBox;
import site.achun.git.social.local.ConfigFileHandler;
import site.achun.git.social.views.HomeController;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        // 初始化配置文件
        try {
            ConfigFileHandler.initInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(!ConfigFileHandler.getInstance().getExistConfigFile()){
            // 不存在配置文件，打开初始化页面
            openInitView(stage);
        }else{
            openHomeView(stage);
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
        Scene scene = new Scene(new InitVBox(), 800, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}