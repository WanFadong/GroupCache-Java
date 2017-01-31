package com.somewan.cache.result;

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

    public Object getValue() {
        return this.value;
    }

    public Object getCode() {
        return this.code;
    }

    public void setCode(ResultCode code) {
        this.code = code;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public static Result successResult(Object value) {
        return new Result(ResultCode.SUCCESS, value);
    }

    public static Result notFoundResult() {
        return new Result(ResultCode.NOT_FOUND, null);
    }

    public static Result serverErrorResult() {
        return new Result(ResultCode.SERVER_ERROR, null);
    }

    public static Result localLoadResult() {
        return new Result(ResultCode.LOCAL_LOAD, null);
    }

    /**
     * erroCode != success
     * @param errorCode
     * @return
     */
    public static Result errorResult(ResultCode errorCode) {
        return new Result(errorCode, null);
    }

    public static Result badRequestResult() {
        return new Result(ResultCode.BAD_REQUEST, null);
    }

    public boolean isSuccess() {
        return code == ResultCode.SUCCESS;
    }


    public void copy(Result result) {
        code = result.code;
        value = result.value;
    }

}
