package site.achun.git.social.data;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.eclipse.jgit.util.StringUtils;
import site.achun.git.social.local.Cache;
import site.achun.git.social.local.GitUtil;
import site.achun.git.social.local.PathUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class DataScanService {

    public static List<Content> contents;

    public static Map<String,List<CommentsInfo>> commentsMap;
    public static void scan() throws IOException {
        contents = new ArrayList<>();
        commentsMap = new HashMap<>();
        if(Cache.follows == null){
            return;
        }

        for (String follow : Cache.follows) {
            Path contentPath = Path.of("./workspace", GitUtil.getPathFromUri(follow), "content");
            // 读取用户信息
            User user = readUserInfo(follow);

            // 读取评论
            File commentsFile = Path.of("./workspace", GitUtil.getPathFromUri(follow), "content", "comments.json").toFile();
            if(commentsFile.exists()){
                List<Comments> commentList = readComments(commentsFile);
                for (Comments comments : commentList) {
                    if(commentsMap.containsKey(comments.uuid())){
                        commentsMap.get(comments.uuid()).add(new CommentsInfo(user,comments));
                    }else{
                        commentsMap.put(comments.uuid(),Arrays.asList(new CommentsInfo(user,comments)));
                    }
                }
            }

            // 读取动态
            if(contentPath.toFile().exists()){
                List<File> contentFiles = Arrays.stream(contentPath.toFile().listFiles())
                        .filter(file -> file.isDirectory())
                        .flatMap(file -> Arrays.stream(file.listFiles()))
                        .collect(Collectors.toList());
                List<Content> currentUserContents = contentFiles.stream()
                        .map(DataScanService::read)
                        .collect(Collectors.toList());
                currentUserContents.stream()
                        .forEach(content -> content.setUser(user));
                contents.addAll(currentUserContents);
            }
        }
        // 读取评论
        for (Content content : contents) {
            if(commentsMap.containsKey(content.getUuid())){
                content.setComments(commentsMap.get(content.getUuid()));
            }
        }
    }

    public static User readUserInfo(String follow) throws IOException {
        File userInfoFile = Path.of("./workspace", GitUtil.getPathFromUri(follow), "user-info.json").toFile();
        User user = new User();
        if(userInfoFile.exists()){
            String userInfoJsonString = Files.lines(userInfoFile.toPath()).collect(Collectors.joining());
            UserInfo userInfo = JSONObject.parseObject(userInfoJsonString, UserInfo.class);
            user.setNickname(userInfo.name());
            user.setCover(Path.of("./workspace",GitUtil.getPathFromUri(follow),userInfo.cover()).toString());
        }else{
            user.setNickname(GitUtil.getPathFromUri(follow));
        }
        user.setRepoUrl(follow);
        return user;
    }

    public static List<Comments> readComments(File file) throws IOException {
        String lines = Files.lines(file.toPath()).collect(Collectors.joining());
        if(StringUtils.isEmptyOrNull(lines)){
            return new ArrayList<>();
        }
        List<Comments> list = JSON.parseArray(lines, Comments.class);
        return list;
    }
    public static Content read(File file){
        Content content = new Content();
        String uuid = file.getName().replace(".md", "");
        content.setUuid(uuid);
        List<String> lines = null;
        try {
            lines = Files.lines(file.toPath()).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Map<String,String> params = new HashMap<>();
        StringBuffer contentStringBuffer = new StringBuffer();
        boolean splitLine = false;
        for (String line : lines) {
            if(line.startsWith("---")){
                splitLine = true;
                continue;
            }
            if(!splitLine){
                if(StringUtils.isEmptyOrNull(line) || !line.contains(":")){
                    continue;
                }
                String key = line.substring(0,line.indexOf(":"));
                String value = line.substring(line.indexOf(":")+1);
                params.put(key,value);
            }else{
                contentStringBuffer.append(line);
                contentStringBuffer.append("\n");
            }
        }
        content.setContent(contentStringBuffer.toString());
        if(params.containsKey("time")){
            LocalDateTime time = LocalDateTime.parse(params.get("time"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            content.setTime(time);
        }
        return content;
    }
}
