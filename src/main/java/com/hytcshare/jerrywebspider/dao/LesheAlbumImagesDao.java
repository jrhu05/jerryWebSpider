package com.hytcshare.jerrywebspider.dao;

import com.hytcshare.jerrywebspider.entity.LesheAlbumImages;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LesheAlbumImagesDao extends JpaRepository<LesheAlbumImages, Integer> {
    List<LesheAlbumImages> findAllByDownloaded(int downloaded);

    LesheAlbumImages findByTitle(String title);

    LesheAlbumImages findByUrl(String url);
}
