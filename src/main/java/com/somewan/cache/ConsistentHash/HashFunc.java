package com.somewan.cache.ConsistentHash;

/**
 * Created by wan on 2017/1/26.
 */
public interface HashFunc {

    // 计算key的哈希值，返回一个32位无符号整数。
    // 因为java中不支持无符号整数。所以返回一个long类型。范围[0,2^32-1]
    public long hash(String key);
}
