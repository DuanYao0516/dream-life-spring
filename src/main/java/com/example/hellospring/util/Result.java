package com.example.hellospring.util;

import java.io.Serial;
import java.io.Serializable;

public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int resultCode;
    private String message;
    private T data;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result() {}

    public Result(int resultCode, String message)
    {
        this.resultCode = resultCode;
        this.message = message;
    }

    public Result(int resultCode, String message, T data)
    {
        this.resultCode = resultCode;
        this.message = message;
        this.data = data;
    }
}
