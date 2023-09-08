package site.achun.git.social.local;

import org.controlsfx.control.PrefixSelectionChoiceBox;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.errors.TransportException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

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
            UsernamePasswordCredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("username","password");
            clone.setURI(repoUrl)
                    .setCredentialsProvider(credentialsProvider)
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

    public static boolean isCorrectUsernameAndPassword(Repository repository, String username, String password){
        Git git = new Git(repository);
        UsernamePasswordCredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(username,password);
        try{
            git.push().setCredentialsProvider(credentialsProvider).call();
            return true;
        }catch (InvalidRemoteException e) {
            throw new RuntimeException(e);
        } catch (org.eclipse.jgit.api.errors.TransportException e) {
            return false;
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException, GitAPIException {
        System.out.println(isCorrectUsernameAndPassword(new FileRepository("./workspace/heika/my-git-social/.git"),"blacard@163.com","yunbin"));
    }
}
