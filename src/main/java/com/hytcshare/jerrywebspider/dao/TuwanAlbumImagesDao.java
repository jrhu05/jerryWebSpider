package com.hytcshare.jerrywebspider.dao;

import com.hytcshare.jerrywebspider.entity.TuwanAlbumImages;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TuwanAlbumImagesDao extends JpaRepository<TuwanAlbumImages, Integer> {
    public TuwanAlbumImages findByUrl(String url);

    public List<TuwanAlbumImages> findAllByDownloaded(int downloaded);

    public List<TuwanAlbumImages> findAllByDownloadedAndAlbumIndex(int downloaded,int albumIndex);

    public List<TuwanAlbumImages> findAllByTitle(String title);
}
