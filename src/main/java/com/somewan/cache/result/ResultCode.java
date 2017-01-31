package com.somewan.cache.result;

/**
 * Created by wan on 2017/1/29.
 */
public enum ResultCode {
    SUCCESS,
    NOT_FOUND,// 没有找到对应数据
    LOCAL_LOAD,// 数据应该在本节点加载
    BAD_REQUEST,// 用户不合法的请求
    SERVER_ERROR,// 服务器处理过程中出现异常
    NET_ERROR// 集群间访问网络错误
}
