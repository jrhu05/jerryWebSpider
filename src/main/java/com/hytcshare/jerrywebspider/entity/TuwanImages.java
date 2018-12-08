package com.hytcshare.jerrywebspider.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * tuwan图片信息类
 *
 * @author jerryfu
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name = "tuwan_images")
public class TuwanImages {
    @Id
    private int id;
    private String title;
    /**
     * 封面图片地址（示例图）
     */
    private String cover;
    /**
     * 压缩包下载地址
     */
    private String url;
    /**
     * 该图包内的图片总数
     */
    private int total;
    private int downloaded;
}
