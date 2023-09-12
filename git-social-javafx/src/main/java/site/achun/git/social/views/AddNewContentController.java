package site.achun.git.social.views;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.eclipse.jgit.api.errors.GitAPIException;
import site.achun.git.social.local.Cache;
import site.achun.git.social.local.GitUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class AddNewContentController {

    @FXML
    private TextArea contentTextArea;
    @FXML
    protected void onSubmit() throws IOException, GitAPIException {
        String content = contentTextArea.getText();
        String uuid = UUID.randomUUID().toString().replace("-","");
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMM"));
        Path contentFilePath = Path.of("./workspace", GitUtil.getPathFromUri(Cache.repoUrl), "content", date, uuid + ".md");
        File contentFile = contentFilePath.toFile();
        if(!contentFile.exists()){
            contentFile.getParentFile().mkdirs();
            contentFile.createNewFile();
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append("time:" + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        buffer.append("\n");
        buffer.append("\n");
        buffer.append("-----------------");
        buffer.append("\n");
        buffer.append(content);
        try (FileWriter fileWriter = new FileWriter(contentFile)) {
            fileWriter.write(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 提交到git
        GitUtil.push();
    }
}
