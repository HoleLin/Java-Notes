### Redis

#### Redis的特性

* **速度快**
  * Redis所有数据都放在内存中;
  * Redis是用C语言实现的,一般来说C语言实现的程序离操作系统更近,执行速度相对会更快;
  * Redis使用了单线程架构,预防了多线程可能产生的竞争问题;
* **基于键值对的数据结构服务器**
  * Redis 全称为REmote Dictionary Server,它主要提供了5种数据结构:**字符串**、**哈希**、**列表**、**集合**、**有序列表**，同时在字符串的基础上演变出了位图（Bitmaps）和**HyperLogLog**,随着(LBS[Location Based Service 基于位置服务]的发展,Redis3.2版本中加入了有关**GEO(地理信息定位)**的功能.
* **丰富的功能**
  * 提供了键过期功能,可以用来实现缓存;
  * 提供发布订阅功能,可以实现消息系统;
  * 支持Lua脚本,可以利用Lua创造新的Redis命令;
  * 提供了简单的事务功能,能在一定程度上保证事务特性;
  * 提供了**流水线(Piepeline)**功能,客户端可以将一批命令一次性传到Redis中,减少了网络的开销;
* **简单稳定**
* **客户端语言多**
  * Redis提供了简单TCP通信协议,客户端语言几乎涵盖了主流的编程语言,如Java,Python,C,C++,Nodejs等.
* **持久化**
  * Redis提供了两种持久化方式:RDB和AOF,即可以用两种策略将内存的数据保存到硬盘中.
* **主从复制**
  * Redis提供复制给你,实现了多个相同数据的Redis副本,复制功能是分布式Redis的基础.
* **高可用和分布式**
  * Redis从2.8版本正式提供了高可用实现Redis Sentinel,它能够保证Redis节点的故障发现和故障自动转移;
  * Redis从3.0版本正式提供了分布式筛选Redis Cluster,它是Redis真正的分布式实现,提供了高可用,读写和同类的扩展.

#### Redis使用场景

* **缓存**
* **排行榜系统**
  * 列表和有序集合数据结构
* **计数器应用**
  * 视频网站播放次数,电商网站浏览数
  * Redis天然支持计数功能而且计数性能也非常好
* **社交网络**
* **消息队列系统**

#### Redis不可做的场景

* **从数据规模的角度来看**
  * 数据可分为大规模数据和小规模数据,对于大规模数据Redis不是很适合;
* **从数据冷热的角度来看**
  * 数据可分为冷数据和热数据,热数据通常为频繁操作的数据,反之为冷数据.对于冷数据的处理,Redis不是很合适.热数据可以放在Redis中加速读写,也可以减轻后端存储的负载.

#### Redis配置,启动,操作,关闭

* **Redis Shell**

  | 可执行文件       | 作用                               |
  | ---------------- | ---------------------------------- |
  | redis-server     | 启动Redis                          |
  | redis-cli        | Redis命令行客户端                  |
  | redis-benchmark  | Redis基准测试工具                  |
  | redis-check-aof  | Redis AOF 持久化文件检测和修复工具 |
  | redis-check-dump | Redis RDB 持久化文件检测和修复工具 |
  | redis-sentinel   | 启动Redis Sentinel                 |

* **启动Redis**

  * 有三种启动方式

    * 默认配置启动

      ```shell
      ./redis-server
      ```

    * 运行配置启动

      ```shell
      ./redis-server --configKey1 configValue1 --configKey2 configValue2
      ```

    * 配置文件启动

      ```shell
      ./redis-server ${dir}/redis.conf
      ```

* **Redis命令行客户端**

  * 交互方式: `redis-cli -h {host} -p {port}`
  * 命令方式: `redis-cli -h {host} -p {port} {command}`
  * 停止服务: `redis-cli shutdown`
    * Redis关闭的过程: 断开与客户端的连接,持久化文件生成,是一种相对优雅的关闭方式.
    * 除了可以使用shutdown命令关门Redis服务外,还可以使用kill进程的方式关闭Redis,但是不要使用kill -9强制杀死Redis服务的方式,这样Redis不会做持久化操作,还会造成缓冲区等资源不能被优雅关闭,极端情况会造成AOF和复制丢失数据的情况;
    * `redis-cli  shutdown nosave|save`:代表是否在关闭Redis之前,生成持久化文件

#### API使用

* **全局命令**

  * 查看所有键: `keys *` (O(n))
  * 键总数: `dbsize`  (O(1))
  * 检查键是否存在: `exists key`
  * 删除键: `del key [key ....]`
  * 键过期: `expire key seconds`
  * 查看键剩余过期时间: `ttl key`,返回值有三种: 
    * 大于等于0的整数: 键剩余的过期时间;
    * -1: 键没设置过期时间;
    * -2: 键不存在;
  * 键的数据结构类型: `type key`,若key不存在则会返回`none`

* **数据结构和内部编码**

  * **对外的数据结构**

    * **string(字符串)**
    * **hash(哈希)**
    * **list(列表)**
    * **set(集合)**
    * **zset(有序集合)**

  * **查看内部编码:**`object encoding key`

    | 数据结构 | 内部编码   |
    | -------- | ---------- |
    |          | raw        |
    | string   | int        |
    |          | embstr     |
    | hash     | hashtable  |
    |          | ziplist    |
    | list     | linkedlist |
    |          | ziplist    |
    |          | quicklist  |
    | set      | hashtable  |
    |          | intset     |
    | zset     | skiplist   |
    |          | ziplist    |

    