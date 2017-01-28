package com.somewan.cache.lru;

import com.somewan.cache.lru.EvictedLog;
import junit.framework.TestCase;

/**
 * Created by wan on 2017/1/26.
 */

public class EvitedLogTest extends TestCase{
    EvictedLog evictedLog = new EvictedLog();

    public void testOnEvicted() {
        evictedLog.onEvicted("姓名", "万发东");
    }
}
