package site.achun.git.social.local;

import site.achun.git.social.data.DataScanService;
import site.achun.git.social.data.User;

import java.io.IOException;

public class UserUtil {

    public static User getCurrentUser() throws IOException {
        return DataScanService.readUserInfo(Cache.repoUrl);
    }
}
