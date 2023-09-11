package site.achun.git.social.compents;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import site.achun.git.social.data.CommentsService;

import java.io.File;
import java.io.IOException;

public class Tweet extends HBox {

    public Tweet(String uuid,String username, String text, String imagePath, String timestamp) {
        setSpacing(10);
        setPadding(new Insets(5));

        // 用户头像
        ImageView userImage = new ImageView(new Image(new File(imagePath).toURI().toString()));
        userImage.setFitHeight(50);
        userImage.setFitWidth(50);

        // 用户名、时间和推文文本
        VBox headerBox = new VBox();
        Label usernameLabel = new Label(username);
        Label timestampLabel = new Label(timestamp);
        headerBox.getChildren().addAll(usernameLabel, timestampLabel);

        Label textLabel = new Label(text);

        // 评论组件
        HBox commentBox = new HBox();
        commentBox.setSpacing(10);
        commentBox.setAlignment(Pos.CENTER_LEFT);
        TextField commentTextField = new TextField();
        commentTextField.setPromptText("Add a comment...");
        Button commentButton = new Button("Comment");
        commentButton.setOnAction(e -> {
            String comment = commentTextField.getText();
            if (!comment.isEmpty()) {
                // 处理评论，例如，将评论添加到推文中或发送到服务器
                System.out.println("Comment: " + comment);
                try {
                    CommentsService.addComments(uuid,comment);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                commentTextField.clear();
            }
        });
        commentBox.getChildren().addAll(commentTextField, commentButton);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(headerBox,textLabel,commentBox);


        getChildren().addAll(userImage, vbox);
    }
}
