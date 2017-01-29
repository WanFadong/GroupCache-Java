package com.somewan.cache.ConsistentHash;

import junit.framework.TestCase;

/**
 * Created by wan on 2017/1/28.
 */
public class Fnv1HashTest extends TestCase {

    public void testHash() {
        FNV1Hash hashFunc = new FNV1Hash();
        for(int i = 0; i < 10; i++) {
            hashFunc.hash("wanfadong" + i);
        }
    }

}
