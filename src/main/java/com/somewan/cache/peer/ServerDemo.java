package com.somewan.cache.peer;

import com.alibaba.fastjson.JSON;
import com.somewan.cache.result.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.StringTokenizer;

/**
 * Created by wan on 2017/1/31.
 */
public class ServerDemo implements Runnable {

    ServerSocket serverSocket;//服务器Socket

    public ServerDemo() {
        try {
            serverSocket = new ServerSocket(80);
        } catch (Exception e) {
            System.out.println("无法启动HTTP服务器:" + e.getLocalizedMessage());
        }
        if (serverSocket == null) System.exit(1);//无法开始服务器
        new Thread(this).start();
        System.out.println("HTTP服务器正在运行,端口:" + "80");
    }

    public void run() {
        while (true) {
            try {
                Socket client = null;//客户Socket
                client = serverSocket.accept();//客户机(这里是 IE 等浏览器)已经连接到当前服务器
                if (client != null) {
                    System.out.println("连接到服务器的用户:" + client);
                    try {
                        // 第一阶段: 打开输入流
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                client.getInputStream()));

                        System.out.println("客户端发送的请求信息: ***************");
                        // 读取第一行, 请求地址
                        String line = in.readLine();
                        System.out.println(line);
                        String resource = line.substring(line.indexOf('/'), line.lastIndexOf('/') - 5);
                        //获得请求的资源的地址
                        resource = URLDecoder.decode(resource, "UTF-8");//反编码 URL 地址
                        String method = new StringTokenizer(line).nextElement().toString();// 获取请求方法, GET 或者 POST

                        // 读取所有浏览器发送过来的请求参数头部信息
                        while ((line = in.readLine()) != null) {
                            System.out.println(line);

                            if (line.equals("")) break;
                        }

                        // 显示 POST 表单提交的内容, 这个内容位于请求的主体部分
                        if ("POST".equalsIgnoreCase(method)) {
                            System.out.println(in.readLine());
                        }

                        System.out.println("请求信息结束 ***************");
                        System.out.println("用户请求的资源是:" + resource);
                        System.out.println("请求的类型是: " + method);

                        // 用 writer 对客户端 socket 输出一段 HTML 代码
                        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                        out.println("HTTP/1.0 200 OK");//返回应答消息,并结束应答
                        out.println("Content-Type:text/plain;charset=UTF-8");
                        out.println();// 根据 HTTP 协议, 空行将结束头信息

                        Result res = Result.successResult("wanfadong");
                        out.println(JSON.toJSONString(res));
                        System.out.println(JSON.toJSONString(res));
                        out.close();

                        closeSocket(client);

                    } catch (Exception e) {
                        System.out.println("HTTP服务器错误:" + e.getLocalizedMessage());
                    }
                }
                //System.out.println(client+"连接到HTTP服务器");//如果加入这一句,服务器响应速度会很慢
            } catch (Exception e) {
                System.out.println("HTTP服务器错误:" + e.getLocalizedMessage());
            }
        }
    }

    /** */
    /**
     * 关闭客户端 socket 并打印一条调试信息.
     *
     * @param socket 客户端 socket.
     */
    void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println(socket + "离开了HTTP服务器");
    }


    public static void main(String[] args) {
        new ServerDemo();
    }
}
