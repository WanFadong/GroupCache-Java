package com.somewan.cache.peer;

/**
 * Created by wan on 2017/1/28.
 */
public interface PeerPicker {
    /**
     * 选择key对应的节点。
     * @param key
     * @return
     */
    public Peer pickPeer(String key);
}
