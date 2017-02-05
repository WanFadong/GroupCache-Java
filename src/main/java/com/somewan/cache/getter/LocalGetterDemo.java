package com.somewan.cache.getter;

import com.somewan.cache.result.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wan on 2017/1/30.
 */
public class LocalGetterDemo implements LocalGetter {
    @Override
    public Result get(String nameSpace, String key) {
        Map<String, Object> wanMap = new HashMap<String, Object>();
        wanMap.put("name", "wanfadong");
        wanMap.put("age", "25");
        wanMap.put("gender", "man");
        Map<String, Object> caiMap = new HashMap<String, Object>();
        caiMap.put("age", "24");

        Object value;
        if(nameSpace.compareTo("wan") == 0) {
            value = wanMap.get(key);
        } else {
            value = caiMap.get(key);
        }
        return Result.successResult(value);
    }
}
