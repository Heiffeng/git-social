package site.achun.git.social.compents;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eclipse.jgit.api.errors.GitAPIException;
import site.achun.git.social.local.FollowsFileUtil;

import java.io.IOException;

public class AddFollows extends VBox {

    public AddFollows(){
        // 创建文本输入框
        TextField textField = new TextField();
        textField.setPromptText("请输入内容");

        // 创建确认按钮
        Button confirmButton = new Button("确认");

        // 创建用于显示错误提示的标签
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;"); // 设置错误提示文字为红色

        // 添加事件处理，当点击确认按钮时，检查输入内容并显示错误提示
        confirmButton.setOnAction(event -> {
            String inputText = textField.getText();
            if (inputText.isEmpty()) {
                errorLabel.setText("输入不能为空！");
            } else {
                // 清除错误提示
                errorLabel.setText("");
                // 在此可以处理输入内容
                this.addFollows(inputText);
                Stage stage = (Stage)textField.getScene().getWindow();
                stage.close();
            }
        });

        // 创建垂直布局容器并添加元素
        this.setSpacing(10);
        this.getChildren().addAll(textField, confirmButton, errorLabel);
        this.setPadding(new Insets(10)); // 设置容器的边距
    }


    public void addFollows(String url){
        try {
            FollowsFileUtil.addFollows(url);
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
