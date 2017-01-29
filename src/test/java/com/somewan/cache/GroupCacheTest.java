package com.somewan.cache;

import com.somewan.cache.getter.LocalGetter;
import com.somewan.cache.getter.LocalGetterDemo;
import junit.framework.TestCase;

/**
 * Created by wan on 2017/1/30.
 */
public class GroupCacheTest extends TestCase {

    public void testGroupCache() {
        LocalGetter getter = new LocalGetterDemo();
        GroupCache groupCache = new GroupCache("groupcache", getter);

        String[] peers = {"8000", "8001", "8002"};
        groupCache.initPeers("8000",peers);

        groupCache.newNamespace("wan");
        groupCache.newNamespace("cai");

        groupCache.get("wan", "name");
        groupCache.get("wan", "name");
    }

}
