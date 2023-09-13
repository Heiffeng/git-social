package site.achun.git.social.views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.util.StringUtils;
import site.achun.git.social.MainApplication;
import site.achun.git.social.local.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InitController implements Initializable {

    @FXML
    private TextField repoUrlTextField;

    @FXML
    private Label tipsLabel;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    protected void onSubmit() throws GitAPIException, IOException {
//        https://gitee.com/heika/my-git-social.git

        String repoUrl = repoUrlTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        if(StringUtils.isEmptyOrNull(repoUrl)){
            tipsLabel.setText("Input is null");
            return;
        }
        if(StringUtils.isEmptyOrNull(username)){
            tipsLabel.setText("username is null");
            return;
        }
        if(StringUtils.isEmptyOrNull(password)){
            tipsLabel.setText("password is null");
            return;
        }

        GitUtil.cloneOrPull(repoUrl);
        ConfigFileHandler.getInstance().write(ConfigObject::setRepoUrl,repoUrl);
        ConfigFileHandler.getInstance().write(ConfigObject::setUsername,username);
        ConfigFileHandler.getInstance().write(ConfigObject::setPassword,password);

        // 用户名和密码
        if(GitUtil.isCorrectUsernameAndPassword(CurrentUser.fileRepository(),username,password)){
            Stage stage = (Stage) repoUrlTextField.getScene().getWindow();
            MainApplication.openHomeView(stage);
        }else{
            tipsLabel.setText("username or password is error. Please try again.");
            usernameTextField.setText("");
            passwordTextField.setText("");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}