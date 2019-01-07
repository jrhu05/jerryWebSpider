package com.hytcshare.jerrywebspider.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;

/**
 * tuwanMp3信息类
 *
 * @author jerryfu
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name = "tuwan_mp3")
public class TuwanMp3 {
    @Id
    private int id;
    private String bgmName;
    private String bgm;
    private int downloaded;
}
