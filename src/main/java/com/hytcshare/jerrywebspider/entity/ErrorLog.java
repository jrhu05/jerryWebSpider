package com.hytcshare.jerrywebspider.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.util.Date;

/**
 * @author jerryfu
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name = "error_log")
public class ErrorLog {
    @Id
    @GeneratedValue
    private int id;
    private String creator;
    private Date logTime;
    private String errorMessage;
    private String stackDump;
}
