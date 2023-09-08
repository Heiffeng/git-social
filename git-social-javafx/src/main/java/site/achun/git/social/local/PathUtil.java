package site.achun.git.social.local;

import org.eclipse.jgit.internal.storage.file.FileRepository;

import java.io.IOException;
import java.nio.file.Path;

public class PathUtil {

    public static String getCurrentUserGitDirPath(){
        return Path.of("./workspace",GitUtil.getPathFromUri(Cache.repoUrl)).toString();
    }

    public static FileRepository getCurrentUserFileRepository() throws IOException {
        return new FileRepository(Path.of(getCurrentUserGitDirPath(),".git").toFile());
    }
}
