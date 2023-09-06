package site.achun.git.social.data;

import java.time.LocalDateTime;

public class Content {
    private User user;
    private String uuid;
    private LocalDateTime time;
    private String content;

    public User getUser() {
        return user;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
