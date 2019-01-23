package com.hytcshare.jerrywebspider.controller;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.hytcshare.jerrywebspider.service.ErrorLogService;
import com.hytcshare.jerrywebspider.service.SpiderTaskService;
import com.hytcshare.jerrywebspider.service.TuwanAlbumImagesService;
import com.hytcshare.jerrywebspider.task.TuwanAlbumImageDownloadTask;
import com.hytcshare.jerrywebspider.task.TuwanAlbumSpiderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import java.util.concurrent.*;

/**
 * tuwanAlbum控制类
 *
 * @author jerryfu
 */
@RestController
@RequestMapping("/tuwanAlbumSpider")
public class TuwanAlbumSpiderController extends BaseController {
    @Value("${tuwan.album.welfareUrl}")
    private String welfareUrl;
    @Value("${tuwan.album.spiderTaskName}")
    private String spiderTaskName;
    @Value("${tuwan.album.downloadTaskName}")
    private String downloadTaskName;
    @Value("${tuwan.album.imageStorePath}")
    private String imageStorePath;

    @Autowired
    private TuwanAlbumImagesService tuwanAlbumImagesService;
    @Autowired
    private SpiderTaskService spiderTaskService;
    @Autowired
    private ErrorLogService errorLogService;

    @RequestMapping("/startSpider")
    public DeferredResult startSpider(Integer start, Integer end) {
        DeferredResult deferredResult = new DeferredResult();

        if (startEndCheck(start, end, deferredResult)) {
            return deferredResult;
        }
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("tuwanAlbumSpider-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        TuwanAlbumSpiderTask task = new TuwanAlbumSpiderTask();
        task.setTuwanStart(start);
        task.setEndLine(end);
        task.setWelfareUrl(welfareUrl);
        task.setSpiderTaskService(spiderTaskService);
        task.setTuwanAlbumSpiderTaskName(spiderTaskName);
        task.setTuwanAlbumImagesService(tuwanAlbumImagesService);
        singleThreadPool.execute(task);
        singleThreadPool.shutdown();
        sealSuccess(deferredResult, "success! spider on going!");
        return deferredResult;
    }

    private boolean startEndCheck(Integer start, Integer endLine, DeferredResult deferredResult) {
        if (endLine == null || endLine < 0) {
            sealFail(deferredResult, "end非法！");
            return true;
        }
        if (start == null || start < 0) {
            sealFail(deferredResult, "start非法！");
            return true;
        }
        if (start > endLine) {
            sealFail(deferredResult, "start非法！");
            return true;
        }
        return false;
    }

    @RequestMapping("/startDownLoadImage")
    public DeferredResult startDownLoadImageZipPackage(Integer start, Integer end) {
        DeferredResult deferredResult = new DeferredResult();
        if (startEndCheck(start, end, deferredResult)) {
            return deferredResult;
        }
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("tuwanAlbumDownloader-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        TuwanAlbumImageDownloadTask task = new TuwanAlbumImageDownloadTask();
        task.setErrorLogService(errorLogService);
        task.setSpiderTaskService(spiderTaskService);
        task.setTuwanAlbumDownloadTaskName(downloadTaskName);
        task.setTuwanAlbumImagesService(tuwanAlbumImagesService);
        task.setTuwanAlbumImageStorePath(imageStorePath);
        task.setDownloadStartId(start);
        task.setDownloadEndId(end);
        singleThreadPool.execute(task);
        singleThreadPool.shutdown();
        sealSuccess(deferredResult, "success! download on going!");
        return deferredResult;
    }
}
