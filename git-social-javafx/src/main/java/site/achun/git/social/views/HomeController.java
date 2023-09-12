package site.achun.git.social.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import site.achun.git.social.compents.TweetComponent;
import site.achun.git.social.data.Content;
import site.achun.git.social.data.DataScanService;
import site.achun.git.social.local.Cache;
import site.achun.git.social.local.GitUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class HomeController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ScrollPane scrollPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // 先刷新 repo
            File repoDir = Path.of("./workspace", GitUtil.getPathFromUri(Cache.repoUrl),".git").toFile();
            Git git = new Git(new FileRepository(repoDir));
            git.pull().call();

            // 读取关注列表
            Path followsPath = Path.of("./workspace", GitUtil.getPathFromUri(Cache.repoUrl), "follows.txt");
            if(followsPath.toFile().exists()){
                List<String> follows = Files.lines(followsPath).collect(Collectors.toList());
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
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CanceledException e) {
            throw new RuntimeException(e);
        } catch (NoHeadException e) {
            throw new RuntimeException(e);
        } catch (RefNotAdvertisedException e) {
            throw new RuntimeException(e);
        } catch (RefNotFoundException e) {
            throw new RuntimeException(e);
        } catch (WrongRepositoryStateException e) {
            throw new RuntimeException(e);
        } catch (InvalidRemoteException e) {
            throw new RuntimeException(e);
        } catch (TransportException e) {
            throw new RuntimeException(e);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        } catch (GitAPIException e) {
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
    protected void addNewContent() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(AddNewContentController.class.getResource("add-new-content.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    private HBox buildContentView(Content content){
        return new TweetComponent(content);
    }

}
