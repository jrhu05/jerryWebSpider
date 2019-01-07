package com.hytcshare.jerrywebspider.utils;

import com.hytcshare.jerrywebspider.enums.ExceptionEnum;
import com.hytcshare.jerrywebspider.exception.SpiderException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Slf4j
public class DownloadUtils {

    public static final int cache = 10 * 1024;
    public static final boolean isWindows;
    public static final String splash;

    static {
        if (System.getProperty("os.name") != null && System.getProperty("os.name").toLowerCase().contains("windows")) {
            isWindows = true;
            splash = "\\";
        } else {
            isWindows = false;
            splash = "/";
        }
    }

    /**
     * 下载文件
     *
     * @param fileStorePath 文件存储的根路径
     * @param subDir        文件存放的子目录，如果子目录为空则文件下载到fileStorePath下
     * @param fileName      文件名称
     * @param url           待下载文件的链接
     */
    public static void downloadFile(String fileStorePath, String subDir, String fileName, String url) {
        HttpGet httpGet = new HttpGet(url.trim());
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000).setSocketTimeout(5000).build();
        httpGet.setConfig(requestConfig);
        //String fileName = getFileNameFromUrl(url);
        fileName += getFileExtensionFromUrl(url);
        log.info("dowloading file {} ", fileName);
        try {
            HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            InputStream is = entity.getContent();
            //拼接存储路径
            String filePath = "";
            if (StringUtils.isEmpty(subDir)) {
                filePath = fileStorePath + splash + fileName;
            } else {
                filePath = fileStorePath + splash + subDir + splash + fileName;
            }
            //创建文件及目录
            File file = new File(filePath);
            //判断父目录是否存在，如果不存在，则创建
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream fileout = new FileOutputStream(file);
            /**
             * 根据实际运行效果 设置缓冲区大小
             */
            byte[] buffer = new byte[cache];
            int ch = 0;
            while ((ch = is.read(buffer)) != -1) {
                fileout.write(buffer, 0, ch);
            }
            is.close();
            fileout.flush();
            fileout.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 下载文件
     *
     * @param fileStorePath 文件存储的根路径
     * @param subDir        文件存放的子目录，如果子目录为空则文件下载到fileStorePath下
     * @param url           待下载文件的链接
     */
    public static void downloadFile(String fileStorePath, String subDir, String url) {
        downloadFile(fileStorePath, subDir, getFileNameFromUrl(url), url);
    }

    private static String getFileNameFromUrl(String url) {
        if (url.trim().endsWith("/")) {
            throw new SpiderException(ExceptionEnum.URL_ILLEGAL.getCode(), ExceptionEnum.URL_ILLEGAL.getDes());
        }
        return url.substring(url.lastIndexOf("/") + 1,url.lastIndexOf("."));
    }

    private static String getFileExtensionFromUrl(String url) {
        if (url.trim().endsWith("/")) {
            throw new SpiderException(ExceptionEnum.URL_ILLEGAL.getCode(), ExceptionEnum.URL_ILLEGAL.getDes());
        }
        return url.substring(url.lastIndexOf("."));
    }

    public static void main(String[] args) {
        //System.out.println(getFileNameFromUrl("http://picshare.static.tuwan.com/picshare/20170623175818_9980.zip"));
        //downloadFile("D:\\data\\tuWan\\image", "猫ソロ", "http://picshare.static.tuwan.com/picshare/20170705193227_711.zip");
        //System.out.println(getFileExtensionFromUrl("http://111.231.221.217:34567/A:/%E5%96%B5%E5%86%99%E7%9C%9F/%E5%96%B5%E5%86%99%E7%9C%9F%20PR15%20001.zip"));
        //downloadFile("D:\\data\\leshe", "", "喵写真PR15001", "http://111.231.221.217:34567/A:/%E5%96%B5%E5%86%99%E7%9C%9F/%E5%96%B5%E5%86%99%E7%9C%9F%20PR15%20001.zip");
        System.out.println(getFileNameFromUrl("https://www.leshe.us/wp-content/uploads/2018/12/ALPHA-020-13.jpg"));
    }

    /**
     * 获取response header中Content-Disposition中的filename值
     *
     * @param response
     * @return
     */
    public static String getFileName(HttpResponse response) {
        Header contentHeader = response.getFirstHeader("Content-Disposition");
        String filename = null;
        if (contentHeader != null) {
            HeaderElement[] values = contentHeader.getElements();
            if (values.length == 1) {
                NameValuePair param = values[0].getParameterByName("filename");
                if (param != null) {
                    try {
                        //filename = new String(param.getValue().toString().getBytes(), "utf-8");
                        //filename=URLDecoder.decode(param.getValue(),"utf-8");
                        filename = param.getValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return filename;
    }
}
