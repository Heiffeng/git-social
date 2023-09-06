package site.achun.git.social;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.util.StringUtils;
import site.achun.git.social.local.ConfigUtil;
import site.achun.git.social.local.GitUtil;
import site.achun.git.social.views.HomeController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TextField repoUrlTextField;

    @FXML
    private Label tipsLabel;

    @FXML
    protected void onSubmit() throws GitAPIException, IOException {
//        https://gitee.com/heika/my-git-social.git
        String repoUrl = repoUrlTextField.getText();
        if(StringUtils.isEmptyOrNull(repoUrl)){
            tipsLabel.setText("Input is null");
        }
        GitUtil.cloneOrPull(repoUrl);
        ConfigUtil.set("repoUrl",repoUrl);
        Stage stage = (Stage) repoUrlTextField.getScene().getWindow();
        MainApplication.openHomeView(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}