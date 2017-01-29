package com.somewan.cache.lru;

import com.somewan.cache.lru.LRUCache;
import com.somewan.cache.result.Result;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by wan on 2017/1/26.
 */
public class LRUCacheTest extends TestCase{

    public void testInit() {
        // 测试初始化一个大entry的cache。
        LRUCache lruCache = new LRUCache(1000000);
    }

    public void testAdd() {
        LRUCache lruCache = new LRUCache();
        assertFalse(lruCache.add(null, "value"));
    }

    public void testCache() {
        LRUCache lruCache = new LRUCache(3);
        assertTrue(lruCache.add("name", "wanfadong"));
        assertEquals("wanfadong", lruCache.get("name"));
        assertTrue(lruCache.add("age","24"));
        assertEquals("24", lruCache.get("age"));
        assertTrue(lruCache.add("gender", "man"));
        assertEquals("man", lruCache.get("gender"));
        // 测试超过最大条目
        assertTrue(lruCache.add("school", "NJU"));
        assertEquals("NJU", lruCache.get("school"));
        // 测试添加重复key
        assertFalse(lruCache.add("school", "南京大学"));
        assertEquals("NJU", lruCache.get("school"));

        //测试不存在的key
        assertEquals(Result.notFoundResult().getCode(), lruCache.get("password").getCode());
    }
}
