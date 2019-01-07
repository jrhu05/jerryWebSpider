package com.hytcshare.jerrywebspider.pipeline;

import com.hytcshare.jerrywebspider.entity.LesheImages;
import com.hytcshare.jerrywebspider.enums.DownloadedStatusEnum;
import com.hytcshare.jerrywebspider.service.LesheImagesService;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class LesheMysqlPipeline implements Pipeline {

    private LesheImagesService lesheImagesService;

    public LesheMysqlPipeline(LesheImagesService lesheImagesService) {
        this.lesheImagesService = lesheImagesService;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<String> zipList = resultItems.get("zipList");
        for (String zipUrl : zipList) {
            if (StringUtils.isEmpty(zipUrl)) {
                continue;
            }
            //解析文件名称
            //http://111.231.221.217:34567/A:/%E9%A3%8E%E4%B9%8B%E9%A2%86%E5%9F%9F/%E9%A3%8E%E4%B9%8B%E9%A2%86%E5%9F%9F%200062.zip
            String zipName = zipUrl.substring(zipUrl.lastIndexOf("/") + 1, zipUrl.lastIndexOf("."));
            try {
                zipName = URLDecoder.decode(zipName, "utf8").replace(" ", "").trim();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            LesheImages image = lesheImagesService.findByTitle(zipName);
            if (image == null || StringUtils.isEmpty(image.getTitle())) {
                image = new LesheImages();
            }
            image.setTitle(zipName);
            image.setUrl(zipUrl);
            image.setDownloaded(DownloadedStatusEnum.NOT_DOWNLOADED.getCode());
            lesheImagesService.insertOrUpdate(image);
        }
    }

    public static void main(String[] args) {
        String zipUrl = "http://111.231.221.217:34567/A:/%E9%A3%8E%E4%B9%8B%E9%A2%86%E5%9F%9F/%E9%A3%8E%E4%B9%8B%E9%A2%86%E5%9F%9F%200062.zip";
        String zipName = zipUrl.substring(zipUrl.lastIndexOf("/") + 1, zipUrl.lastIndexOf("."));
        try {
            zipName = URLDecoder.decode(zipName, "utf8").replace(" ", "").trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(zipName);
    }
}
