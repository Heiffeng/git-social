package site.achun.git.social.views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import site.achun.git.social.compents.AddContent;
import site.achun.git.social.compents.AddFollows;
import site.achun.git.social.compents.TweetComponent;
import site.achun.git.social.compents.UserInfoPane;
import site.achun.git.social.data.Content;
import site.achun.git.social.data.DataScanService;
import site.achun.git.social.local.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;

public class HomeController implements Initializable {

    @FXML
    private ScrollPane scrollPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // 先刷新 repo
            File repoDir = Path.of(CurrentUser.dirPath(),".git").toFile();
            Git git = new Git(new FileRepository(repoDir));
            git.pull().call();

            // 读取关注列表
            List<String> follows = FollowsFileUtil.readFollows(ConfigFileHandler.getRepoUrl());
            if(!follows.isEmpty()){
                Cache.follows = follows;
                follows.stream().forEach(followRepoUrl -> {
                    try {
                        GitUtil.cloneOrPull(followRepoUrl);
                    } catch (GitAPIException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(vbox);

        // 获取内容
        try {
            DataScanService.scan();
            if(DataScanService.contents!=null){
                DataScanService.contents.stream().forEach(content -> vbox.getChildren().add(buildContentView(content)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void addNewContent(){
        Stage stage = new Stage();
        stage.setTitle("Add Content");
        Scene scene = new Scene(new AddContent(), 400, 300);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void addNewFollows(){
        Scene scene = new Scene(new AddFollows(),300,200);
        Stage stage = new Stage();
        stage.setTitle("Add Follows");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void openUserInfo(){
        Stage curStage = (Stage) scrollPane.getScene().getWindow();
        curStage.close();
        Scene scene = new Scene(new UserInfoPane(),300,200);
        Stage stage = new Stage();
        stage.setTitle("UserInfo");
        stage.setScene(scene);
        stage.show();
    }
    private HBox buildContentView(Content content){
        return new TweetComponent(content);
    }

}
