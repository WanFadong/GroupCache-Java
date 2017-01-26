package com.somewan.cache.lru;

/**
 * LRUCache中，当一个数据被淘汰时会回调这个方法。
 * Created by wan on 2017/1/25.
 */
public interface Evictable {
    public void onEvicted(String key, Object value);
}
