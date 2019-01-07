package com.hytcshare.jerrywebspider.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;

/**
 * leshe图片信息类
 *
 * @author jerryfu
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name = "leshe_images")
public class LesheImages {
    @Id
    @GeneratedValue
    private int id;
    /**
     * 图包名称
     */
    private String title;
    /**
     * 压缩包下载地址
     */
    private String url;
    /**
     * 是否已下载：0未下载，1已下载
     */
    private int downloaded;
}
