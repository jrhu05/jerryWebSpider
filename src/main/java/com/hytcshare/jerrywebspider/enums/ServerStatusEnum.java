package com.hytcshare.jerrywebspider.enums;

import lombok.Getter;

@Getter
public enum ServerStatusEnum {
    /**
     *
     */
    OK("0"),
    ERROR("99999");

    private String code;

    ServerStatusEnum(String code) {
        this.code = code;
    }
}
