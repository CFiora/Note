package com.fiora.note2.enums;

import lombok.Getter;

@Getter
public enum StatusCode {
    OK(200,"Success"),
    FAIL(100, "Fail");

    private int code;
    private String message;

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
