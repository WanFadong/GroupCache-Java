package com.somewan.cache.SingleFlight;

import com.somewan.cache.Result;

/**
 * Created by wan on 2017/1/28.
 */
public interface SingleLoader {

    /**
     * 用于为SingleFlight提供一个回调方法。
     * 对于同一个key在同一时间只会被执行一次。
     * @param key
     * @return 这里没有用返回值，而是用副作用的形式。是因为SingleFlight中需要先提供一个Result的句柄。
     */
    public void singleLoad(String key, Result result);

}
