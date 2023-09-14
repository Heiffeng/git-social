package site.achun.git.social.compents;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.util.StringUtils;
import site.achun.git.social.MainApplication;
import site.achun.git.social.local.ConfigFileHandler;
import site.achun.git.social.local.ConfigObject;
import site.achun.git.social.local.CurrentUser;
import site.achun.git.social.local.GitUtil;

import java.io.IOException;

public class InitVBox extends VBox {
    private InputHBox repo;
    private InputHBox username;
    private InputHBox password;
    private Label tipsLabel;
    public InitVBox(){
        this.setSpacing(15);
        this.setPadding(new Insets(10)); // 设置容器的边距

        Label descriptionLabel = new Label("""
                Please input your git repository url,username and password. 
                """);
        descriptionLabel.setAlignment(Pos.CENTER);
        descriptionLabel.setMinWidth(760);

        // 添加文本字段
        repo = new InputHBox("Repository", "Input center repository url...");
        username = new InputHBox("Username", "Input username...");
        password = new InputHBox("Password","Input password...");

        // 添加标签
        tipsLabel = new Label("");
        tipsLabel.setMinWidth(600);
        // 添加按钮
        Button submitButton = new Button("Submit");
        HBox hbox = new HBox();
        hbox.getChildren().addAll(tipsLabel, submitButton);

        // 添加组件到VBox中
        this.getChildren().addAll(descriptionLabel,repo, username, password, hbox);

        submitButton.setOnAction(this::onSubmit);
    }


    public static class InputHBox extends HBox{
        private TextField textField;
        public InputHBox(String name,String promptText){
            this.setSpacing(10);
            Label label = new Label(name+"：");
            label.setMinWidth(200);
            label.setAlignment(Pos.CENTER_RIGHT);
            textField = new TextField();
            textField.setMinWidth(500);
            textField.setPromptText(promptText);
            getChildren().addAll(label,textField);
        }

        public TextField getTextField() {
            return textField;
        }
    }

    public void onSubmit(ActionEvent event){
        String repoUrl = repo.getTextField().getText();
        String usernameValue = username.getTextField().getText();
        String passwordValue = password.getTextField().getText();

        if(StringUtils.isEmptyOrNull(repoUrl)){
            tipsLabel.setText("Input is null");
            return;
        }
        if(StringUtils.isEmptyOrNull(usernameValue)){
            tipsLabel.setText("username is null");
            return;
        }
        if(StringUtils.isEmptyOrNull(passwordValue)){
            tipsLabel.setText("password is null");
            return;
        }

        try {
            GitUtil.cloneOrPull(repoUrl);
            ConfigFileHandler.getInstance().write(ConfigObject::setRepoUrl,repoUrl);
            ConfigFileHandler.getInstance().write(ConfigObject::setUsername,usernameValue);
            ConfigFileHandler.getInstance().write(ConfigObject::setPassword,passwordValue);

            // 用户名和密码
            if(GitUtil.isCorrectUsernameAndPassword(CurrentUser.fileRepository(),usernameValue,passwordValue)){
                Stage stage = (Stage) repo.getTextField().getScene().getWindow();
                MainApplication.openHomeView(stage);
            }else{
                tipsLabel.setText("username or password is error. Please try again.");
                username.getTextField().setText("");
                password.getTextField().setText("");
            }
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
