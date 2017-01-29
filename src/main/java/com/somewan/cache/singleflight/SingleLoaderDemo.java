package com.somewan.cache.singleflight;

import com.somewan.cache.result.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wan on 2017/1/30.
 */
public class SingleLoaderDemo implements SingleLoader {
    @Override
    public void singleLoad(String key, Result resultRtn) {
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
        resultRtn.copy(Result.successResult(value));
    }
}
