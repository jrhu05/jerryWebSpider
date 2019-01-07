package com.hytcshare.jerrywebspider.task;


import com.hytcshare.jerrywebspider.pageprocessor.LesheAlbumPageProcessor;
import com.hytcshare.jerrywebspider.pipeline.LesheAlbumMysqlPipeline;
import com.hytcshare.jerrywebspider.service.LesheAlbumImagesService;
import com.hytcshare.jerrywebspider.service.SpiderTaskService;
import com.hytcshare.jerrywebspider.utils.TaskUtils;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

@Slf4j
public class LesheAlbumSpiderTask implements Runnable {
    private String welfareUrl;
    private LesheAlbumImagesService LesheAlbumImagesService;
    private SpiderTaskService spiderTaskService;
    private String spiderTaskName;

    public void setWelfareUrl(String welfareUrl) {
        this.welfareUrl = welfareUrl;
    }

    public void setLesheAlbumImagesService(LesheAlbumImagesService lesheAlbumImagesService) {
        LesheAlbumImagesService = lesheAlbumImagesService;
    }

    public void setSpiderTaskService(SpiderTaskService spiderTaskService) {
        this.spiderTaskService = spiderTaskService;
    }

    public void setSpiderTaskName(String spiderTaskName) {
        this.spiderTaskName = spiderTaskName;
    }

    @Override
    public void run() {
        log.info("leshe album spider start！");
        TaskUtils.startTask(spiderTaskService, spiderTaskName);
        OOSpider.create(new LesheAlbumPageProcessor())
                .addUrl(welfareUrl)
                .addPipeline(new ConsolePipeline())
                .addPipeline(new LesheAlbumMysqlPipeline(LesheAlbumImagesService))
                .thread(5).run();
        TaskUtils.shutdownTask(spiderTaskService, spiderTaskName);
    }
}
