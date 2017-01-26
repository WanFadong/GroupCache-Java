package com.somewan.cache.ConsistentHash;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.jvm.hotspot.utilities.Assert;

import java.util.*;

/**
 * Created by wan on 2017/1/26.
 */
public class ConsistentHash {
    private static final Logger LOG = LogManager.getLogger(ConsistentHash.class);
    private static final int DEFAULT_REPLICAS = 50;

    private HashFunc hashFunc;// 默认FNV1
    private int replicas;// 副本数，默认50
    private SortedSet<Long> peerHashSet;// 节点的hash值顺序表
    private Map<Long, String> hashPeerMap;// <hash-节点名称>映射关系map

    /**
     * 使用默认配置
     */
    public ConsistentHash() {
        this(null, 0);
    }

    public ConsistentHash(HashFunc hashFunc, int replicas) {
        if(hashFunc == null) {
            hashFunc = new Fnv1Hash();
        }
        this.hashFunc = hashFunc;
        if(replicas <= 0) {
            replicas = DEFAULT_REPLICAS;
        }
        this.replicas = replicas;
    }

    public boolean isEmpty() {
        if(hashList == null || hashList.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 初始化节点。
     * 节点不能重复。重复的不再次添加，只会打印warn日志。
     * @return 表示成功/失败。
     */
    public boolean setPeers(String[] peers) {
        LOG.info("正在初始化节点。peers=({})", JSON.toJSONString(peers));
        try {
            if (peers == null || peers.length == 0) {
                LOG.warn("节点数据为空，初始化失败。");
                return false;
            }
            if (isEmpty()) {
                int num = peers.length;
                hashList = new ArrayList<Long>(num);
                int cap = (int) (num / 0.75) + 1;
                hashPeerMap = new HashMap<Long, String>(cap);
            }

            for (String peer : peers) {
                for (int i = 0; i < replicas; i++) {
                    String peerKey = peer + "_" + i;
                    long hash = hashFunc.hash(peerKey);
                    boolean ok = peerHashSet.add(hash);
                    if(!ok) {
                        // 节点名称重复
                        LOG.warn("初始化节点重复，peer={}", peer);
                        break;
                    }
                    hashPeerMap.put(hash, peer);
                }
            }
            LOG.info("初始化节点成功。peerHashSet={} hashPeerMap.size={}", JSON.toJSONString(peerHashSet), hashPeerMap.size());
            return true;
        }catch (Exception e) {
            LOG.error("初始化节点异常", e);
            return false;
        }
    }

    /**
     * 对于给定的key，查询其保存在哪个节点上。
     * @param key
     * @return 返回key所在的节点。返回null表示失败。
     */
    public String getPeer(String key) {
        LOG.info("正在查询key={}对应的节点。", key);
        try {
            if (key == null) {
                LOG.warn("key不能为null，查询key对应节点失败。");
                return null;
            }

            if (isEmpty()) {
                LOG.warn("节点尚未初始化，查询key对应节点失败。peerHashSet={} peerHashMap={}",
                        peerHashSet == null ? null : peerHashSet.size(),
                        hashPeerMap == null ? null : hashPeerMap.size());
                return null;
            }

            long hash = hashFunc.hash(key);

            // 查找hashSet中大于hash的最小hash
            //TODO 使用二分查找
            long nextHash = peerHashSet.first();// 如果是 > peerHashSet中最大的hash，因为是环，赋给第一个
            for (long peerHash : peerHashSet) {
                if (peerHash > hash) {
                    nextHash = peerHash;
                    break;
                }
            }

            // 返回值
            String peer = hashPeerMap.get(nextHash);// peer不应该为null。
            LOG.info("获取key对应的节点成功，peer={}", peer);
            return peer;
        } catch (Exception e) {
            LOG.error("查询key对应节点失败",e);
            return fasle;
        }
    }
}
