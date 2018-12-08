package com.hytcshare.jerrywebspider.service;

import com.hytcshare.jerrywebspider.dao.ErrorLogDao;
import com.hytcshare.jerrywebspider.entity.ErrorLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ErrorLogService {
    @Autowired
    private ErrorLogDao errorLogDao;

    public void insertOrUpdate(ErrorLog errorLog) {
        errorLogDao.save(errorLog);
    }
}
