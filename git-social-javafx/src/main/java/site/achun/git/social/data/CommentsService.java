package site.achun.git.social.data;

import com.alibaba.fastjson2.JSONObject;
import site.achun.git.social.local.PathUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CommentsService {

    // uuid,comments content,time,
    public static void addComments(String uuid,String content) throws IOException {
        File commentFile = Path.of(PathUtil.getCurrentUserGitDirPath(),"content","comments.json").toFile();
        String jsonString = null;
        if(!commentFile.exists()){
            commentFile.getParentFile().mkdirs();
            commentFile.createNewFile();
            Map<String,Comments> map = new HashMap<>();
            map.put(uuid,new Comments(uuid,content,""));
            jsonString = JSONObject.toJSONString(map);
        }else{

        }
        // Write JSON to a file
        try (FileWriter fileWriter = new FileWriter(commentFile)) {
            fileWriter.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
