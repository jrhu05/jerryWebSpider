package com.hytcshare.jerrywebspider.enums;

import lombok.Getter;

@Getter
public enum ExceptionEnum {
    /**
     * endLine参数不合法s
     */
    ENDLINE_ILLEGAL("9001", "endLine参数不合法"),
    URL_ILLEGAL("9002", "url不合法"),
    COLLECTION_NAME_ILLEGAL("9003", "collectionName不合法"),
    TASK_ALREADY_ONGOING("9004", "任务已经在进行中");

    private String code;
    private String des;

    private ExceptionEnum(String code, String des) {
        this.code = code;
        this.des = des;
    }
}
