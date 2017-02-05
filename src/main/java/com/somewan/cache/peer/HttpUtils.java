package com.somewan.cache.peer;

import com.alibaba.fastjson.JSON;
import com.somewan.cache.result.Result;
import com.somewan.cache.result.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 工具类：提供一些HTTP的实用方法：
 *  发送GET请求，获取响应中的body部分。
 * Created by wan on 2017/1/31.
 */
public class HttpUtils {
    private static final Logger LOG = LogManager.getLogger(HttpUtils.class);

    public static Result get(String urlStr) {
        if(StringUtils.isBlank(urlStr)) {
            return Result.badRequestResult();
        }

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            int code = connection.getResponseCode();
            // 正常返回的响应一定是200
            if(code != 200) {
                return Result.errorResult(ResultCode.NET_ERROR);
            }
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // 正常返回响应的body只有一行。
            String retStr = reader.readLine();

            if(retStr == null) {
                return Result.errorResult(ResultCode.NET_ERROR);
            }

            Result result = JSON.parseObject(retStr, Result.class);
            LOG.info("请求成功，url=({})，result={}", urlStr, JSON.toJSONString(result));
            return result;
        } catch (MalformedURLException e) {
            LOG.error("请求失败2017年02月03日16:32:26，url=({})", urlStr, e);
            return Result.errorResult(ResultCode.BAD_REQUEST);
        } catch (IOException e) {
            LOG.error("请求失败，url=({})", urlStr, e);
            return Result.errorResult(ResultCode.NET_ERROR);
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    return Result.errorResult(ResultCode.NET_ERROR);// 这里会被返回吗？
                }
            }
            if(connection != null) {
                connection.disconnect();
            }
        }

    }

}
