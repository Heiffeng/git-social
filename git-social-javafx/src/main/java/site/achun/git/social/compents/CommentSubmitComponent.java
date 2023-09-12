package site.achun.git.social.compents;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

public class CommentSubmitComponent extends HBox {

    public CommentSubmitComponent(Consumer<String> consumer){
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER_LEFT);
        TextField commentTextField = new TextField();
        commentTextField.setPromptText("Add a comment...");
        this.setStyle("-fx-opacity: 0.5;"); // 设置初始透明度
        commentTextField.setOnMousePressed(event -> {
            this.setStyle("-fx-opacity: 1.0;");
        });
        commentTextField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                this.setStyle("-fx-opacity: 0.5;"); // 失去焦点时设置透明度为 0.5
            }
        });
        Button commentButton = new Button("Submit");
        commentButton.setOnAction(e -> {
            String comment = commentTextField.getText();
            if (!comment.isEmpty()) {
                // 处理评论，例如，将评论添加到推文中或发送到服务器
                System.out.println("Comment: " + comment);
                    consumer.accept(comment);
//                    CommentsService.addComments(content.getUuid(),comment);
                commentTextField.clear();
            }
        });
        this.getChildren().addAll(commentTextField, commentButton);
    }
}
