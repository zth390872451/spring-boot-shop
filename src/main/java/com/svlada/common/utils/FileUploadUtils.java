package com.svlada.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.svlada.common.utils.DateUtils.PART_DATE_FORMAT;

public class FileUploadUtils {
    private static final Logger log = LoggerFactory.getLogger(FileUploadUtils.class);
    public static final String http_path = "http://www.pcjshe.com";
    /**
     * 保存文件路径：年/月/日/
     * @param
     * @Desc 上传文件
     */
    public static List<String> saveCommonFile(MultipartFile[] files, String uploadPath) {
        try {
            List<String> filePaths = new ArrayList<>();
            for (MultipartFile file : files){
                String tomcatPath = ApplicationSupport.getValue("tomcatPath");
                String path = tomcatPath + "/" + uploadPath + "/" +
                    DateUtils.getFormatDate(null, PART_DATE_FORMAT) + "/"
                    + UUID.randomUUID().toString()
                    +"/"+ file.getName() + "/";
                File dir = new File(path);
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        try {
                            throw new Exception("创建保存目录失败");
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                }
                String originalFilename = file.getOriginalFilename();
                String fileName = file.getName() + getFileSuffix(file.getOriginalFilename());
                File target = new File(dir, originalFilename);
                file.transferTo(target);
                String absolutePath = target.getAbsolutePath();
                String relativePath = http_path + absolutePath.replace(tomcatPath, "");
                filePaths.add(relativePath);
            }
            return filePaths;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String upload(String base64Data, String uploadPath){
        try{
            String dataPrix = "";
            String data = "";
            if(base64Data == null || "".equals(base64Data)){
                log.error("上传失败，上传图片数据有误！");
                return null;
            }else{
                String [] d = base64Data.split("base64,");
                if(d != null && d.length == 2){
                    dataPrix = d[0];
                    data = d[1];
                }else{
                    throw new Exception("上传失败，数据不合法");
                }
            }
            String suffix = "";
            if("data:image/jpeg;".equalsIgnoreCase(dataPrix)){//data:image/jpeg;base64,base64编码的jpeg图片数据
                suffix = ".jpg";
            } else if("data:image/x-icon;".equalsIgnoreCase(dataPrix)){//data:image/x-icon;base64,base64编码的icon图片数据
                suffix = ".ico";
            } else if("data:image/gif;".equalsIgnoreCase(dataPrix)){//data:image/gif;base64,base64编码的gif图片数据
                suffix = ".gif";
            } else if("data:image/png;".equalsIgnoreCase(dataPrix)){//data:image/png;base64,base64编码的png图片数据
                suffix = ".png";
            }else{
                throw new Exception("上传图片格式不合法");
            }
            String tempFileName = UUID.randomUUID().toString() + suffix;
            //因为BASE64Decoder的jar问题，此处使用spring框架提供的工具包
            byte[] bs = Base64Utils.decodeFromString(data);
            try{
                String tomcatPath = ApplicationSupport.getValue("tomcatPath");
                String relativePath = tomcatPath+"/" + tempFileName;
                String absolutePath = http_path +"/" + tempFileName;
                Path pathObj = Paths.get(relativePath);
                Files.write(pathObj, bs);
                return absolutePath;
            }catch(Exception ee){
                log.error("ee 上传失败，写入文件失败!",ee);
                return null;
            }
        }catch (Exception e) {
            log.error("e 上传失败，写入文件失败!",e);
            return null;
        }
    }

    /**
     * 获取文件后缀
     * @param originalFileName
     * @return
     */
    public static String getFileSuffix(String originalFileName){
        int dot=originalFileName.lastIndexOf('.');
        if((dot>-1)&&(dot<originalFileName.length())){
            return originalFileName.substring(dot);
        }
        return originalFileName;
    }
}
