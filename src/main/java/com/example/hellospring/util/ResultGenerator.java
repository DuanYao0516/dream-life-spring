package com.example.hellospring.util;

public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final String DEFAULT_FAIL_MESSAGE = "FAIL";
    private static final int RESULT_CODE_SUCCESS = 200;
    private static final int RESULT_CODE_SERVER_ERROR = 500;

    public static Result genSuccessResult()
    {
        return new Result(RESULT_CODE_SUCCESS, DEFAULT_SUCCESS_MESSAGE);
    }

    public static Result genSuccessResult(Object data)
    {
        return new Result(RESULT_CODE_SUCCESS, DEFAULT_SUCCESS_MESSAGE, data);
    }

    public static Result genFailResult(String message)
    {
        return new Result(RESULT_CODE_SERVER_ERROR, message);
    }

    public static Result genFailResult(String message, Object data)
    {
        return new Result(RESULT_CODE_SERVER_ERROR, message, data);
    }

    public static Result genErrorResult(int code, String message) {
        return new Result(code, message);
    }


}
