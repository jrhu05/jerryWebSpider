package com.hytcshare.jerrywebspider.service;

import com.hytcshare.jerrywebspider.dao.SpiderTaskDao;
import com.hytcshare.jerrywebspider.entity.SpiderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpiderTaskService {
    @Autowired
    private SpiderTaskDao spiderTaskDao;

    public SpiderTask getTaskByName(String taskName) {
        return spiderTaskDao.getByTaskName(taskName);
    }

    public void saveOrUpdate(SpiderTask spiderTask) {
        spiderTaskDao.save(spiderTask);
    }
}
