package com.hytcshare.jerrywebspider.task;

import com.hytcshare.jerrywebspider.entity.ErrorLog;
import com.hytcshare.jerrywebspider.entity.TuwanImages;
import com.hytcshare.jerrywebspider.enums.DownloadedStatusEnum;
import com.hytcshare.jerrywebspider.service.ErrorLogService;
import com.hytcshare.jerrywebspider.service.SpiderTaskService;
import com.hytcshare.jerrywebspider.service.TuwanImagesService;
import com.hytcshare.jerrywebspider.utils.DownloadUtils;
import com.hytcshare.jerrywebspider.utils.ExceptionUtils;
import com.hytcshare.jerrywebspider.utils.TaskUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class TuwanImageDownloadTask implements Runnable {
    private TuwanImagesService tuwanImagesService;
    private ErrorLogService errorLogService;
    private SpiderTaskService spiderTaskService;

    private String tuwanImageStorePath;
    private String tuwanDownloadTaskName;

    public void setSpiderTaskService(SpiderTaskService spiderTaskService) {
        this.spiderTaskService = spiderTaskService;
    }

    public void setTuwanDownloadTaskName(String tuwanDownloadTaskName) {
        this.tuwanDownloadTaskName = tuwanDownloadTaskName;
    }

    public void setTuwanImagesService(TuwanImagesService tuwanImagesService) {
        this.tuwanImagesService = tuwanImagesService;
    }

    public void setErrorLogService(ErrorLogService errorLogService) {
        this.errorLogService = errorLogService;
    }

    public void setTuwanImageStorePath(String tuwanImageStorePath) {
        this.tuwanImageStorePath = tuwanImageStorePath;
    }

    @Override
    public void run() {
        log.info("start download tuwan image zip package!");
        TaskUtils.startTask(spiderTaskService, tuwanDownloadTaskName);
        //获取待下载列表
        List<TuwanImages> notDownloadedList = tuwanImagesService.getNotDownloadedList();
        //对无效链接进行过滤
        List<TuwanImages> validImages = wipeOutUselessImage(notDownloadedList);
        //遍历下载
        for (TuwanImages image : notDownloadedList) {
            try {
                //下载图包
                DownloadUtils.downloadFile(tuwanImageStorePath, image.getId() + image.getTitle(), image.getUrl());
                //下载封面图
                DownloadUtils.downloadFile(tuwanImageStorePath, image.getId() + image.getTitle(), image.getCover());
                //更新已下载记录
                image.setDownloaded(DownloadedStatusEnum.DOWNLOADED.getCode());
                tuwanImagesService.insertOrUpdate(image);
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
        log.info("download tuwan image zip package finish!");
        //更新任务状态为执行完毕
        TaskUtils.shutdownTask(spiderTaskService, tuwanDownloadTaskName);
    }

    private List<TuwanImages> wipeOutUselessImage(List<TuwanImages> notDownloadedList) {
        List<TuwanImages> validImages = new ArrayList<>();
        for (TuwanImages image : notDownloadedList) {
            if (StringUtils.isEmpty(image.getUrl()) || !image.getUrl().trim().endsWith("zip")) {
                //从数据库中删除无效记录
                tuwanImagesService.delete(image);
            } else {
                validImages.add(image);
            }
        }
        return validImages;
    }
}
