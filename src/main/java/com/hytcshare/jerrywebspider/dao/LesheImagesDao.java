package com.hytcshare.jerrywebspider.dao;

import com.hytcshare.jerrywebspider.entity.LesheImages;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LesheImagesDao extends JpaRepository<LesheImages, Integer> {
    List<LesheImages> findAllByDownloaded(int downloaded);

    LesheImages findByTitle(String title);
}
