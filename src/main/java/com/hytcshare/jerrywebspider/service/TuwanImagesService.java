package com.hytcshare.jerrywebspider.service;

import com.hytcshare.jerrywebspider.dao.TuwanImagesDao;
import com.hytcshare.jerrywebspider.entity.TuwanImages;
import com.hytcshare.jerrywebspider.enums.DownloadedStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TuwanImagesService {
    @Autowired
    private TuwanImagesDao tuwanImagesDao;

    public void insertOrUpdate(TuwanImages tuwanImages) {
        tuwanImagesDao.save(tuwanImages);
    }

    public List<TuwanImages> getNotDownloadedList() {
        return tuwanImagesDao.findAllByDownloaded(DownloadedStatusEnum.NOT_DOWNLOADED.getCode());
    }

    public void delete(TuwanImages tuwanImages) {
        tuwanImagesDao.delete(tuwanImages);
    }
}
