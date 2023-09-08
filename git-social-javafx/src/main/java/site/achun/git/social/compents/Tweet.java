package site.achun.git.social.compents;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;

public class Tweet extends HBox {

    public Tweet(String username, String text, String imagePath) {
        setSpacing(10);
        setPadding(new Insets(5));

        // 用户头像
        ImageView userImage = new ImageView(new Image(new File(imagePath).toURI().toString()));
        userImage.setFitHeight(50);
        userImage.setFitWidth(50);

        // 用户名和推文文本
        VBox contentBox = new VBox();
        Label usernameLabel = new Label(username);
        Label textLabel = new Label(text);

        contentBox.getChildren().addAll(usernameLabel, textLabel);

        getChildren().addAll(userImage, contentBox);
    }
}
