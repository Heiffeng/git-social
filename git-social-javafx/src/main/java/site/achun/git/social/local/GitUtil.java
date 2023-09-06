package site.achun.git.social.local;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;

public class GitUtil {

    public static void cloneOrPull(String uri) throws GitAPIException {
        File dirFile = Path.of("./workspace",getPathFromUri(uri)).toFile();
        if(!dirFile.exists()) dirFile.mkdirs();
        CloneCommand clone = Git.cloneRepository();
        clone.setURI(uri)
                .setBranch("master")
                .setDirectory(dirFile)
                .call();
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
