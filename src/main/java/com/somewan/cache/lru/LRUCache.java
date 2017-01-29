package com.somewan.cache.lru;

import com.alibaba.fastjson.JSON;
import com.somewan.cache.result.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 一个使用LRU淘汰算法的缓存容器。
 * 不提供缓存的更新(update, remove)方法，只提供增加(add)和查找(get)
 * 支持value = null，不支持key = null；
 * Created by wan on 2017/1/25.
 */
public class LRUCache {
    private static final Logger LOG = LogManager.getLogger(LRUCache.class);
    // public static final Object NOT_FOUND = new Object();
    public static final int DEFAULT_MAX_ENTRY = 10000;

    private Deque<String> ruList;// 访问顺序链表：按使用顺序保存map中key，维护淘汰顺序。类似于先进先出。
    private Map<String, Object> cache;// 缓存数据容器
    private Evictable evict;// 数据被删除后执行的操作。
    private int maxEntry;// 存储的数据条目数。

    //TODO 不使用锁可以吗
    // 先访问map的锁，再访问list的锁，不产生死锁。
    private ReadWriteLock mapLock = new ReentrantReadWriteLock();// map上的锁
    private ReadWriteLock listLock = new ReentrantReadWriteLock();// 链表上的锁

    /**
     * maxEntry默认为DEFAULT_MAX_ENTRY
     * evict默认为null
     */
    public LRUCache() {
        this(null, DEFAULT_MAX_ENTRY);
    }

    public LRUCache(Evictable evict) {
        this(evict, DEFAULT_MAX_ENTRY);
    }

    public LRUCache(int maxEntry) {
        this(null, maxEntry);
    }

    public LRUCache(Evictable evict, int maxEntry) {
        long start = System.currentTimeMillis();
        this.evict = evict;
        if(maxEntry <= 0) {
            maxEntry = DEFAULT_MAX_ENTRY;
        }
        this.maxEntry = maxEntry;
        ruList = new LinkedList<String>();
        int capacity = (int) (this.maxEntry/0.75) + 1;
        cache = new HashMap<String, Object>(capacity);
        long end = System.currentTimeMillis();
//        LOG.info("初始化{}耗时{}ms。maxEntry={}，evict={}",
//                LRUCache.class.getName(), (end-start), maxEntry, JSON.toJSONString(evict));
    }

    /**
     * 获取key对应的value。
     * Result.found用于区别到底是value=null还是没有key对应的value。
     * @param key
     * @return
     */
    public Result get(String key) {
        LOG.info("正在获取缓存中key={}的数据", key);
        //TODO 不要在锁里面打印日志。
        mapLock.readLock().lock();
        try {
            // 查看map
            if (!cache.containsKey(key)) {
                LOG.info("缓存中不存在key={}的数据", key);
                return Result.notFoundResult();
            }

            Object obj = cache.get(key);
            // 更新访问顺序链表
            setFreshKey(key);
            LOG.info("获取缓存数据成功（key={}, value={}）", key, JSON.toJSONString(obj));
            return Result.successResult(obj);
        } catch (RuntimeException e){
            LOG.error("读取缓存失败。", e);
            return Result.errorResult();
        } finally {
            mapLock.readLock().unlock();
        }
    }

    /**
     * 向缓存中添加一条数据
     * 不能重复添加，只能是key不存在的时候添加。
     * @param key
     * @param value
     * @return 返回值表示是否添加成功。key已存在，那么添加失败
     */
    public boolean add(String key, Object value) {
        LOG.info("正在向缓存中添加（key={}, value={}的数据）", key, JSON.toJSONString(value));
        if(key == null) {
            LOG.warn("缓存中key不允许为null，写入缓存失败");
            return false;//TODO 返回异常，以及异常代码。
        }
        mapLock.writeLock().lock();
        try {
            // map中写入数据
            if(cache.containsKey(key)) {
                LOG.warn("缓存中已存在该key，写入缓存失败");
                return false;
            }
            // 是否超过最大条目数
            if(cache.size() >= maxEntry) {
                // 淘汰一条数据
                removeOldestKey();
            }

            cache.put(key, value);//WARN map和list不是放在一个事务里做的，所以要注意通过锁保持一致性。

            // 更新访问顺序链表
            setFreshKey(key);
            LOG.info("写入缓存数据成功");
            return true;
        } catch (Exception e) {
           LOG.error("写入缓存失败。", e);
           return false;
        }  finally{
            mapLock.writeLock().unlock();
        }
    }


    /**
     * 把最新访问(get/add)的key放到链表最前面。
     * @param key
     */
    private void setFreshKey(String key) {
        listLock.writeLock().lock();
        try {
            // list不可能为null。
            if (!key.equals(ruList.peekFirst())) {// 如果list为空，那么peekFirst返回null。
                ruList.remove(key);// 如果key不存在，那么rulist不会变动。
                ruList.addFirst(key);
            }
        } finally {
            listLock.writeLock().unlock();
        }
    }

    /**
     * 淘汰一条数据。
     */
    private void removeOldestKey() {
        listLock.writeLock().lock();
        try {
            String key = ruList.pollLast();
            Object value = cache.remove(key);// 在add中已经获取了mapLock
            LOG.info("数据（key = {}, value = {}）被淘汰", key, JSON.toJSONString(value));
            if(evict != null)
                evict.onEvicted(key, value);// TODO 移出锁内
        } finally {
            listLock.writeLock().unlock();
        }
    }

}
