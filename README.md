# GroupCache-Java

## 前言

GroupCache-Java是GroupCache的Java语言版。GroupCache是谷歌开源的内存缓存系统（是用Go语言来实现的）。

### 为什么会去做GroupCache-Java？

目的自然是只有一个，就是锻炼自己的编码能力。

原因主要有两个：一个是年前有太长的时间没写代码，感觉很不安心，担心自己手生了，所以想写点东西练练手。另一个是之前做方案设计的时候有详细阅读过GroupCache的源码，感觉这个缓存系统的核心很清晰，也不是很复杂——然后就开始明白，原来缓存系统是这样的啊，感觉像是打开了一扇新的大门——我又是主学Java的，所以就有了用Java来实现GroupCache的想法。

### GroupCache VS Memcached

GroupCache作者Brad Fitzpatrick也是MemCached（C语言来实现的）的主要开发者，所以不可避免要对这两个内存缓存系统进行比较。<!--more-->

首先是作者对GroupCache与Memcached关系的描述：

> groupcache is a caching and cache-filling library, intended as a replacement for memcached in many cases.

根据其项目说明文档，GroupCache与MemCached的主要区别在于以下五个方面：

1. GroupCache是一个真正的分布式集群——他的分布式是在服务端来实现的。客户端无需计算数据存放在那个节点上，随机把数据发送到集群的一个节点，这个节点会自己去数据所在的节点获取数据返回给客户端。
2. 具有“**请求归并**”（`a cache filling mechanism`）的功能。简单地说就是避免“惊群效应”的产生。
3. 不支持数据的多版本，也就是不支持数据的更新。
4. 具有**热数据复制**的功能。
5. 目前只支持Go语言，而且在可预见的一段时间内，作者也不会考虑去支持其他语言。zzz~


## 使用说明

GroupCache-Java的使用方法可以参考[GroupCacheJavaDemo](https://github.com/WanFadong/GroupCacheJavaDemo)项目。

1. 将GroupCache-Java加入到项目的依赖中。
2. 使用GroupCache.java中提供的方法，初始化集群的名称、命名空间、节点ip和端口。需要提供一个读取源数据的getter方法（实现LocalGetter接口）。
3. 在上一步的集群节点的端口上启动服务器，监听HTTP请求。调用GroupCache方法，返回相应的缓存数据给用户。

注：Java1.6+

## 返回数据

+ 返回的是Result类型的JSON字符串，反序列化即可。

```
public class Result {

    private ResultCode code;
    private Object value;
}
```

+ ResultCode标识成功还是失败，以及失败的原因。下面是部分返回码及说明。

```
public enum ResultCode {
    SUCCESS,
    NOT_FOUND,// 没有找到对应数据
    BAD_REQUEST,// 用户不合法的请求(请求参数有误)
    SERVER_ERROR,// 服务器处理过程中出现异常
    NET_ERROR// 集群间访问网络错误
}
```

## TODO

因为时间比较匆忙，目前只实现了最基本的功能，仅仅是能用而已。下面需要进行功能上以及性能上的完善，包括：

+ 控制内存大小。目前是条目不超过10000条，接下来先是支持条数可配置；考虑是否可以控制占用的内存大小，而不是条目数。
+ 统计系统的状态。统计系统运行到现在到底处理了多少数据，这个GC中有，但是GCJ中没做。
+ 优化LRUCache的性能，优化锁的操作。
+ 对于某些错误进行内部重试（有可能是网络的原因），而不是直接返回失败。

以及项目中标注TODO的地方。