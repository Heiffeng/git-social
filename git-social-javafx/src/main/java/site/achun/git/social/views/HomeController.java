package site.achun.git.social.views;

import com.alibaba.fastjson2.util.DateUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import site.achun.git.social.local.Cache;
import site.achun.git.social.local.ConfigUtil;
import site.achun.git.social.local.GitUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

public class HomeController implements Initializable {

    @FXML
    private Label tipsLabel;
    @FXML
    private TextArea contentTextArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipsLabel.setText("正在读取关注列表...");
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

        // 获取内容
    }


    @FXML
    protected void onSubmit() throws IOException, GitAPIException {
        String content = contentTextArea.getText();
        String uuid = UUID.randomUUID().toString().replace("-","");
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        Path contentFilePath = Path.of("./workspace", GitUtil.getPathFromUri(Cache.repoUrl), "content", date, uuid + ".md");
        File contentFile = contentFilePath.toFile();
        if(!contentFile.exists()){
            contentFile.getParentFile().mkdirs();
            contentFile.createNewFile();
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(content);
        try (FileWriter fileWriter = new FileWriter(contentFile)) {
            fileWriter.write(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        tipsLabel.setText(content);
        // 提交到git
        UsernamePasswordCredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(ConfigUtil.get("username"),ConfigUtil.get("password"));
        File repoDir = Path.of("./workspace", GitUtil.getPathFromUri(Cache.repoUrl),".git").toFile();
        Git git = new Git(new FileRepository(repoDir));
        git.add().addFilepattern(".").call();
        git.commit().setAll(true).setMessage("AUTO COMMIT").call();
        git.push().setRemote("origin").setCredentialsProvider(credentialsProvider).call();
    }

}
