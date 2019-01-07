package com.hytcshare.jerrywebspider.task;

import com.hytcshare.jerrywebspider.entity.ErrorLog;
import com.hytcshare.jerrywebspider.entity.LesheAlbumImages;
import com.hytcshare.jerrywebspider.enums.DownloadedStatusEnum;
import com.hytcshare.jerrywebspider.service.ErrorLogService;
import com.hytcshare.jerrywebspider.service.LesheAlbumImagesService;
import com.hytcshare.jerrywebspider.service.SpiderTaskService;
import com.hytcshare.jerrywebspider.utils.DownloadUtils;
import com.hytcshare.jerrywebspider.utils.ExceptionUtils;
import com.hytcshare.jerrywebspider.utils.TaskUtils;
import lombok.extern.slf4j.Slf4j;
import java.util.Date;
import java.util.List;

@Slf4j
public class LesheAlbumImageDownloadTask implements Runnable {
    private LesheAlbumImagesService lesheAlbumImagesService;
    private ErrorLogService errorLogService;
    private SpiderTaskService spiderTaskService;

    private String lesheAlbumImageStorePath;
    private String lesheAlbumDownloadTaskName;

    public void setErrorLogService(ErrorLogService errorLogService) {
        this.errorLogService = errorLogService;
    }

    public void setSpiderTaskService(SpiderTaskService spiderTaskService) {
        this.spiderTaskService = spiderTaskService;
    }

    public void setLesheAlbumImagesService(LesheAlbumImagesService lesheAlbumImagesService) {
        this.lesheAlbumImagesService = lesheAlbumImagesService;
    }

    public void setLesheAlbumImageStorePath(String lesheAlbumImageStorePath) {
        this.lesheAlbumImageStorePath = lesheAlbumImageStorePath;
    }

    public void setLesheAlbumDownloadTaskName(String lesheAlbumDownloadTaskName) {
        this.lesheAlbumDownloadTaskName = lesheAlbumDownloadTaskName;
    }

    @Override
    public void run() {
        log.info("start download leshe album images!");
        TaskUtils.startTask(spiderTaskService, lesheAlbumDownloadTaskName);
        //获取待下载列表
        List<LesheAlbumImages> notDownloadedList = lesheAlbumImagesService.getNotDownloadedList();
        //遍历下载
        for (LesheAlbumImages image : notDownloadedList) {
            try {
                //下载图片
                DownloadUtils.downloadFile(lesheAlbumImageStorePath, "", image.getUrl());
                //更新已下载记录
                image.setDownloaded(DownloadedStatusEnum.DOWNLOADED.getCode());
                lesheAlbumImagesService.insertOrUpdate(image);
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
        TaskUtils.shutdownTask(spiderTaskService, lesheAlbumDownloadTaskName);

    }
}
