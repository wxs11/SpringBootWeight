package com.wxs.springbootweight.util;

import lombok.Getter;


@Getter
public enum ResultEnum {

    UNKUOW_ERROR(400, "未知错误"),
    SUCCESS(200, "success"),
    ERROR(201, "error");

    private int code;
    private String message;

    ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }
}

