package com.hytcshare.jerrywebspider.service;

import com.hytcshare.jerrywebspider.dao.TuwanMp3Dao;
import com.hytcshare.jerrywebspider.entity.TuwanMp3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TuwanMp3Service {
    @Autowired
    private TuwanMp3Dao tuwanMp3Dao;

    public void insertOrUpdate(TuwanMp3 tuwanMp3) {
        tuwanMp3Dao.save(tuwanMp3);
    }
}
