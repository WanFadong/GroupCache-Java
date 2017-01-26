package com.somewan.cache.lru;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 一个使用LRU淘汰算法的缓存容器。
 * 数据不变更，所以没有并发问题，不需要锁。
 * 支持value = null，不支持key = null；
 * Created by wan on 2017/1/25.
 */
public class LRUCache {
    private Deque<String> ruList;// 按使用顺序维护map中的key列表，也就是一个淘汰顺序list。
    private Map<String, Object> cache;// 缓存数据容器
    private Evictable evict;// 数据被删除后执行的操作。
    private ReadWriteLock lock = new ReentrantReadWriteLock();//TODO 不使用锁可以吗
    public static final Object NOT_FOUND = new Object();

    public LRUCache() {
        this(null);
    }

    public LRUCache(Evictable evict) {
        ruList = new LinkedList<String>();
        cache = new HashMap<String, Object>();
        this.evict = evict;
    }

    public Object get(String key) {
        lock.readLock().lock();
        try {
            // 查看map
            if (!cache.containsKey(key)) {
                return NOT_FOUND;
            }

            Object obj = cache.get(key);
            // 更新rulist，移动key到最后面
            if(ruList.getLast().equals(key)) {

            }

        } finally {
            lock.readLock().unlock();
        }
    }
}
