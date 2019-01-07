package com.hytcshare.jerrywebspider.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;

/**
 * spider信息类
 *
 * @author jerryfu
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name = "spider_task")
public class SpiderTask {
    @Id
    @GeneratedValue
    private int id;
    private String taskName;
    private String successCount;
    private int status;
}
