package com.somewan.cache.singleflight;

import com.somewan.cache.result.Result;
import com.somewan.cache.result.ResultCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.*;

/**
 * 一个group对应一个single。因为single中只记录key，不记录namespace。
 * Created by wan on 2017/1/28.
 */
public class SingleFlight {
    private static final Logger LOG = LogManager.getLogger(SingleFlight.class);
    private Lock mapLock = new ReentrantLock();
    private Map<String, Result> keyResultMap = new HashMap<String, Result>();

    /**
     *
     * 注：这里的返回值都指向一个地方，有点危险。如果被修改了，其他地方都会修改。
     * @param singleLoader
     * @param key
     * @return
     */
    public Result singleDo(SingleLoader singleLoader, String key) {
        try {
            if (key == null) {
                return Result.errorResult(ResultCode.BAD_REQUEST);
            }

            mapLock.lock();
            // 如果已经有线程正在执行load任务，那么阻塞住
            if (keyResultMap.containsKey(key)) {
                Result result;
                try {
                    LOG.info("已有线程正在获取key={}对应的数据", key);
                    result = keyResultMap.get(key);
                } finally {
                    mapLock.unlock();
                }

                synchronized (result) {
                    try {
                        result.wait();
                    } catch (InterruptedException e) {
                        LOG.error("获取key={}的缓存失败", key, e);
                    }
                }
                return result;
            }

            Result result;
            try {
                // 如果没有
                LOG.info("初次获取key={}的数据", key);
                result = new Result();
                keyResultMap.put(key, result);
            } finally {
                mapLock.unlock();
            }

            // 这里不能用返回值，必须用函数副作用。
            // （如果用返回值，还是得把返回的值设置到这个Result中）
            singleLoader.singleLoad(key, result);
            synchronized (result) {
                result.notifyAll();
            }

            //移除
            mapLock.lock();
            try {
                keyResultMap.remove(key);
            } finally {
                mapLock.unlock();
            }
            return result;
        } catch (Exception e) {
            LOG.error("获取key={}的缓存失败", key, e);
            return Result.errorResult(ResultCode.SERVER_ERROR);
        }

    }
}
