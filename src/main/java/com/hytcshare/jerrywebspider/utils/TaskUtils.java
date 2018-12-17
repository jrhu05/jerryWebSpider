package com.hytcshare.jerrywebspider.utils;

import com.hytcshare.jerrywebspider.entity.SpiderTask;
import com.hytcshare.jerrywebspider.enums.ExceptionEnum;
import com.hytcshare.jerrywebspider.enums.SpiderTaskStatusEnum;
import com.hytcshare.jerrywebspider.exception.SpiderException;
import com.hytcshare.jerrywebspider.service.SpiderTaskService;
import org.springframework.util.StringUtils;

public final class TaskUtils {
    public static void startTask(SpiderTaskService spiderTaskService, String spiderTaskName) {
        SpiderTask spiderTask = spiderTaskService.getTaskByName(spiderTaskName);
        if (spiderTask == null || StringUtils.isEmpty(spiderTask.getTaskName())) {
            //建立新任务
            spiderTask = new SpiderTask();
            spiderTask.setTaskName(spiderTaskName);
            spiderTask.setStatus(SpiderTaskStatusEnum.ONGOING.getCode());
            spiderTaskService.saveOrUpdate(spiderTask);
        } else if (spiderTask.getStatus() == SpiderTaskStatusEnum.ONGOING.getCode()) {
            //已经有在执行的任务
            throw new SpiderException(ExceptionEnum.TASK_ALREADY_ONGOING.getCode(), ExceptionEnum.TASK_ALREADY_ONGOING.getDes());
        }
        //置为正在执行
        spiderTask.setStatus(SpiderTaskStatusEnum.ONGOING.getCode());
        spiderTaskService.saveOrUpdate(spiderTask);
    }

    public static void shutdownTask(SpiderTaskService spiderTaskService, String spiderTaskName) {
        SpiderTask spiderTask = spiderTaskService.getTaskByName(spiderTaskName);
        spiderTask.setStatus(SpiderTaskStatusEnum.SHUTDOWN.getCode());
        spiderTaskService.saveOrUpdate(spiderTask);
    }
}
