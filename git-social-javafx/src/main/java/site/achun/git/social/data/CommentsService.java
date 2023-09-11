package site.achun.git.social.data;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.eclipse.jgit.util.StringUtils;
import site.achun.git.social.local.PathUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class CommentsService {

    // uuid,comments content,time,
    public static void addComments(String uuid,String content) throws IOException {
        File commentFile = Path.of(PathUtil.getCurrentUserGitDirPath(),"content","comments.json").toFile();
        String jsonString = null;
        if(!commentFile.exists()){
            commentFile.getParentFile().mkdirs();
            commentFile.createNewFile();
            Map<String,Comments> map = new HashMap<>();
            jsonString = JSON.toJSONString(Arrays.asList(new Comments(uuid,content,"")));
        }else{
            String lines = Files.lines(commentFile.toPath()).collect(Collectors.joining());
            List<Comments> list = null;
            if(StringUtils.isEmptyOrNull(lines)){
                list = new ArrayList<>();
            }else{
                list = JSON.parseArray(lines, Comments.class);
            }
            list.add(new Comments(uuid,content,""));
            jsonString = JSON.toJSONString(list);
        }
        // Write JSON to a file
        try (FileWriter fileWriter = new FileWriter(commentFile)) {
            fileWriter.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
