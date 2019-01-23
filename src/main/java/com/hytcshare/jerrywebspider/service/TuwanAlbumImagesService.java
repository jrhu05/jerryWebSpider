package com.hytcshare.jerrywebspider.service;

import com.hytcshare.jerrywebspider.dao.TuwanAlbumImagesDao;
import com.hytcshare.jerrywebspider.entity.TuwanAlbumImages;
import com.hytcshare.jerrywebspider.enums.DownloadedStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TuwanAlbumImagesService {
    @Autowired
    private TuwanAlbumImagesDao tuwanAlbumImagesDao;

    public void insertOrUpdate(TuwanAlbumImages tuwanAlbumImages) {
        tuwanAlbumImagesDao.save(tuwanAlbumImages);
    }

    public TuwanAlbumImages fingByUrl(String imgUrl) {
        return tuwanAlbumImagesDao.findByUrl(imgUrl);
    }

    public int countByTitle(String title){
        List<TuwanAlbumImages> allByTitle = tuwanAlbumImagesDao.findAllByTitle(title);
        if (allByTitle == null){
            return 0;
        }
        return allByTitle.size();
    }

    public List<TuwanAlbumImages> getNotDownloadedList() {
        return tuwanAlbumImagesDao.findAllByDownloaded(DownloadedStatusEnum.NOT_DOWNLOADED.getCode());
    }

    public List<TuwanAlbumImages> getNotDownloadedListByIndex(int index) {
        return tuwanAlbumImagesDao.findAllByDownloadedAndAlbumIndex(DownloadedStatusEnum.NOT_DOWNLOADED.getCode(),index);
    }
}
