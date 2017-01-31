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
     * 通过HTTP请求的方式来处理。
     * @param key
     * @return
     */
    public Result get(String cacheName, String groupName, String key) {
        // RESTful风格的请求。
        String url = host +"/" + cacheName + "/" + groupName;
        Result result = HttpUtils.get(url);
        //TODO 某些错误的类型，可以尝试重试
        return result;
    }
}
