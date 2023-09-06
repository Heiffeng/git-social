package site.achun.git.social.local;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;

public class GitUtil {

    public static void cloneOrPull(String repoUrl) throws GitAPIException, IOException {
        File dirFile = Path.of("./workspace",getPathFromUri(repoUrl)).toFile();
        if(!dirFile.exists()) {
            dirFile.mkdirs();
            CloneCommand clone = Git.cloneRepository();
            clone.setURI(repoUrl)
                    .setBranch("master")
                    .setDirectory(dirFile)
                    .call();
        }else{
            File repoDir = Path.of("./workspace", GitUtil.getPathFromUri(repoUrl),".git").toFile();
            Git git = new Git(new FileRepository(repoDir));
            git.pull().call();
        }
    }

    public static String getPathFromUri(String url) {
        URI uri = URI.create(url);
        String path = uri.getPath();
        if(path.endsWith(".git")){
            path = path.substring(0,path.length()-4);
        }
        return path;
    }
}
