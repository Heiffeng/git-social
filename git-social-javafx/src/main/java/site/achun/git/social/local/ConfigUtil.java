package site.achun.git.social.local;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class ConfigUtil {

    private final static String CONFIG_PATH = "./workspace/config.json";

    public static void set(String key,String value) throws IOException {
        JSONObject json = reader();
        json.put(key,value);
        write(json);
    }

    public static String get(String key) throws IOException {
        return reader().getString(key);
    }

    public static void write(JSONObject jsonObject){
        // Write JSON to a file
        try (FileWriter fileWriter = new FileWriter(CONFIG_PATH)) {
            fileWriter.write(jsonObject.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static JSONObject reader() throws IOException {
        JSONObject jsonObject = new JSONObject();
        Path configPath = Path.of(CONFIG_PATH);
        if(!Files.exists(configPath)){
            return jsonObject;
        }
        String content = Files.lines(configPath)
                .collect(Collectors.joining());
        jsonObject = JSON.parseObject(content);
        return jsonObject;
    }
}
