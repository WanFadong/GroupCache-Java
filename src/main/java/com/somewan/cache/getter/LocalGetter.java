package com.somewan.cache.getter;

import com.somewan.cache.result.Result;

/**
 * Created by wan on 2017/1/29.
 */
public interface LocalGetter {
    public Result get(String nameSpace, String key);
}
