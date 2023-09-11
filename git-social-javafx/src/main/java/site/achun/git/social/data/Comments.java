package site.achun.git.social.data;

import java.time.LocalDateTime;

public record Comments(
        String uuid,
        String content,
        String time
) {
}
