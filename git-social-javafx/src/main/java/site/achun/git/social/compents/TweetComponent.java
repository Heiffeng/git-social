package site.achun.git.social.compents;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import site.achun.git.social.data.CommentsService;
import site.achun.git.social.data.Content;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TweetComponent extends HBox {

    public TweetComponent(Content content) {
        setSpacing(10);
        setPadding(new Insets(5));

        // 用户头像
        String imagePath = content.getUser().getCover();
        ImageView userImage = new ImageView(new Image(new File(imagePath).toURI().toString()));
        userImage.setFitHeight(50);
        userImage.setFitWidth(50);

        // 用户名、时间
        Username username = new Username(content.getUser().getNickname(), content.getTime());

        // 推文文本
        ContentTextFlow contentScrollPane = new ContentTextFlow(content.getContent());

        OperationsHBox operationsHBox = new OperationsHBox(content);

        VBox vbox = new VBox();
        vbox.setMaxWidth(490);
        vbox.setMinWidth(490);
        vbox.getChildren().addAll(username,contentScrollPane,operationsHBox);

        if(content.getComments()!=null){
            List<CommentComponent> commentsList = content.getComments().stream()
                    .map(comment -> new CommentComponent(comment))
                    .collect(Collectors.toList());
            vbox.getChildren().addAll(commentsList);
        }

        getChildren().addAll(userImage, vbox);
    }

    public static class Username extends HBox{
        public Username(String username, LocalDateTime time){
            Label usernameLabel = new Label(username);
            usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

            String timeString = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Label timestampLabel = new Label(timeString);
            timestampLabel.setStyle("-fx-font-size: 10px; -fx-opacity: 0.7");

            this.setSpacing(10);

            this.getChildren().addAll(usernameLabel, timestampLabel);
        }
    }

    public static class ContentTextFlow extends TextFlow {
        public ContentTextFlow(String content){
            Text text = new Text(content);
            text.setFont(Font.font("Arial", FontWeight.NORMAL, 12)); // 你可以根据需要调整字体和字号
            text.setStyle("-fx-opacity: 0.8"); // 调整不透明度以控制存在感

            this.getChildren().add(text);
        }
    }

    public static class OperationsHBox extends GridPane {
        public OperationsHBox(Content content){
            CommentSubmitComponent commentSubmitComponent = new CommentSubmitComponent(text -> {
                try {
                    CommentsService.addComments(content.getUuid(), text);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            Button button3 = new Button("Like");
            this.add(new HBox(),0,0);

            HBox hbox = new HBox();
            hbox.setSpacing(10);
            hbox.getChildren().addAll(commentSubmitComponent,button3);
            this.add(hbox,1,0);
            this.setHgap(200);
        }
    }
}
