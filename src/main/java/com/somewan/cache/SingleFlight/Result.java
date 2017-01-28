package com.somewan.cache.SingleFlight;

import java.util.concurrent.locks.Condition;

/**
 * 一个用于保存结果的，并且可以等待的容器。
 * Created by wan on 2017/1/28.
 */
public class Result {

    public Object value() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    private Object value;


}
