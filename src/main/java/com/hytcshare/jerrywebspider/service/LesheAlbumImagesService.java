package com.hytcshare.jerrywebspider.service;

import com.hytcshare.jerrywebspider.dao.LesheAlbumImagesDao;
import com.hytcshare.jerrywebspider.entity.LesheAlbumImages;
import com.hytcshare.jerrywebspider.enums.DownloadedStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LesheAlbumImagesService {
    @Autowired
    private LesheAlbumImagesDao lesheAlbumImagesDao;

    public void insertOrUpdate(LesheAlbumImages lesheImages) {
        lesheAlbumImagesDao.save(lesheImages);
    }

    public List<LesheAlbumImages> getNotDownloadedList() {
        return lesheAlbumImagesDao.findAllByDownloaded(DownloadedStatusEnum.NOT_DOWNLOADED.getCode());
    }

    public void delete(LesheAlbumImages lesheImages) {
        lesheAlbumImagesDao.delete(lesheImages);
    }

    public LesheAlbumImages findByTitle(String title) {
        return lesheAlbumImagesDao.findByTitle(title);
    }

    public LesheAlbumImages fingByUrl(String imgUrl) {
        return lesheAlbumImagesDao.findByUrl(imgUrl);
    }
}
