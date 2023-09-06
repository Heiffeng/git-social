package site.achun.git.social.views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import site.achun.git.social.local.ConfigUtil;
import site.achun.git.social.local.GitUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Label tipsLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipsLabel.setText("正在读取关注列表...");
        try {
            String repoUrl = ConfigUtil.get("repoUrl");
            tipsLabel.setText("repoUrl:"+repoUrl);
            // 先刷新 repo
            File repoDir = Path.of("./workspace", GitUtil.getPathFromUri(repoUrl),".git").toFile();
            Git git = new Git(new FileRepository(repoDir));
            git.pull().call();
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
    }



    private List<String> readFollowList(){

        return null;
    }
}
