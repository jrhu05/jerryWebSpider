package com.hytcshare.jerrywebspider.controller;

import com.hytcshare.jerrywebspider.service.ErrorLogService;
import com.hytcshare.jerrywebspider.service.LesheAlbumImagesService;
import com.hytcshare.jerrywebspider.service.SpiderTaskService;
import com.hytcshare.jerrywebspider.task.LesheAlbumImageDownloadTask;
import com.hytcshare.jerrywebspider.task.LesheAlbumSpiderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * lesheSpiderAlbum控制类
 *
 * @author jerryfu
 */
@RestController
@RequestMapping("/lesheAlbumSpider")
public class lesheAlbumSpiderController extends BaseController {
    @Value("${leshe.album.welfareUrl}")
    private String welfareUrl;
    @Value("${leshe.album.spiderTaskName}")
    private String spiderTaskName;
    @Value("${leshe.album.downloadTaskName}")
    private String downloadTaskName;
    @Value("${leshe.album.imageStorePath}")
    private String imageStorePath;

    @Autowired
    private LesheAlbumImagesService lesheAlbumImagesService;
    @Autowired
    private SpiderTaskService spiderTaskService;
    @Autowired
    private ErrorLogService errorLogService;

    @RequestMapping("/startSpider")
    public DeferredResult startSpider() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        LesheAlbumSpiderTask task = new LesheAlbumSpiderTask();
        task.setLesheAlbumImagesService(lesheAlbumImagesService);
        task.setSpiderTaskName(spiderTaskName);
        task.setSpiderTaskService(spiderTaskService);
        task.setWelfareUrl(welfareUrl);
        executorService.execute(task);
        DeferredResult deferredResult = new DeferredResult();
        sealSuccess(deferredResult, "success! spider on going!");
        return deferredResult;
    }

    @RequestMapping("/startDownLoadImage")
    public DeferredResult startDownLoadImageZipPackage() {
        DeferredResult deferredResult = new DeferredResult();
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        LesheAlbumImageDownloadTask lesheAlbumImageDownloadTask = new LesheAlbumImageDownloadTask();
        lesheAlbumImageDownloadTask.setErrorLogService(errorLogService);
        lesheAlbumImageDownloadTask.setLesheAlbumDownloadTaskName(downloadTaskName);
        lesheAlbumImageDownloadTask.setLesheAlbumImagesService(lesheAlbumImagesService);
        lesheAlbumImageDownloadTask.setLesheAlbumImageStorePath(imageStorePath);
        lesheAlbumImageDownloadTask.setSpiderTaskService(spiderTaskService);
        executorService.execute(lesheAlbumImageDownloadTask);
        sealSuccess(deferredResult, "success! download on going!");
        return deferredResult;
    }
}
