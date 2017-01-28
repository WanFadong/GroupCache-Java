package com.somewan.cache.peer;

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
     * @param key
     * @return
     */
    // TODO
    public Object get(String key) {
        return null;
    }
}
