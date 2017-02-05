package com.somewan.cache;

import com.alibaba.fastjson.JSON;
import com.somewan.cache.getter.LocalGetter;
import com.somewan.cache.peer.ConsistentHashPeerPicker;
import com.somewan.cache.peer.PeerPicker;
import com.somewan.cache.result.Result;
import com.somewan.cache.result.ResultCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wan on 2017/1/29.
 */
public class GroupCache {
    private static final Logger LOG = LogManager.getLogger(GroupCache.class);

    private String host;// 当前机器的host，格式：ip:port; 127.0.0.1:8000
    private String cacheName;
    private PeerPicker picker;// 需要用户提供节点配置，完成初始化
    private LocalGetter getter;// 需要用于提供getter实现类。
    private Map<String, Group> groups;

    public GroupCache(String cacheName, LocalGetter getter) {
        this.cacheName = cacheName;
        this.getter = getter;
        this.groups = new HashMap<String, Group>();
    }

    public String getHost() {
        return host;
    }

    public String getCacheName() {
        return cacheName;
    }

    public PeerPicker getPicker() {
        return picker;
    }

    public LocalGetter getGetter() {
        return getter;
    }

    /**
     * 初始化集群节点
     * @param local 本机host
     * @param peers 集群节点（包括本机）
     * @return 表示成功或者失败。
     */
    public boolean initPeers(String local, String[] peers) {
        try {
            this.host = local;
            picker = new ConsistentHashPeerPicker();
            boolean success = ((ConsistentHashPeerPicker) picker).initPeers(peers);
            LOG.info("初始化集群节点{}", success ? "成功" : "失败");
            return success;
        } catch (Exception e) {
            LOG.error("初始化集群节点出现异常", e);
            return false;
        }
    }

    /**
     * 新建一个命名空间
     * @param name
     */
    public void newNamespace(String name) {
        try {
            Group group = new Group(this, name);
            groups.put(name, group);
            LOG.info("新建命名空间{}", name);
        } catch (Exception e) {
            LOG.error("新建命名空间出现异常", e);
        }
    }

    /**
     * 获取命名空间下key对应的值。
     * @param namespace
     * @param key
     * @return
     */
    public Result get(String namespace, String key) {
        try {
            Group group = groups.get(namespace);
            if (group == null) {
                LOG.warn("用户请求不存在的命名空间,namespace={}", namespace);
                return Result.errorResult(ResultCode.BAD_REQUEST);
            }

            Result result = group.get(key);
            LOG.info("请求namespace={} key={} 数据结果：{}", namespace, key, JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            LOG.error("获取数据出现异常，namespace={}, key={}", namespace, key, e);
            return Result.serverErrorResult();
        }
    }
}
