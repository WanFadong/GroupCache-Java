package com.somewan.cache.consistenthash;

import junit.framework.TestCase;

/**
 * Created by wan on 2017/1/28.
 */
public class ConsistentHashTest extends TestCase{

    public void testSetPeers() {
        ConsistentHash consistentHash = new ConsistentHash();
        String[] peers1 = null;
        assertFalse(consistentHash.setPeers(peers1));
        String[] peers = {"8001", "8002"};
        assertTrue(consistentHash.setPeers(peers));
        String[] peers2 = {"8001", "8002", "8003"};
        assertTrue(consistentHash.setPeers(peers2));
    }

    public void testGetPeer() {
        ConsistentHash consistentHash = new ConsistentHash();

        String[] keys = {"wanfadong", "wanfajuan", "wanfadong", "wanqian", "tianrui"};
        assertTrue(consistentHash.getPeer(keys[1]) == null);
        String[] peers = {"8001", "8002", "8003"};
        consistentHash.setPeers(peers);

        for(String key: keys) {
            assertTrue(consistentHash.getPeer(key) != null);
        }
    }

}
