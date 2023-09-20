package site.achun.git.social.local;

import com.alibaba.fastjson2.JSON;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConfigFileHandler {

    private final static String CONFIG_PATH = "./workspace/config.json";
    private Boolean existConfigFile;
    private ConfigObject configObject;
    private ConfigFileHandler() throws IOException {
        File configFile = Path.of(CONFIG_PATH).toFile();
        existConfigFile = configFile.exists();
        if(existConfigFile){
            String content = Files.lines(configFile.toPath())
                        .collect(Collectors.joining());
            configObject = JSON.parseObject(content,ConfigObject.class);
        }
    }

    public static ConfigFileHandler instance = null;

    public static void initInstance() throws IOException {
        if(instance == null){
            instance = new ConfigFileHandler();
        }
    }
    public static ConfigFileHandler getInstance(){
        return instance;
    }

    public  <T,U> void write(Setter<ConfigObject,U> key, U value) throws IOException {
        if(!existConfigFile){
            File configFile = Path.of(CONFIG_PATH).toFile();
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
            configObject = new ConfigObject();
            existConfigFile = true;
        }
        key.set(configObject,value);
        try (FileWriter fileWriter = new FileWriter(CONFIG_PATH)) {
            fileWriter.write(JSON.toJSONString(configObject));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <R,T> R read(Function<ConfigObject,R> key){
        return key.apply(configObject);
    }

    public Boolean getExistConfigFile() {
        return existConfigFile;
    }

    public static String getRepoUrl(){
        return getInstance().read(ConfigObject::getRepoUrl);
    }

}
