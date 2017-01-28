package com.somewan.cache.peer;

import junit.framework.TestCase;

/**
 * Created by wan on 2017/1/28.
 */
public class ConsistentHashPeerPickerTest extends TestCase {

    public void testPickPeer() {
        String[] peers = {"8001", "8002", "8003"};
        String[] keys = {"wanfadong", "wanfajuan", "wanqian"};
        // 初始化
        ConsistentHashPeerPicker peerPicker = new ConsistentHashPeerPicker();
        assertNull(peerPicker.pickPeer(keys[0]));

        peerPicker.initPeers(peers, "groupCache", "test");
        for(String key: keys) {
            assertNotNull(peerPicker.pickPeer(key));
        }
    }

}
