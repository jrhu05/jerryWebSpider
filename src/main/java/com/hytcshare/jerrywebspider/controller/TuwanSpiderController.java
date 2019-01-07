package com.hytcshare.jerrywebspider.controller;

import com.hytcshare.jerrywebspider.service.ErrorLogService;
import com.hytcshare.jerrywebspider.service.SpiderTaskService;
import com.hytcshare.jerrywebspider.service.TuwanImagesService;
import com.hytcshare.jerrywebspider.service.TuwanMp3Service;
import com.hytcshare.jerrywebspider.task.TuwanImageDownloadTask;
import com.hytcshare.jerrywebspider.task.TuwanSpiderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * tuwanSpider控制类
 *
 * @author jerryfu
 */
@RestController
@RequestMapping("/tuwanSpider")
public class TuwanSpiderController extends BaseController {

    @Value("${tuwan.welfareUrl}")
    private String welfareUrl;
    @Value("${tuwan.imageStorePath}")
    private String tuwanImageStorePath;
    @Value("${tuwan.spiderTaskName}")
    private String tuwanSpiderTaskName;
    @Value("${tuwan.downloadTaskName}")
    private String tuwanDownloadTaskName;

    @Autowired
    private TuwanImagesService tuwanImagesService;
    @Autowired
    private TuwanMp3Service tuwanMp3Service;
    @Autowired
    private ErrorLogService errorLogService;
    @Autowired
    private SpiderTaskService spiderTaskService;

    @RequestMapping("/startSpider")
    public DeferredResult startSpider(Integer start, Integer endLine) {
        DeferredResult deferredResult = new DeferredResult();

        if (endLine == null || endLine < 0) {
            sealFail(deferredResult, "endLine非法！");
            return deferredResult;
        }
        if (start == null || start < 0) {
            sealFail(deferredResult, "start非法！");
            return deferredResult;
        }
        if (start > endLine) {
            sealFail(deferredResult, "start非法！");
            return deferredResult;
        }
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        TuwanSpiderTask task = new TuwanSpiderTask();
        task.setTuwanStart(start);
        task.setEndLine(endLine);
        task.setWelfareUrl(welfareUrl);
        task.setTuwanImagesService(tuwanImagesService);
        task.setTuwanMp3Service(tuwanMp3Service);
        task.setSpiderTaskService(spiderTaskService);
        task.setTuwanSpiderTaskName(tuwanSpiderTaskName);
        executorService.execute(task);
        sealSuccess(deferredResult, "success! spider on going!");
        return deferredResult;
    }

    @RequestMapping("/startDownLoadImageZipPackage")
    public DeferredResult startDownLoadImageZipPackage() {
        DeferredResult deferredResult = new DeferredResult();
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        TuwanImageDownloadTask tuwanImageDownloadTask = new TuwanImageDownloadTask();
        tuwanImageDownloadTask.setTuwanImagesService(tuwanImagesService);
        tuwanImageDownloadTask.setErrorLogService(errorLogService);
        tuwanImageDownloadTask.setTuwanImageStorePath(tuwanImageStorePath);
        tuwanImageDownloadTask.setSpiderTaskService(spiderTaskService);
        tuwanImageDownloadTask.setTuwanDownloadTaskName(tuwanDownloadTaskName);
        executorService.execute(tuwanImageDownloadTask);
        sealSuccess(deferredResult, "success! download on going!");
        return deferredResult;
    }


}
