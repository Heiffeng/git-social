package site.achun.git.social.data;

import site.achun.git.social.local.Cache;
import site.achun.git.social.local.GitUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataScanService {

    public static List<Content> contents;
    public static void scan(){
        if(Cache.follows == null){
            return;
        }
        for (String follow : Cache.follows) {
            Path contentPath = Path.of("./workspace", GitUtil.getPathFromUri(follow), "content");
            if(contentPath.toFile().exists()){
                List<File> contentFiles = Arrays.stream(contentPath.toFile().listFiles())
                        .flatMap(file -> Arrays.stream(file.listFiles()))
                        .collect(Collectors.toList());
                contents = contentFiles.stream()
                        .map(DataScanService::read)
                        .collect(Collectors.toList());
            }
        }
    }

    public static Content read(File file){
        Content content = new Content();
        String uuid = file.getName().replace(".md", "");
        content.setUuid(uuid);
        try {
            List<String> contentString = Files.lines(file.toPath()).collect(Collectors.toList());
            content.setContent(contentString.stream().collect(Collectors.joining("\n")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content;
    }
}
