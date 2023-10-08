package site.achun.git.social.compents;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import site.achun.git.social.data.User;
import site.achun.git.social.local.CurrentUser;

import java.io.IOException;

public class UserInfoPane extends VBox {

    public UserInfoPane(){
        try {
            User user = CurrentUser.getUser();
            this.getChildren().add(new Label("Name:" + user.getNickname()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
