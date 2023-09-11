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
import site.achun.git.social.data.Content;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class Tweet extends HBox {

    public Tweet(Content content) {
        setSpacing(10);
        setPadding(new Insets(5));

        // 用户头像
        String imagePath = content.getUser().getCover();
        ImageView userImage = new ImageView(new Image(new File(imagePath).toURI().toString()));
        userImage.setFitHeight(50);
        userImage.setFitWidth(50);

        // 用户名、时间和推文文本
        VBox headerBox = new VBox();
        Label usernameLabel = new Label(content.getUser().getNickname());
        String time = content.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Label timestampLabel = new Label(time);
        headerBox.getChildren().addAll(usernameLabel, timestampLabel);

        Label textLabel = new Label(content.getContent());

        // 评论组件
        HBox commentSubmitBox = new HBox();
        commentSubmitBox.setSpacing(10);
        commentSubmitBox.setAlignment(Pos.CENTER_LEFT);
        TextField commentTextField = new TextField();
        commentTextField.setPromptText("Add a comment...");
        Button commentButton = new Button("Comment");
        commentButton.setOnAction(e -> {
            String comment = commentTextField.getText();
            if (!comment.isEmpty()) {
                // 处理评论，例如，将评论添加到推文中或发送到服务器
                System.out.println("Comment: " + comment);
                try {
                    CommentsService.addComments(content.getUuid(),comment);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                commentTextField.clear();
            }
        });
        commentSubmitBox.getChildren().addAll(commentTextField, commentButton);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(headerBox,textLabel,commentSubmitBox);

        if(content.getComments()!=null){
            List<ShowComment> commentsList = content.getComments().stream()
                    .map(comment -> new ShowComment(comment))
                    .collect(Collectors.toList());
            vbox.getChildren().addAll(commentsList);
        }

        getChildren().addAll(userImage, vbox);
    }
}
