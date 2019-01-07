package com.hytcshare.jerrywebspider.task;

import com.hytcshare.jerrywebspider.entity.ErrorLog;
import com.hytcshare.jerrywebspider.entity.TuwanAlbumImages;
import com.hytcshare.jerrywebspider.enums.DownloadedStatusEnum;
import com.hytcshare.jerrywebspider.service.ErrorLogService;
import com.hytcshare.jerrywebspider.service.SpiderTaskService;
import com.hytcshare.jerrywebspider.service.TuwanAlbumImagesService;
import com.hytcshare.jerrywebspider.utils.DownloadUtils;
import com.hytcshare.jerrywebspider.utils.ExceptionUtils;
import com.hytcshare.jerrywebspider.utils.TaskUtils;
import lombok.extern.slf4j.Slf4j;
import java.util.Date;
import java.util.List;

@Slf4j
public class TuwanAlbumImageDownloadTask implements Runnable {

    private TuwanAlbumImagesService tuwanAlbumImagesService;
    private ErrorLogService errorLogService;
    private SpiderTaskService spiderTaskService;

    private String tuwanAlbumImageStorePath;
    private String tuwanAlbumDownloadTaskName;

    public void setTuwanAlbumImagesService(TuwanAlbumImagesService tuwanAlbumImagesService) {
        this.tuwanAlbumImagesService = tuwanAlbumImagesService;
    }

    public void setErrorLogService(ErrorLogService errorLogService) {
        this.errorLogService = errorLogService;
    }

    public void setSpiderTaskService(SpiderTaskService spiderTaskService) {
        this.spiderTaskService = spiderTaskService;
    }

    public void setTuwanAlbumImageStorePath(String tuwanAlbumImageStorePath) {
        this.tuwanAlbumImageStorePath = tuwanAlbumImageStorePath;
    }

    public void setTuwanAlbumDownloadTaskName(String tuwanAlbumDownloadTaskName) {
        this.tuwanAlbumDownloadTaskName = tuwanAlbumDownloadTaskName;
    }

    @Override
    public void run() {
        log.info("start download tuwan album images!");
        TaskUtils.startTask(spiderTaskService, tuwanAlbumDownloadTaskName);
        //获取待下载列表
        List<TuwanAlbumImages> notDownloadedList = tuwanAlbumImagesService.getNotDownloadedList();
        //遍历下载
        for (TuwanAlbumImages image : notDownloadedList) {
            try {
                //下载图片
                DownloadUtils.downloadFile(tuwanAlbumImageStorePath, "", image.getUrl());
                //更新已下载记录
                image.setDownloaded(DownloadedStatusEnum.DOWNLOADED.getCode());
                tuwanAlbumImagesService.insertOrUpdate(image);
            } catch (Exception e) {
                //记录错误日志
                ErrorLog errorLog = new ErrorLog();
                errorLog.setCreator(this.getClass().getName());
                errorLog.setErrorMessage("图片文件下载失败！index: " + image.getId());
                errorLog.setLogTime(new Date());
                errorLog.setStackDump(ExceptionUtils.getStackTrace(e));
                errorLogService.insertOrUpdate(errorLog);
            }
        }
        log.info("download leshe album images finish!");
        //更新任务状态为执行完毕
        TaskUtils.shutdownTask(spiderTaskService, tuwanAlbumDownloadTaskName);
    }
}
