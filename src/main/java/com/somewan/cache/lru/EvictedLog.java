package com.somewan.cache.lru;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 当数据被淘汰时，打印日志到
 * Created by wan on 2017/1/25.
 */
public class EvictedLog implements Evictable {
    private static final Logger LOG = LogManager.getLogger("Console.Trace");

    public void onEvicted(String key, Object value) {
        LOG.info("数据（key = {}, value = {}）被淘汰", key, JSON.toJSONString(value));
    }
}
