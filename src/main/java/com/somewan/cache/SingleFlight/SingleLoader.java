package com.somewan.cache.SingleFlight;

/**
 * Created by wan on 2017/1/28.
 */
public interface SingleLoader {

    /**
     * 用于为SingleFlight提供一个回调方法。
     * 对于同一个key在同一时间只会被执行一次。
     * @param key
     */
    public Object singleLoad(String key);

}
