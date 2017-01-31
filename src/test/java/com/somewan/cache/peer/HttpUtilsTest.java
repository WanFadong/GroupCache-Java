package com.somewan.cache.peer;

import com.alibaba.fastjson.JSON;
import com.somewan.cache.result.Result;
import junit.framework.TestCase;

/**
 * Created by wan on 2017/1/31.
 */
public class HttpUtilsTest extends TestCase {

    public void testGet() {
        Result result = HttpUtils.get("http://127.0.0.1:80/groupcache/info/name");
        System.out.println(JSON.toJSONString(result));
    }

}
