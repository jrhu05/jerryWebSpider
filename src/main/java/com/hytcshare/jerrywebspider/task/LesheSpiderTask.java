package com.hytcshare.jerrywebspider.task;

import com.hytcshare.jerrywebspider.enums.ExceptionEnum;
import com.hytcshare.jerrywebspider.exception.SpiderException;
import com.hytcshare.jerrywebspider.pageprocessor.LeshePageProcessor;
import com.hytcshare.jerrywebspider.pipeline.LesheMysqlPipeline;
import com.hytcshare.jerrywebspider.service.LesheImagesService;
import com.hytcshare.jerrywebspider.service.SpiderTaskService;
import com.hytcshare.jerrywebspider.utils.TaskUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
public class LesheSpiderTask implements Runnable {
    private String welfareUrl;
    private String collectionName;
    private LesheImagesService lesheImagesService;
    private SpiderTaskService spiderTaskService;
    private String spiderTaskName;

    public void setWelfareUrl(String welfareUrl) {
        this.welfareUrl = welfareUrl;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public void setLesheImagesService(LesheImagesService lesheImagesService) {
        this.lesheImagesService = lesheImagesService;
    }

    public void setSpiderTaskService(SpiderTaskService spiderTaskService) {
        this.spiderTaskService = spiderTaskService;
    }

    public void setSpiderTaskName(String spiderTaskName) {
        this.spiderTaskName = spiderTaskName;
    }

    @Override
    public void run() {
        log.info("leshe spider start！");
        TaskUtils.startTask(spiderTaskService, spiderTaskName);
        if (StringUtils.isEmpty(collectionName)) {
            throw new SpiderException(ExceptionEnum.COLLECTION_NAME_ILLEGAL.getCode(), ExceptionEnum.COLLECTION_NAME_ILLEGAL.getDes());
        }
        String[] albums = collectionName.split(",");
        for (String album : albums) {
            String encode = "";
            try {
                encode = URLEncoder.encode(album, "utf8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                continue;
            }
            OOSpider.create(new LeshePageProcessor())
                    .addUrl(welfareUrl + encode + "?sortby=lastModtime&order=desc&page=1")
                    .addPipeline(new ConsolePipeline())
                    .addPipeline(new LesheMysqlPipeline(lesheImagesService))
                    .thread(5).run();
        }
        TaskUtils.shutdownTask(spiderTaskService, spiderTaskName);
    }
}
