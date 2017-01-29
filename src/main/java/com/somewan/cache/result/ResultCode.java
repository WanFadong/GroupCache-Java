package com.somewan.cache.result;

/**
 * Created by wan on 2017/1/29.
 */
public enum ResultCode {
    SUCCESS,
    NOT_FOUND,// 没有找到对应数据
    LOCAL_LOAD,// 数据应该在本节点加载
    ERROR// 处理过程中出现异常
}
