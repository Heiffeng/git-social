package site.achun.git.social.compents;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import site.achun.git.social.data.CommentsInfo;

public class CommentComponent extends HBox{

    public CommentComponent(CommentsInfo info){
        setSpacing(10);
        setPadding(new Insets(5));

        Label usernameLabel = new Label();
        usernameLabel.setText(info.user().getNickname()+"：");

        Label contentLabel = new Label();
        contentLabel.setText(info.comments().content());

        getChildren().addAll(usernameLabel,contentLabel);
    }
}
