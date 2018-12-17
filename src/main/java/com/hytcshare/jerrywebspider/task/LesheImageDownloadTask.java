package com.hytcshare.jerrywebspider.task;

import com.hytcshare.jerrywebspider.entity.ErrorLog;
import com.hytcshare.jerrywebspider.entity.LesheImages;
import com.hytcshare.jerrywebspider.entity.TuwanImages;
import com.hytcshare.jerrywebspider.enums.DownloadedStatusEnum;
import com.hytcshare.jerrywebspider.service.ErrorLogService;
import com.hytcshare.jerrywebspider.service.LesheImagesService;
import com.hytcshare.jerrywebspider.service.SpiderTaskService;
import com.hytcshare.jerrywebspider.utils.DownloadUtils;
import com.hytcshare.jerrywebspider.utils.ExceptionUtils;
import com.hytcshare.jerrywebspider.utils.TaskUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

@Slf4j
public class LesheImageDownloadTask implements Runnable {
    private LesheImagesService lesheImagesService;
    private ErrorLogService errorLogService;
    private SpiderTaskService spiderTaskService;

    private String lesheImageStorePath;
    private String lesheDownloadTaskName;

    public void setLesheImagesService(LesheImagesService lesheImagesService) {
        this.lesheImagesService = lesheImagesService;
    }

    public void setErrorLogService(ErrorLogService errorLogService) {
        this.errorLogService = errorLogService;
    }

    public void setSpiderTaskService(SpiderTaskService spiderTaskService) {
        this.spiderTaskService = spiderTaskService;
    }

    public void setLesheImageStorePath(String lesheImageStorePath) {
        this.lesheImageStorePath = lesheImageStorePath;
    }

    public void setLesheDownloadTaskName(String lesheDownloadTaskName) {
        this.lesheDownloadTaskName = lesheDownloadTaskName;
    }

    @Override
    public void run() {
        log.info("start download leshe image zip package!");
        TaskUtils.startTask(spiderTaskService, lesheDownloadTaskName);
        //获取待下载列表
        List<LesheImages> notDownloadedList = lesheImagesService.getNotDownloadedList();
        //遍历下载
        for (LesheImages image : notDownloadedList) {
            try {
                //下载图包
                DownloadUtils.downloadFile(lesheImageStorePath, "", image.getTitle(), image.getUrl());
                //更新已下载记录
                image.setDownloaded(DownloadedStatusEnum.DOWNLOADED.getCode());
                lesheImagesService.insertOrUpdate(image);
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
        log.info("download leshe image zip package finish!");
        //更新任务状态为执行完毕
        TaskUtils.shutdownTask(spiderTaskService, lesheDownloadTaskName);

    }
}
