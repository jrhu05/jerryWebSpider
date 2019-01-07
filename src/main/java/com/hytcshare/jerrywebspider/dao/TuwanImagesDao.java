package com.hytcshare.jerrywebspider.dao;

import com.hytcshare.jerrywebspider.entity.TuwanImages;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TuwanImagesDao extends JpaRepository<TuwanImages, Integer> {
    List<TuwanImages> findAllByDownloaded(int downloaded);
}
