package site.achun.git.social.local;

import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FollowsFileUtil {

    public static void initFollowsFile() throws IOException, GitAPIException {
        File followsFile = Path.of("./workspace", GitUtil.getPathFromUri(Cache.repoUrl), "follows.txt").toFile();
        followsFile.createNewFile();
        try (FileWriter fileWriter = new FileWriter(followsFile)) {
            fileWriter.write(Cache.repoUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GitUtil.push();
    }

    public static List<String> readFollows(String repoUrl) throws IOException, GitAPIException {
        File followsFile = Path.of("./workspace", GitUtil.getPathFromUri(repoUrl), "follows.txt").toFile();
        if(!followsFile.exists()){
            initFollowsFile();
        }
        List<String> follows = Files.lines(followsFile.toPath()).collect(Collectors.toList());
        return follows;
    }

    public static void addFollows(String url) throws GitAPIException, IOException {
        List<String> follows = readFollows(Cache.repoUrl);
        follows.add(url);
        File followsFile = Path.of("./workspace", GitUtil.getPathFromUri(Cache.repoUrl), "follows.txt").toFile();
        try (FileWriter fileWriter = new FileWriter(followsFile)) {
            String string = follows.stream().collect(Collectors.joining("\n"));
            fileWriter.write(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GitUtil.push();
    }
}
