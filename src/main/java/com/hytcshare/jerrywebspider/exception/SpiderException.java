package com.hytcshare.jerrywebspider.exception;

public class SpiderException extends RuntimeException {
    private String code;
    private String msg;

    public SpiderException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
