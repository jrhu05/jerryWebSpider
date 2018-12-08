package com.hytcshare.jerrywebspider.enums;

import lombok.Getter;

@Getter
public enum ExceptionEnum {
    /**
     * endLine参数不合法s
     */
    ENDLINE_ILLEGAL("9001", "endLine参数不合法"),
    URL_ILLEGAL("9002", "url不合法");

    private String code;
    private String des;

    private ExceptionEnum(String code, String des) {
        this.code = code;
        this.des = des;
    }
}
