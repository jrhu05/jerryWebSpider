package com.hytcshare.jerrywebspider.service;

import com.hytcshare.jerrywebspider.dao.LesheImagesDao;
import com.hytcshare.jerrywebspider.entity.LesheImages;
import com.hytcshare.jerrywebspider.enums.DownloadedStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LesheImagesService {
    @Autowired
    private LesheImagesDao lesheImagesDao;

    public void insertOrUpdate(LesheImages lesheImages) {
        lesheImagesDao.save(lesheImages);
    }

    public List<LesheImages> getNotDownloadedList() {
        return lesheImagesDao.findAllByDownloaded(DownloadedStatusEnum.NOT_DOWNLOADED.getCode());
    }

    public void delete(LesheImages lesheImages) {
        lesheImagesDao.delete(lesheImages);
    }

    public LesheImages findByTitle(String title) {
        return lesheImagesDao.findByTitle(title);
    }
}
