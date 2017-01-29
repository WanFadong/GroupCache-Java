package com.somewan.cache.SingleFlight;

import com.somewan.cache.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于测试SingleFlight的一个桩
 * Created by wan on 2017/1/28.
 */
public class CacheLoader implements SingleLoader {
    @Override
    public Result singleLoad(String key) {
        Map<String, Object> cacheData = new HashMap<String, Object>();
        cacheData.put("name", "wanfadong");
        cacheData.put("age", "25");
        cacheData.put("gender", "man");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Object value = cacheData.get(key);
        return Result.successResult(value);
    }
}
