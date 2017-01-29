package com.somewan.cache;

import com.somewan.cache.SingleFlight.SingleFlight;
import com.somewan.cache.peer.ConsistentHashPeerPicker;
import com.somewan.cache.peer.PeerPicker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by wan on 2017/1/29.
 */
public class GroupCache {
    private static final Logger LOG = LogManager.getLogger(GroupCache.class);

    private String host;// 当前机器的host，格式：ip:port; 127.0.0.1:8000
    private String cacheName;
    private SingleFlight single;// 用于请求归并
    private PeerPicker picker;// 需要用户提供节点配置，完成初始化
    private LocalGetter getter;// 需要用于提供getter实现类。

    public GroupCache(String cacheName, LocalGetter getter) {
        this.cacheName = cacheName;
        this.getter = getter;
    }

    /**
     * 初始化集群节点
     * @param local 本机host
     * @param peers 集群节点（包括本机）
     * @return
     */
    public boolean initPeers(String local, String[] peers) {
        this.host = local;
        picker = new ConsistentHashPeerPicker();
        ((ConsistentHashPeerPicker)picker).initPeers(peers);
    }
}
