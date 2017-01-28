package com.somewan.cache.ConsistentHash;

import junit.framework.TestCase;

/**
 * Created by wan on 2017/1/28.
 */
public class Fnv1HashTest extends TestCase {

    public void testHash() {
        Fnv1Hash hashFunc = new Fnv1Hash();
        for(int i = 0; i < 10; i++) {
            hashFunc.hash("wanfadong" + i);
        }
    }

}
