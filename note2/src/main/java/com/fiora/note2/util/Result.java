package com.fiora.note2.util;

import com.fiora.note2.enums.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result implements Serializable {
    private int code;
    private String message;
    private Object data;

    public static Result ok() {
        return Result.result(200, "success", null);
    }
    public static Result ok(Object data) {
        return Result.result(200, "success", data);
    }

    public static Result result(StatusCode statusCode) {
        return Result.result(statusCode.getCode(), statusCode.getMessage(), null);
    }

    public static Result result(int code, String message, Object data) {
        return new Result(code, message, data);
    }
}
