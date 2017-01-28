package com.somewan.cache.peer;

import com.alibaba.fastjson.JSON;
import com.somewan.cache.ConsistentHash.ConsistentHash;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by wan on 2017/1/28.
 */
public class ConsistentHashPeerPicker implements PeerPicker {
    private static final Logger LOG = LogManager.getLogger(ConsistentHashPeerPicker.class);
    private ConsistentHash consistentHash = new ConsistentHash();
    private String cacheName;
    private String clusterName;


    /**
     * 选择key所在的节点。
     * @param key
     * @return
     */
    @Override
    public Peer pickPeer(String key) {
        if(consistentHash.isEmpty()) {
            LOG.warn("集群节点尚未初始化，获取key对应的节点失败。");
            return null;
        }

        String peerHost = consistentHash.getPeer(key);
        Peer peer = new Peer();
        peer.setBasePath(peerHost, cacheName, clusterName);
        LOG.info("获取节点成功：key={} peer={}", key, JSON.toJSONString(peer));
        return peer;
    }

    /**
     * 初始化集群节点。
     * @param peers
     * @return
     */
    public boolean initPeers(String[] peers, String cacheName, String clusterName) {
        LOG.info("正在初始化集群。cacheName={} clusterName={}", cacheName, clusterName);
        if(cacheName == null) {
            return false;
        }
        if(clusterName == null) {
            return false;
        }
        this.cacheName = cacheName;
        this.clusterName = clusterName;
        return consistentHash.setPeers(peers);
    }
}
