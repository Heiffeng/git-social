package site.achun.git.social.compents;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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

public class AddContent extends VBox{

    public AddContent(){
        TextArea textArea = new TextArea();
        textArea.setPromptText("Input something");

        // 创建确认按钮
        Button confirmButton = new Button("确认");

        // 创建用于显示错误提示的标签
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;"); // 设置错误提示文字为红色


        // 添加事件处理，当点击确认按钮时，检查输入内容并显示错误提示
        confirmButton.setOnAction(event -> {
            String inputText = textArea.getText();
            if (inputText.isEmpty()) {
                errorLabel.setText("输入不能为空！");
            } else {
                // 清除错误提示
                errorLabel.setText("");
                // 在此可以处理输入内容
                try {
                    this.onSubmit(inputText);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (GitAPIException e) {
                    throw new RuntimeException(e);
                }
                Stage stage = (Stage)textArea.getScene().getWindow();
                stage.close();
            }
        });

        // 创建垂直布局容器并添加元素
        this.setSpacing(10);
        this.getChildren().addAll(textArea, confirmButton, errorLabel);
        this.setPadding(new Insets(10)); // 设置容器的边距
    }

    protected void onSubmit(String content) throws IOException, GitAPIException {
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
