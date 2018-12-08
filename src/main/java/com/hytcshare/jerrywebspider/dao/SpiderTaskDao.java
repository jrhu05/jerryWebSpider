package com.hytcshare.jerrywebspider.dao;

import com.hytcshare.jerrywebspider.entity.SpiderTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpiderTaskDao extends JpaRepository<SpiderTask, Integer> {
    SpiderTask getByTaskName(String taskName);
}
