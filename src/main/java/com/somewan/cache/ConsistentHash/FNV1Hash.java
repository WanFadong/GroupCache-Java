package com.somewan.cache.ConsistentHash;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FNV1_32_HASH算法
 * 拷贝自http://www.osbaike.net/article-show-id-7198.html。并做简要修改。
 * Created by wan on 2017/1/26.
 */
public class FNV1Hash implements HashFunc {
    private static final Logger LOG = LogManager.getLogger(FNV1Hash.class);

    @Override
    public long hash(String data) {
        // copy from other
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < data.length(); i++)
            hash = (hash ^ data.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // modified by me
        long lhash = hash + (1L << 31);// 所有数向左平移2^31，范围由[-2^31, 2^31-1]-->[0, 2^32-1]
        //LOG.info("哈希结果：data={}, hash0={} hash={}", data, hash, lhash);
        return lhash;
    }
}
