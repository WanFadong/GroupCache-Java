package com.somewan.cache.peer;

import com.somewan.cache.Result;

/**
 * Created by wan on 2017/1/28.
 */
public class Peer {
    private String host;// peer的host
    private String basePath;// Peer的host/cacheName/clusterName

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String host, String cacheName, String clusterName) {
        this.basePath = host +"/" + cacheName + "/" + clusterName;
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    /**
     * 从peer节点获取key对应的数据。
     * 暂时不支持key对应的数据为null的情况。返回null表示出错。
     * @param key
     * @return
     */
    // TODO
    public Result get(String key) {
        return null;
    }
}
