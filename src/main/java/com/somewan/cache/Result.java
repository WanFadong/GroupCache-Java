package com.somewan.cache;

/**
 * Created by wan on 2017/1/29.
 */
public class Result {

    private ResultCode code;
    private Object value;

    public Result() {

    }

    private Result(ResultCode code, Object value) {
        this.code = code;
        this.value = value;
    }

    public static Result successResult(Object value) {
        return new Result(ResultCode.SUCCESS, value);
    }

    public static Result notFoundResult() {
        return new Result(ResultCode.NOT_FOUND, null);
    }

    public static Result errorResult() {
        return new Result(ResultCode.ERROR, null);
    }

    public static Result localLoadResult() {
        return new Result(ResultCode.LOCAL_LOAD, null);
    }

    public boolean isSuccess() {
        return code == ResultCode.SUCCESS;
    }


    public void copy(Result result) {
        code = result.code;
        value = result.value;
    }

    public Object getValue() {
        return this.value;
    }
}
