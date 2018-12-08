package com.hytcshare.jerrywebspider.enums;

import lombok.Getter;

@Getter
public enum SpiderTaskStatusEnum {
    /**
     * 任务正在进行中
     */
    ONGOING(1, "任务正在进行中"),
    SHUTDOWN(0, "任务已停止");

    private int code;
    private String des;

    SpiderTaskStatusEnum(int code, String des) {
        this.code = code;
        this.des = des;
    }
}
