package site.achun.git.social.local;

import org.eclipse.jgit.internal.storage.file.FileRepository;
import site.achun.git.social.data.DataScanService;
import site.achun.git.social.data.User;

import java.io.IOException;
import java.nio.file.Path;

public class CurrentUser {

    public static String dirPath(){
        return Path.of("./workspace",GitUtil.getPathFromUri(ConfigFileHandler.getRepoUrl())).toString();
    }

    public static FileRepository fileRepository() throws IOException {
        return new FileRepository(Path.of(CurrentUser.dirPath(),".git").toFile());
    }

    public static User getUser() throws IOException {
        return DataScanService.readUserInfo(ConfigFileHandler.getRepoUrl());
    }
}
