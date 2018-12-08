package com.hytcshare.jerrywebspider.enums;

import lombok.Getter;

@Getter
public enum DownloadedStatusEnum {
    /**
     * 资源已下载
     */
    DOWNLOADED(1, "资源已下载"),
    NOT_DOWNLOADED(0, "资源未下载");

    private int code;
    private String des;

    DownloadedStatusEnum(int code, String des) {
        this.code = code;
        this.des = des;
    }
}
