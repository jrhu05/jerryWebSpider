package com.hytcshare.jerrywebspider.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;

/**
 * leshe相册图片信息类
 *
 * @author jerryfu
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name = "leshe_album_images")
public class LesheAlbumImages {
    @Id
    @GeneratedValue
    private int id;
    /**
     * 图包名称
     */
    private String title;
    /**
     * 图片下载地址
     */
    private String url;
    /**
     * 是否已下载：0未下载，1已下载
     */
    private int downloaded;
}
