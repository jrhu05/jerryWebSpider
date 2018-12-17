package com.hytcshare.jerrywebspider.controller;

import com.hytcshare.jerrywebspider.service.ErrorLogService;
import com.hytcshare.jerrywebspider.service.LesheImagesService;
import com.hytcshare.jerrywebspider.service.SpiderTaskService;
import com.hytcshare.jerrywebspider.task.LesheImageDownloadTask;
import com.hytcshare.jerrywebspider.task.LesheSpiderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * lesheSpider控制类
 *
 * @author jerryfu
 */
@RestController
@RequestMapping("/lesheSpider")
public class lesheSpiderController extends BaseController {
    @Value("${leshe.welfareUrl}")
    private String welfareUrl;
    @Value("${leshe.collectionName}")
    private String collectionName;
    @Value("${leshe.spiderTaskName}")
    private String spiderTaskName;
    @Value("${leshe.downloadTaskName}")
    private String downloadTaskName;
    @Value("${leshe.imageStorePath}")
    private String imageStorePath;

    @Autowired
    private LesheImagesService lesheImagesService;
    @Autowired
    private SpiderTaskService spiderTaskService;
    @Autowired
    private ErrorLogService errorLogService;

    @RequestMapping("/startSpider")
    public DeferredResult startSpider() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        LesheSpiderTask task = new LesheSpiderTask();
        task.setCollectionName(collectionName);
        task.setLesheImagesService(lesheImagesService);
        task.setSpiderTaskName(spiderTaskName);
        task.setSpiderTaskService(spiderTaskService);
        task.setWelfareUrl(welfareUrl);
        executorService.execute(task);
        DeferredResult deferredResult = new DeferredResult();
        sealSuccess(deferredResult, "success! spider on going!");
        return deferredResult;
    }

    @RequestMapping("/startDownLoadImageZipPackage")
    public DeferredResult startDownLoadImageZipPackage() {
        DeferredResult deferredResult = new DeferredResult();
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        LesheImageDownloadTask lesheImageDownloadTask = new LesheImageDownloadTask();
        lesheImageDownloadTask.setErrorLogService(errorLogService);
        lesheImageDownloadTask.setLesheDownloadTaskName(downloadTaskName);
        lesheImageDownloadTask.setLesheImagesService(lesheImagesService);
        lesheImageDownloadTask.setLesheImageStorePath(imageStorePath);
        lesheImageDownloadTask.setSpiderTaskService(spiderTaskService);
        executorService.execute(lesheImageDownloadTask);
        sealSuccess(deferredResult, "success! download on going!");
        return deferredResult;
    }
}
