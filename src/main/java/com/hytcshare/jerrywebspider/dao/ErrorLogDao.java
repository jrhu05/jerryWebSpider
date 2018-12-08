package com.hytcshare.jerrywebspider.dao;

import com.hytcshare.jerrywebspider.entity.ErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorLogDao extends JpaRepository<ErrorLog, Integer> {
}
