package com.somewan.cache.peer;

import com.somewan.cache.result.Result;

/**
 * Created by wan on 2017/1/28.
 */
public class Peer {
    private String host;// peer的host

    public Peer(String host) {
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
    public Result get(String cacheName, String groupName, String key) {
        String url = host +"/" + cacheName + "/" + groupName;
        return Result.notFoundResult();
    }
}
