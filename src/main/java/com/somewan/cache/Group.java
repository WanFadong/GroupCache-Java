package com.somewan.cache;

import com.somewan.cache.SingleFlight.SingleFlight;
import com.somewan.cache.SingleFlight.SingleLoader;
import com.somewan.cache.lru.LRUCache;
import com.somewan.cache.peer.ConsistentHashPeerPicker;
import com.somewan.cache.peer.Peer;
import com.somewan.cache.peer.PeerPicker;

/**
 * 一个Group代表一个命名空间
 * Created by wan on 2017/1/29.
 */
public class Group implements SingleLoader{

    private String cacheName;// 整个缓存的名称
    private String groupName;// 命名空间名称
    private LRUCache mainCache;
    private LRUCache hotCache;
    private PeerPicker picker;
    private SingleFlight single;// 用于请求归并

    //TODO 数据内存控制，现在默认使用maxEntry = 10000;

    /**
     * 需要设置名称
     */
    public Group() {
        mainCache = new LRUCache();
        hotCache = new LRUCache();
        picker = new ConsistentHashPeerPicker();
        single = new SingleFlight();
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * 从缓存中获取key对应的数据。
     * @param key
     * @return
     */
    public Object get(String key) {

    }

    /**
     * 要进行请求归并的部分
     * @param key
     * @return
     */
    @Override
    public Object singleLoad(String key) {
        return null;
    }

    private Object lookupCache(String key) {
        mainCache.get(key);
    }

    private Object getFromPeer(Peer peer, String key) {

    }

    private Object getLocally(String key) {

    }

    private Object setHotCache()
}
