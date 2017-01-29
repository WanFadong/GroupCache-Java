package com.somewan.cache;

import com.somewan.cache.SingleFlight.SingleFlight;
import com.somewan.cache.SingleFlight.SingleLoader;
import com.somewan.cache.lru.LRUCache;
import com.somewan.cache.peer.ConsistentHashPeerPicker;
import com.somewan.cache.peer.Peer;
import com.somewan.cache.peer.PeerPicker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 一个Group代表一个命名空间
 * Created by wan on 2017/1/29.
 */
public class Group implements SingleLoader{
    private static final Logger LOG = LogManager.getLogger(Group.class);

    private String groupName;// 命名空间名称
    private String host;// 缓存实例运行在的host

    private LRUCache mainCache;
    private LRUCache hotCache;
    private GroupCache groupCache;// 用于提供sigle, picker, getter。

//    private String cacheName;// 整个缓存的名称
//    private SingleFlight single;// 用于请求归并
//    private PeerPicker picker;// 需要用户提供节点配置，完成初始化
//    private LocalGetter getter;// 需要用于提供getter实现类。

    //TODO 数据内存控制，现在默认使用maxEntry = 10000;

    public Group(GroupCache groupCache) {
        mainCache = new LRUCache();
        hotCache = new LRUCache();
        single = new SingleFlight();
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void

    /**
     * 从缓存中获取key对应的数据。
     * 在调用Get之前，需要先初始化getter和picker。
     * @param key
     * @return
     */
    public Result get(String key, PeerPicker picker, LocalGetter getter) {
        LOG.info("正在获取key=({})对应的数据。", key);
        // 先检查本地缓存；
        Result result = lookupCache(key);
        if(result.isSuccess()) {
            return result;
        }

        // 本地缓存不存在，开始加载过程
        LOG.info("本地缓存中不存在key={}", key);
        result = single.singleDo(this, key);

    }


    /**
     * 要进行请求归并的部分
     * 注意：不要更改这个result引用指向的对象。
     * @param key
     * @return
     */
    @Override
    public void singleLoad(String key, Result resultRtn) {
        LOG.info("正在加载key={}的数据", key);
        // 首先再次检查本地缓存
        Result result = lookupCache(key);
        if(result.isSuccess()) {
            resultRtn.copy(result);
            return;
        }

        // 从其他节点获取数据。
        LOG.info("再次检查，本地缓存中不存在key={}", key);
        result = getFromPeer(key);
        if(result.isSuccess()) {
            resultRtn.copy(result);
            return;
        }

        // 从本地获取数据
    }

    private Result lookupCache(String key) {
        Result result = mainCache.get(key);
        if(result.isSuccess()) {
            return result;
        }
        result = hotCache.get(key);
        return result;
    }

    /**
     * 从其他结点获取数据，并且设置hotcache。
     * @param key
     * @return
     */
    private Result getFromPeer(String key) {
        Peer peer = picker.pickPeer(key);
        Result result = peer.get(key);
        if(result.isSuccess()) {
            // 设置hotCache数据
            setHotCache(key, result);
        }
        LOG.info("从节点{}获取key={}对应的数据，Result={}", peer.getBasePath(), key, result);
        return result;
    }

    private Object getLocally(String key) {

    }

    /**
     * 设置热缓存。现在默认是有10%的概率设置为热数据。随机的
     * @param result
     * @return
     */
    // TODO 更好的热数据判定算法
    private void setHotCache(String key, Result result) {
        int random = (int) (Math.random() * 10);
        if(random == 0) {
            hotCache.add(key, result.getValue());
        }
    }
}
