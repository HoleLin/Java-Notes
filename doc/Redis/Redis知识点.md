### **Redis**

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

* ##### **全局命令**

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

* ##### **数据结构和内部编码**

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

* ##### **单线程架构**

  * Redis使用了单线程架构和I/O多路复用模型来实现高性能的内存数据库服务.Redis客户端与服务端的模型,每次客户端调用都经历了**发送命令,执行命令,返回结果**三个过程.
  * 因为Redis是单线程来处理命令的,所以一条命令从客户端达到服务端不会立刻被执行,所有命令都会进入到一个队列中,然后逐个被执行.
  * **单线程为什么还能这么快?**
    * 纯内存访问,Redis将所有数据放在内存中,内存的响应时长大约为100纳秒;
    * 非阻塞I/O,Redis使用epoll作为I/O多路复用技术的实现,在加上Redis自身的时间处理模型将epoll中的连接,读写,关闭都转换为事件,不在网络I/O上浪费过多的时间;
    * 单线程避免了线程切换和竞态产生的消耗;

------



* ##### **字符串**

  * 字符串类型的值可以是字符串(简单的字符串,复杂的字符串(JSON,XML)),数字(整数,浮点数),甚至为二进制(图片,音频,视频),但是值最大不能超过512MB.

  * **常用命令**

    * **设置值**:`set key value [ex seconds] [px milliseconds] [nx|xx]`

      * ex seconds: 为键设置秒级过期时间;
      * px milliseconds: 为键设置毫秒级过期时间;
      * nx: 键必须不存在,才可以设置成功,用于添加.
      * xx: 与nx相反,键必须存在,才可以设置成功,用于更新.

    * **setex,setnx,setxx**

      * `setex key seconds value`
      * `setnx key value`
      * `setxx key value`
      * `setnx`和`setxx`使用场景: 如果有多个客户端同事执行`setnx key value`,根据`setnx`的特性只有一个客户端能设置成功,`setnx`可以作为分布式锁的一种实现方案.Redis官方给出了使用`setnx`实现分布式锁的方法:https://redis.io/topics/distlock

    * **获取值**: `get key`

      * 获取的键不存在时,则返回nill(空)

    * **批量设置值**: `mset key value [key value....]`

    * **批量获取值**: `mget key [key...]`

    * 批量操作可以有效的提高开发效率

      * 不使用mget命令: **n 次get时间 = n次网络时间 + n次命令时间**
      * 使用mget命令: **n次get时间 = 1次网络时间 + n次命令时间**

    * **计数**: 

      * **自增**:`incr key`
      * **自减**:`decr key`
      * **自增指定数字**: `incrby key increment`
      * **自减指定数字**: `decrby key decrment`
      * **自增浮点数**: `incrbyfloat key incremet`
      * incr命令用于对值做自增操作,返回结果分为三种情况:
        * 值不是整数,返回错误;
        * 值是帧数,返回自增后的结果;
        * 键不存在,按照值为0自增,返回结果为1;

    * **追加值**: `append key value` 向字符串尾部追加值

    * **字符串长度**:`strlen key`

    * **设置并返回原值**:`getset key value`

    * **设置指定位置的字符**: `setrange key offset value`

    * **获取部分字符串**: `getrange key start end`

      * start和end分别是开始和结束的偏移量,偏移量从0开始计算

    * **字符串类型命令时间复杂度**

      | 命令                           | 时间复杂度                                                   |
      | ------------------------------ | ------------------------------------------------------------ |
      | set key value                  | O(1)                                                         |
      | get key                        | O(1)                                                         |
      | del key [key ...]              | O(k) k是键的个数                                             |
      | mset key value [key value ...] | O(k) k是键的个数                                             |
      | mget key [key ...]             | O(k) k是键的个数                                             |
      | incr key                       | O(1)                                                         |
      | decr key                       | O(1)                                                         |
      | incrby key increment           | O(1)                                                         |
      | decrby key decrment            | O(1)                                                         |
      | incrbyfloat key increment      | O(1)                                                         |
      | append key value               | O(1)                                                         |
      | strlen key                     | O(1)                                                         |
      | setrange key offset value      | O(1)                                                         |
      | getrange key start end         | O(n) n为字符串长度,由于获取字符串非常快,若字符串不是很长,可视为O(1) |

    * 内存编码

      * 字符串类型内部编码有3种:
        * int: 8个字节的长整型;
        * embstr: 小于等于39个字节的字符串;
        * raw: 大于39个字节的字符串;
      * Redis会更根据当前值的类型和长度决定使用哪种内部编码实现.

  * **典型使用场景**

    * 缓存功能

    * 计数

    * 共享Session

    * 限速

      * 限制用户每分钟获取验证码的频率,如一分钟不能超过5次
      * 网站限制一个IP地址不能一秒钟之内访问超过n次

      ```
      伪代码:
      phoneNum = "132xxxxxx";
      key = "shortMsg:limit:" + phoneNum;
      // SET key value EX 60 NX
      isExists = redis.set(key,1,"EX 60","NX");
      if(isExists != null || redis.incr(key) <=5 ){
      	// 通过
      }else{
      	// 限速
      }
      ```

------



* ##### **哈希**

  * 键值对 value= {{field1,value},....{fieldN,valueN}}

  * **命令**

    * **设置值:** `hset key field value`
    * **获取值**: `hget key field`
      * 若键或者field不存在,则会返回nil
    * **删除field**: `hdel key field [field ...]`
      * hdel会删除一个或多个field,返回结果为成功删除的field个数
    * **计算field个数**: `hlen key`
    * **批量设置或获取field-value**:
      * `hmget key field [field ....]`
      * `hmset key field value [field value ...]`
    * **判断field是否存在**: `hexists key field`
    * **获取所有field**: `hkeys key`
    * **获取所有的value**: `hvals key`
    * **获取所有的field-value**: `hgetall key`
      * 使用过hgetall的时候,若哈希元素个数比较多,会存在阻塞Redis的可能,若一定要获取全部field-value,可以使用`hscan`命令,该命令会渐进式遍历哈希类型.
    * **hincrby hincrbyfloat** 
      * `hincrby key field`
      * `hincrbyfloat key field`
    * **计算value的字符串长度(Redis3.2以上)**:`hstrlen key field`

  * **哈希类型命令的时间复杂度**

    | 命令                                    | 时间复杂度        |
    | --------------------------------------- | ----------------- |
    | hset key field value                    | O(1)              |
    | hget key field                          | O(1)              |
    | hdel key field [field ...]              | O(k),k是field个数 |
    | hlen key                                | O(1)              |
    | hgetall key                             | O(n),n是field总数 |
    | hgmet key field [field ...]             | O(k),k是field个数 |
    | hmset key field value [field value ...] | O(k),k是field个数 |
    | hexists key field                       | O(1)              |
    | hkeys key                               | O(n),n是field总数 |
    | hvals key                               | O(n),n是field总数 |
    | hsetnx key field value                  | O(1)              |
    | hincrby key field increment             | O(1)                  |
    | hincrbyfloat key field increment        | O(1)                  |
    | hstrlen key field                       | O(1)                  |

  * **内部编码**
  
    * 哈希类型的内部编码有两种:
      * **zplist(压缩列表)**: 当**哈希类型元素个数小于hash-max-ziplist-entries配置(默认512个)**,**同时所有值都小于hash-max-ziplist-value配置(默认64字节)时,Redis会使用ziplist作为哈希的内部实现**,ziplist使用更加紧凑的结构实现多个元素的连续存储,所有在节省内存方面比hashtable更加优秀.
      * **hashtable(哈希表)**: 当哈希类型无法满足ziplist的条件时,Redis会使用hashtable作为哈希的内部实现,因此此时ziplist的读写效率会下降,而hashtable的读写时间复杂度为O(1).

------



* **列表**

  * **描述**

    > 列表(list)类型是用来存储多个有序的字符串.列表中的每个字符串称为元素(element),一个列表最多可以存储2<sup>32</sup>-1个元素.
    >
    > Redis中,可以对列表两端插入(push)和弹出(pop),还可以指定范围的元素列表,获取指定索引下标的元素

  * **特点**: 

    * 列表元素是有序的;
    * 列表元素是可以重复的;

  * **命令**

    * **列表的四种操作类型**

      * **添加**: rpush lpush linsert
      * **查**: lrange lindex llen
      * **删除**: lpop rpop lrem ltrim
      * **修改**: lset
      * **阻塞操作**: blpop brpop

    * **添加操作**

      * 从右边插入元素: `rpush key value [value ...]` 
      * 从左边插入元素: `lpush key value [value ...]` 
      * 向某个元素前或者后插入元素:`linsert key before|after pivot value`
        * linsert命令会从列表中找到等于pivot的元素,在其前(before)或者后(after)插入一个新的元素value

    * **查找操作**

      * 获取指定范围内的元素列表:`lrange key strat end`

        * 索引下标有两个特点:

          > * 索引下标从左到右分别是0到N-1,但是从右到左分别是-1到N
          > * lrange中end选项包含了自身,**左闭由闭**

      * 获取列表指定索引下标元素: `lindex key index`

      * 获取列表长度: `llen key`

    * **删除**

      * 从列表左侧弹出元素: `lpop key`
      * 从列表右侧弹出元素: `rpop key`
      * 删除指定元素: `lrem key count value`
        * lrem会从列表中找到等于value的元素进行删除,根据count不同分为三种情况:
          * count>0,从左到右,删除最多count个元素
          * count<0,从右到左,删除最多count绝对值个元素
          * count=0,删除所有
      * 按照索引范围修剪列表:`ltrim key start end`

    * **修改**

      * 修改指定索引下标的元素:`lset key index newValue`

    * **阻塞操作**

      * 阻塞式弹出:

        * `blpop key [key...] timeout `
        * `brpop key [key...] timeout`
        * key [key...],多个列表的键
        * timeout: 阻塞时间(单位秒)

      * 列表为空:  若timeout=3,那么客户端要等到3秒后返回,若timeout=0,那客户端会一直阻塞等下去;

      * 列表不为空: 客户端会立即返回 

      * 使用brpop时,有两点注意:

        * 若是多个建,那么brpop会从左只有遍历键,一旦有一个键能弹出元素,客户端立即返回;
        * 若多个客户端对同一个键执行brpop,那么最新执行brpop命令的客户端可以获取弹出的值;

      * **列表命令时间复杂度**

        | 命令                                  | 时间复杂度                                |
        | ------------------------------------- | ----------------------------------------- |
        | rpush key value [value...]            | O(k) ,k是元素个数                         |
        | lpush key value [value...]            | O(k) ,k是元素个数                         |
        | linsert key before\|after pivot value | O(n),n是pivot距离列表头或为的距离         |
        | lrange key start end                  | O(s+n),s是start偏移量,n是start到end的范围 |
        | lindex key index                      | O(n),n是索引的偏移量                      |
        | llen key                              | O(1)                                      |
        | lpop key                              | O(1)                                      |
        | rpop key                              | O(1)                                      |
        | lrem count value                      | O(n),n是列表长度                          |
        | ltrim key start end                   | O(n),n是要剪裁的元素总数                  |
        | lset key index value                  | O(n),n是索引的偏移量                      |
        | blpop brpop                           | O(1)                                      |

  * **内部编码**

    * 列表类型的内部编码有两种:
      * **ziplist(压缩列表)**: **当列表的元素个数小于list-max-ziplist-entries配置(默认512个)**,**同时列表中每个元素的值都小于list-max-ziplist-value配置时(默认64字节)**.
      * **linkedlist(链表)**: 当列表类型无法满足ziplist的条件时,Redis会使用linkedlist作为列表的内部实现;

  * **口诀:**

    * lpush+lpop=Stact(栈)
    * lpush+rpop=Queue(队列)
    * lpush+ltrim=Capped Collection(有限集合)
    * lpush+brpop=Message Queue(消息队列)


------

* **集合**

  * **理论**

    > * 集合(set)类型可以用来保存多个字符串元素,但是集合中不允许有重复元素,且集合的元素是无序的,不能通过索引下标获取元素.
    > * 一个集合最多可以存储2<sup>32</sup>-1个元素.
    > * Redis除了支持集合内的增删改查,同事还支持多个集合去交集,并集,差集.

  * **命令**

    * 添加元素: `sadd key element [element...]`,返回添加成功的元素个数;
    * 删除元素: `srem key element [element...]`,返回添加删除的元素个数;
    * 计算元素个数:`scard key`
    * 判断元素是否在集合中: `sismenber key element`
      * 在,返回1
      * 不在,返回0
    * 随机从集合返回指定个数元素:`srandmember key [count]`
    * 随机从集合中弹出元素: `spop key`,会从集合中删除被弹出的元素
    * 获取所有元素:`smembers key`
    * 集合间操作
      * 求多个集合的**交集**: `sinter key [key...]`
      * 求多个集合的**并集**: `sunion key [key...]`
      * 求多个集合的**差集**: `sdiff key [key...]`
      * 将交集,并集,差集的结果保存
        * `sinterstore destination key [key...]`
        * `sunionstore destination key [key...]`
        * `sdiffstore destination key [key...]`

  * **集合常用命令时间复杂度**

    | 命令                           | 时间复杂度                                     |
    | ------------------------------ | ---------------------------------------------- |
    | sadd key element [element ...] | O(k),k是元素个数                               |
    | srem key element [element...]  | O(k),k是元素个数                               |
    | scard key                      | O(1)                                           |
    | sismember key element          | O(1)                                           |
    | srandmember key [count]        | O(count)                                       |
    | spop key                       | O(1)                                           |
    | smembers key                   | O(n),n是元素总数                               |
    | sinter key [key...]            | O(n*k),k是多个集合中元素最少的个数,m是键的个数 |
    | sunion key [key...]            | O(k),k是多个集合元素个数和                     |
    | sdiff key [key...]             | O(k),k是多个集合元素个数和                     |

  * **内部编码**

    * **intset(整数集合)**: 当集合中的元素都是整数且元素个数小于set-maxinset-entries配置(默认512个)时,Redis会选用intset来作为集合的内部实现,从而减少内存的使用.
    * **hashtable(哈希表)**: 当集合类型无法满足intset的条件是,Redis会使用hashtable作为集合的内部实现.

  * **使用场景**

    * 标签(tag): 用户的兴趣爱好
    * sadd=Tagging(标签)
    * spop/srandmember=Random item(生成随机数,比如抽奖)
    * sadd+sinter=Social Graph(社交需求)

------

* **有序集合**

  * **理论**

    > * 集合(无重复元素);
    > * 可以排序;
    > * 有序集合提供了获取指定分数和元素范围查询,计算成员排名等功能.
    > * 有序集合中元素不能重复,但是score可以重复;

  * **列表,集合,有序集合三者的异同点**

    | 数据结构 | 是否允许重复元素 | 是否有序 | 有序实现方式 | 应用场景          |
    | -------- | ---------------- | -------- | ------------ | ----------------- |
    | 列表     | 是               | 是       | 索引下标     | 时间轴,消息队列等 |
    | 集合     | 否               | 否       | 无           | 标签,社交等       |
    | 有序集合 | 否               | 是       | 分值         | 排行榜系统,社交   |

  * **命令**

    * 添加成员:`zadd key socre member [score member ...]`

      * Redis3.2为zadd命令添加了nx,xx,ch,incr四个选项:
        * nx:member必须不存在,才可以设置成功,用于添加;
        * xx:member必须存在,才可以设置成功,用于更新;
        * ch:返回此次操作后,有序集合元素和分数变化的个数;
        * incr: 对socre做增加,相当于zincrby;
      * 有序集合相比集合提供了排序字段,但也产生了代价,zadd的时间复杂度为O(log(n)),sadd的时间复杂度为O(1);

    * 计算成员个数:`zcard key`

    * 计算某个成员的分数:`zscore key member`

    * 计算成员的排名

      * 分数从低到高排名:`zrank key member`
      * 分数从高到低排名:`zrevrank key member`

    * 删除成员:`zrem key member [member...]`

    * 增加成员的分数: `zincrby key increment member`

    * 返回指定排名范围的成员

      * 按照分值排名,从低到高:`zrange key start end [withscores]`
      * 按照分值排名,从高到低:`zrevrange key start end [withscores]`

    * 返回指定分数范围的成员

      * 按照分数从低到高:`zrangebyscore key min max [withscores] [limit offset count]`
      * 按照分数从高到低`zrevrangebyscore key max min [withscores] [limit offset count]`
      * withscores选项会同时返回每个成员的分数;
      * limit offset count 选项可以限制输出的起始位置和个数;
      * min和max还支持开区间(小括号)和闭区间(中括号),-inf和+inf分别代表无限小和无限大
        * `zrangebysocre user:ranking (200 +inf withsocres`

    * 返回指定分数范围成员个数:`zcount key min max`

    * 删除指定排名内升序元素:`zremrangebyrank key start end`

    * 删除指定分数范围的成员:`zremrangebyscore key min max`

    * 集合间

      * 交集: `zinterscore destination numkeys key [key...] [weights weight [weight ...]] [aggregate sum|min|max]`
        * destination:交集计算结果保存到的键的名称
        * numkeys: 需要做交集计算键的个数;
        * key [key ...]: 需要做交集计算的键
        * weight weight[weight...]: 每个键的权重,在做交集计算时,每个键中的每个member会将自己分数乘以这个权重,每个键的权重默认为1;
        * aggregate sum|min|max: 计算成员交集后,分值可以按照sum(和),min(最小值),max(最大值)做汇总,默认值为sum
      * 并集:`zunionscore destination numkeys key [key...] [weights weight [weight ...]] [aggregate sum|min|max]`

    * 有序集合命令的时间复杂度

      | 命令                                          | 时间复杂度                                                   |
      | --------------------------------------------- | ------------------------------------------------------------ |
      | zadd key score member [score member...]       | O(k*log(n)),k是添加成员的个数,n是当前有序集合成员个数        |
      | zcard key                                     | O(1)                                                         |
      | zscore key member                             | O(1)                                                         |
      | zrank key member                              | O(log(n)),n是当前有序集合成员个数                            |
      | zrevrank key member                           | O(log(n)),n是当前有序集合成员个数                            |
      | zrem key member [member...]                   | O(k*log(n)),k是删除成员的个数,n是当前有序集合成员个数        |
      | zincrby key increment member                  | O(log(n)),n是当前有序集合成员个数                            |
      | zrange key start end [withscores]             | O(k+log(n)),k是获取成员的个数,n是当前有序集合成员个数        |
      | zrevrange key start end [withscores]          | O(k+log(n)),k是获取成员的个数,n是当前有序集合成员个数        |
      | zrangebyscore key min max [withscores]        | O(k+log(n)),k是获取成员的个数,n是当前有序集合成员个数        |
      | zrebvrangebyscore key min max [withscores]    | O(k+log(n)),k是获取成员的个数,n是当前有序集合成员个数        |
      | zcount                                        | O(log(n)),n是当前有序集合成员个数                            |
      | zremrangebyrank key start end                 | O(k+log(n)),k是删除成员的个数,n是当前有序集合成员个数        |
      | zremrangebyscore key min max                  | O(k+log(n)),k是删除成员的个数,n是当前有序集合成员个数        |
      | zinterscore destination numkeys key [key ...] | O(n*k)+O(m*log(m)),n是成员数最小的有序集合成员个数,k是有序集合的个数,k是有序集合的个数,m是结果集中成员个数. |
      | zunionscore destination numkeys key [key ...] | O(n)+O(m*log(m)),n是所有有序集合成员个数和,m是结果集中成员个数. |

  * **内部编码**

    * **ziplist(压缩列表)**: 当有序集合的元素个数小于zet-max-ziplist-entries配置(默认128个),同事每个元素的值都小于zset-max-ziplist-value配置(默认64字节)时,Reids会用ziplist作为有序集合的内部实现,ziplist可以有效减少内存的使用.
    * **skiplist(跳表)**: 当ziplist条件不满足时,有序集合会使用skiplist作为内部实现,因为此时ziplist的读写效率会下降.

  * **使用场景**

    * 排行榜系统

------



* **键管理**

  * **单个键管理**

    * **键重命名**:`rename key newkey`

      * 为了防止被强行rename,Redis提供了renamenx命令,确保只有newKey不存在的时候才被覆盖.

      * 由于重命名键期间会执行del命令杀出旧的键,如果键对应的值比较大,会存在阻塞Redis的可能性.

      * 如果rename和renamenx中key和newKey相同,在Redis3.2和之前版本返回结果略有不同

        > Redis 3.2 
        >
        > rename key key
        >
        > OK
        >
        > Redis3.2之前的版本会提示错误
        >
        > rename key key 
        >
        > (error) ERR souce and destination objects are the same

    * **随机返回一个键**: `randomkey`

    * **键过期**

      * `expire key seconds`: 键在seconds秒后过期
      * `expireat key timestamp`: 键在秒级时间戳timestamp后过期
      * `ttl`和`pttl`都可以查询键的剩余过期时间,但`pttl`精度更高可以达到毫秒级别,有3种返回值:
        * 大于等于0的整数:键剩余的过期时间(ttl是秒,pttl是毫秒);
        * -1: 键没有设置过期时间;
        * -2:键不存在;
      * `expireat`命令可以设置键的秒级过期时间戳
      * Redis2.6版本后提供毫秒级的过期方案:
        * `pexpire key milliseconds`: 键在milliseconds毫秒后过期.
        * `pexpireat key milliseconds-timestamp`: 键在毫秒级时间戳timestamp后过期;
      * `persist`命令可以将键的过期时间清除;
      * `setex`命令作为set+expire的组合,不但是原子执行,同时减少了一次网络通讯的时间;
      * Redis不支持二级数据结构(列如哈希,列表)内部元素的过期功能,列如不能对列表类型的一个元素做过期时间设置;
      
    * **迁移键**

      * `move key db`

      * `dump key`

      * `restore key ttl value`

      * `dump+restore`可以实现在不同的Redis实例之间进行数据迁移的功能,整个迁移的过程分为两步:

        * 在源Redis上,`dump`命令会将键值序列化,格式采用的是RDB格式.
        * 在目标Redis上,`restore`命令将上面序列化的值进行复原,其中ttl参数代表过期时间,如果ttl=0代表没有过期时间.
        * `dump+restore`有两点需要注意:
          * 整个迁移过程并非与原子性的,而是通过客户端分步完成的
          * 迁移过程是开启了两个客户端连接,所以dump的过程不是源Redis和目标Redis之间进行传输.

      * `migrate host port key|""  destination-db timeout [copy] [replace] [keys key [key ...]]`

        >  host: 目标Redis的IP地址
        >
        > port: 目标Redis的端口
        >
        > key|"": 在Redis3.0.6版本之前,migrate只支持迁移一个键,所以此处是要迁移的键,但在Redis3.0.6版本之后支持迁移多个键,多个当前要迁移多个键,此处为空字符串"".
        >
        > destination-db: 目标Redis的数据库索引,例如要迁移到0号数据库,这里就写0.
        >
        > timeout: 迁移的超时时间(单位为毫秒)
        >
        > [copy] : 如果添加此选项,迁移后并不删除源键
        >
        > [replace] : 如果添加此选项,migrate不管目标Redis是否存在该键都会正常迁移进行数据覆盖.
        >
        > [keys  key [key...]: 迁移多个键,列如要迁移key1,key2,key3,此处填写"keys key1 key2 key3"

        * migrate命令就是将dump,restore,del三个命令进行组合,从而简化操作流程.
        * migrate命令具有原子性
        * 实现过程和dump+restore基本类似,但有3点不太相同:
          * 整个过程是原子执行的,不需要在多个Redis实例上开启客户端,只需要在源Redis上执行migrate命令即可.
          * migrate命令的数据传输直接在源Redis和目标Redis上完成的.
          * 目标Redis完成restore后会发送OK给源Redis,源Redis接收后会根据migrate对应的选项来决定是否在源Redis上删除对应的键; 
        
      * move,dump+restore,migrate三个命令比较
      
        | 命令         | 作用域        | 原子性 | 支持多个键 |
        | ------------ | ------------- | ------ | ---------- |
        | move         | Redis实例内部 | 是     | 否         |
        | dump+restore | Redis实例之间 | 否     | 否         |
        | migrate      | Redis实例之间 | 是     | 是         |
      
    * **遍历键**

      * 全量遍历键: `key pattern`
        * *代表匹配任意字符;
        * .代表匹配一个字符;
        * []代表匹配部分字符,例如[1,3]代表匹配1,3,[1-10]代表匹配1到10的任意数字;
        * \x用来做转义,例如要匹配星号,问号需要进行转义;
      * 渐进式遍历: `scan cursor [match pattern] [count number]`
        * cursor是必需参数,实际上cursor是一个游标,第一次遍历从0开始,每次sacn遍历完都会返回当前游标的值,直到游标为0,表示遍历结束.
        * match pattern是可选参数,它的作用是做模式的匹配的,和keys的模式匹配很像.
        * count number是可选参数,它的作用是表明每次要遍历的键个数,默认值是10,此参数可以适当增大.
        * Redis从2.8版本后,提供了新的命令scan,它能有效的解决keys命令可能存在阻塞问题,每次scan命令的时间复杂度是O(1)

------



* **数据库管理**
  * 切换数据库: `select dbIndex`
  * Redis默认配置中有16个数据库;
  * 清除数据库
    * 清除当前数据库:`flushdb`
    * 清除所有数据库: `flushall`
    * 若当前数据库键值数量比较多,flushdb/flushall存在阻塞Redis的可能性;

------

* **慢查询**

  * 所谓慢查询日志就是系统在命令执行前后计算每条命令的执行时间,当超过预设阀值,就将这条命令记录下来;

  * Redis客户端执行一条命令分为如下四个部分:

    * 发送命令
    * 加入命令队列
    * **命令执行(慢查询只统计该步骤的耗时)**
    * 返回结果

  * 预设阀值: `slowlog-log-slower-than`(单位微妙,1秒=1000毫秒=1000000微妙),默认值是10000(10毫秒)

    * 若 `slowlog-log-slower-than`=0,则会记录所有的命令;
    * 若 `slowlog-log-slower-than`<0,则不会记录任何记录;

  * 慢查询日志最多存储多少条: `slowlog-max-len`

  * 在Redis中有两种修改配置的方法:

    * 修改配置文件

    * 使用`config set`命令动态修改

      ```yaml
      config set slowlog-log-slower-than 20000
      config set slowlog-max-len 1000
      config rewrite
      ```

  * 获取慢查询日志:`slowlog get [n]`

    * n指定条数
    * 每个慢查询日志有4个属性组成:**慢查询日志的标志id,发生时间戳,命令耗时,执行命令和参数**

  * 获取慢查询日志列表当前长度:`slowlog len`

  * 慢查询日志重置:`slowlog reset`

* **redis-cli**

  * -r(repeat):代表命令将执行多次;
    * `redis-cli -r 3 ping`
  * -i(interval):代表每个几秒执行一次命令,但是-i选项必须和-r选项一起使用,**单位是秒,不支持毫秒为单位**;
  * -x:代表从标准输入(stdin)读取数据作为redis-cli的最后一个参数;
    * `echo "world" | redis-cli -x set hello`
  * -c(cluster):连接Redis cluster节点时需要使用,-c选项可以防止moved和ask异常;
  * -a(auth):若Redis配置了密码,则需要使用;
  * --scan和--pattern用于扫描指定模式的键,相当于使用sacan命令;
  * --slave:把当前客户端模拟成当前Redis节点的从节点,可以用来获取当前节点的更新操作;
  * --rdb:会请求Redis实例生成并发送RDB持久化文件,保存在本地;可以用来持久化文件的定期备份
  * --pipe:用于将命令封装成Redis同行协议定的格式,批量发送给Redis执行;
  * --bigkeys: 使用scan命令对Redis的键进行采样,从中找到内存占用比较大的键值.
  * --eval:用于执行Lua脚本
  * --latency:
    * --latency: 测试客户端到目标Redis的网络延迟;
    * --latency-history:分时段的形式展示延迟信息
    * --latency-dist: 使用统计图表的形式从控制台输出延迟统计信息
  * --stat: 实时获取Redis的重要统计信息
  * --raw和--no-raw
    * --no-raw要求命令返回结果必须是原始的格式
    * --no-raw要求命令返回结果必须是格式化后的

* **Pipeline**

  * Redis客户端执行一条命令分为如下四个部分:

    1. 发送命令

    2. 加入命令队列
    3. 命令执行
    4. 返回结果

  * 1~4称为Round Trip Time (RTT,往返时间)

  * Redis提供的批量操作命令(mget,mset),可以有效的节约RTT,使用Pipeline执行n次命令,整个过程需要1次RTT.

  * 原生批量命令和Pipeline对比

    * 原生批量命令是原子的,Pipeline是非原子的
    * 原生批量命令是一个命令对应多个key,Pipeline支持多个命令
    * 原生批量命令是Redis服务端支持实现的,而Pipeline需要服务端和客户端的共同实现.

* **事务**

  * Redis提供了简单的事务功能,将一组需要一起执行的命令放到`multi`和`exec`两个命令之间.`multi`命令代表事务开始,`exec`命令代表事务结束,它们之间的命令是原子顺序执行的.
  * 若出现语法错误,会造成整个事务无法执行.

* **Lua脚本**

  * 将Lua脚本加载到Redis内存中:`script load`
  * 判断Lua脚本加载到内存中的生成的sha码是否存在:`script exists sha [sha...]`
  * 清除内存中已加载的所有Lua脚本:`script flush`
  * 杀死正在执行的Lua脚本:`script kill`

------

* **Bitmaps**

  * Bitmaps可以想象成一个以位为单位的数组,数组的每个单元只能存储0或1,数组的下标在Bitmaps中叫做偏移量;

  * **命令:**

    * 设置值:`setbit key offset value`
    * 获取值:`getbit key offset`
    * 获取Bitmaps指定范围值为1的个数: `bitcount [stard] [end]`
    * Bitmaps间的运算:`bitop op destkey key [key ...]`,bitop是一个复合操作,它可以做多个Bitmaps的支持and(交集),or(并集),not(非),xor(异或)操作并将结果保存到destkey中
    * 计算Bitmaps中第一个值为targetBit的偏移量:`bitpos key target [start][end]`
    

  ------

* **HyperLogLog**

  * **命令**
  * 添加:`padd key element [element...]`
    * 计算独立用户数: `pfcount key [key...]`
  * 合并: `pfmerge destkey sourcekey [sourcekey...]`
  * HyperLogLog内存占用量非常小,但是存在错误率,故使用时要注意两点:
    * 只为了计算独立总数,不需要获取单条数据;
    * 可以容忍一定错误率
  
  ------
  
* **发布订阅**

  * 命令:
    * 发布消息:`publish channel message`
    * 订阅消息:`subscribe channel [channel...]`
    * 取消订阅:`unsubscribe channel [channel]`
    * 按照模式订阅和取消订阅:
      * `psubscribe pattern [pattern...]`
      * `punsubscribe pattern [pattern...]`
    * 查看活跃的频道:`pubsub channels [pattern]`
    * 查看频道订阅数:`pubsub numsub [channel...]`
    * 查看模式订阅数:`pubsub numpat`
  * 注意点:
    * 客户端在执行订阅命令之后进入订阅状态,只能接受subscribe,psubscribe,unsubscribe,punsubscribe四个命令;
    * 新开启的订阅客户端,无法收到该频道之前的消息,因为Redis不会对发布的消息进行持久化.

  ------

* 客户端通信协议

  * 几乎所有的主流编程语言都有Redis的客户端

    * 客户端与服务端之间的通信协议是在TCP协议之上构建的
    * Redis指定了RESP(REdis Serialization Protocol,Redis序列化协议)实现客户端与服务端的正常交互

  * **发送命令格式**

    ```
    *<参数数量> CRLF
    $<参数1的字节数量> CRLF
    <参数1> CRLF
    ...
    $<参数N的字节数量> CRLF
    <参数N> CRLF
    
    // 示例 set hello world
    *3
    $3
    SET
    $5
    hello
    $5
    world
    ```

  * **返回结果格式** 

    * Redis返回结果类型分为以下五种:
    * 状态回复: 在RESP中第一个字节为"+";
       * 错误回复: 在RESP中第一个字节为"-";
      * 证书回复: 在RESP中第一个字节为": ";
    * 字符串回复: 在RESP中第一个字节为"$";
    * 多条字符串回复: 在RESP中第一个字节为"*";
  
  ------
  
  
  
* **客户端API**

  * **client list**

    * `client list`命令能列出与Redis服务端相连的所有客户端连接信息

      ```shell
      127.0.0.1:6379> client list
      id=47 addr=127.0.0.1:58708 fd=8 name= age=5 idle=0 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=26 qbuf-free=32742 obl=0 oll=0 omem=0 events=r cmd=client user=default
      
      * 标识: id,addr,fd name
      id: 客户端连接的唯一标识,这个id是随着Redis的连接自增的,重启Redis后会重置为0;
      addr: 客户端连接的ip和端口;
      fd: socket的文件描述符,与lsof命令结果中的fd是同一个,如果fd=-1代表当前客户端不是外部客户端,而是Redis内部的伪装客户端.
      name: 客户端的名字,可以通过client setName和client getName两个命令来设置和获取
      
      * 输入缓冲区: qbuf,qbuf-free
      Redis为每个客户端分配了输入缓存区,它的作用是将客户端发送的命令临时保存,同事Redis从输入缓冲区拉取命令并执行,输入缓冲区为客户端发送命令到Redis执行命令提供了缓存功能.
      qbuf和qbuf-free分别代表这个缓存区的总容量和剩余容量Redis没有提供相应的配置来规定每个缓冲区的大小,输入缓冲区会根据输入内容大小的不同动态调整,只是要求每个客户端缓冲区的大小不能超过1G,c超过后客户端将被关闭.
      ```
      
    * 输入缓冲区使用不当会产生两个问题:

      * 一旦某个客户端的输入缓冲区超过1G,客户端将会被关闭.
      * 输入缓冲区不收maxmemory控制,假设一个Redis实例设置了maxmenmory为4G,已经存储了2G数据,但是此时输入缓冲区使用了3G,已经超过maxmemory限制,可能产生数据丢失,键值淘汰,OOM等情况

    * 监控输入缓冲区异常的方法有两种:

      * 通过定期执行client list 命令,收集qbuf和qbuf-free找到异常的连接记录并分析,最终找到可能出现问题的客户端

      * 通过info命令的info clients模块,找到最大的输入缓冲区

        | 命令         | 优点                                     | 缺点                                                         |
        | ------------ | ---------------------------------------- | ------------------------------------------------------------ |
        | client list  | 能精准分析给个客户端来定位               | 执行速度较慢(尤其在连接数较多的情况下),频繁执行存在阻塞Redis的可能 |
        | info clients | 执行速度比client list快,分析过程较为简单 | 不能精准定位到客户端;不能显示所有缓冲区的总量,只能显示最大量 |

    * 输出缓冲区: obl,oll,omem

      * Redis为每个客户端分配了输出缓冲区,它的作用是保存命令执行的结果返回给客户端,为Redis和客户端交互返回结果提供缓冲;

      * 与输入缓冲区不同的是,输出缓冲区的容量可以通过参数`client-output-buffer-limit`来进行设置,并且输出缓冲区做的更加细致,按照客户端的不同分为三种:普通客户端,发布订阅客户端,slave客户端.

      * 对应的配置规则是:`client-output-buffer-limit <class> <hard limit> <soft limit> <soft seconds>`

        * <class>: 客户端类型,分为三种:

          * normal: 普通客户端
          * slave: slave客户端,用于复制
          * pubsub: 发布订阅客户端

        * <hard limit>: 如果客户端使用的输出缓冲区大于<hard limit>,客户端会被立即关闭;

        * <soft limit>和<soft seconds>: 如果客户端使用的输出缓冲区超过了<soft limit>并且持续了<soft limit>秒,客户端会被立即关闭.

        * Redis的默认配置是:

          ```
          client-output-buffer-limit normal 0 0 0
          client-output-buffer-limit slave 256mb 64mb 60
          client-output-buffer-limit pubsub 32mb 8mb 60
          ```

        * 和输入缓冲区相同是,输出缓冲区也不受到maxmemory的限制,实际上输出缓冲区由两部分组成:固定缓冲区(16 KB)和动态缓冲区,其中固定缓冲区返回比较小的执行结果,而动态缓冲区返回比较大的结果

        * client list中的obl代表固定缓冲区的长度,oll代表动态缓冲区列表的长度,omem代表使用的字节数.

    * 客户端的存活状态

      * client list中age和idle分别代表当前客户端已经连接的时间和最近一次的空闲时间

    * 客户端的限制maxclients和timeout

      * maxclients参数来限制最大客户端连接数,一旦连接数超过maxclients,新的连接将被拒绝.maxclients默认值是100000
      * config set maxclients 对最大客户端连接数动态设置

    * 客户端类型

      * client list中flag是用于标识当前客户端的类型

      | 序号 | 客户端类型 | 说明                                              |
      | ---- | ---------- | ------------------------------------------------- |
      | 1    | N          | 普通客户端                                        |
      | 2    | M          | 当前客户端是master节点                            |
      | 3    | S          | 当前客户端是slave节点                             |
      | 4    | O          | 当前客户端正在执行monitor命令                     |
      | 5    | x          | 当前客户端正在执行事务                            |
      | 6    | b          | 当前客户端正在等待阻塞事件                        |
      | 7    | i          | 当前客户端正在等待VM I/O,但此状态目前已经废弃不用 |
      | 8    | d          | 一个受监视的键已被修改,EXEC命令将失败             |
      | 9    | u          | 客户端未被阻塞                                    |
      | 10   | c          | 回复完整输出后,关闭连接                           |
      | 11   | A          | 尽可能地快速关闭连接                              |
    
  * client list命令结果的全部属性

    | 序号 | 参数      | 含义                                                    |
    | ---- | --------- | ------------------------------------------------------- |
    | 1    | id        | 客户端连接id                                            |
    | 2    | addr      | 客户端连接IP和端口                                      |
    | 3    | fd        | socket的文件描述符                                      |
    | 4    | name      | 客户端连接名称                                          |
    | 5    | age       | 客户端连接存活时间                                      |
    | 6    | idle      | 客户端连接空闲时间                                      |
    | 7    | flags     | 客户端类型标识                                          |
    | 8    | db        | 当前客户端正在使用的数据库索引下标                      |
    | 9    | sub/psub  | 当前客户端订阅的频道或者模式数                          |
    | 10   | multi     | 当前事务中已执行命令个数                                |
    | 11   | qbuf      | 输入缓冲区总容量                                        |
    | 12   | qbuf-free | 输入缓冲区剩余容量                                      |
    | 13   | obl       | 固定缓冲区的长度                                        |
    | 14   | oll       | 动态缓冲区的长度                                        |
    | 15   | omem      | 固定缓冲区和动态缓冲区使用的容量                        |
    | 16   | events    | 文件描述符事件(r/w): r和w分别代表客户端套接字可读和可写 |
    | 17   | cmd       | 当前客户端最后一次执行的命令,不包含参数                 |

  * 用于杀掉指定IP地址和端口的客户端:`client kill ip:port`

  * 用于阻塞客户端timeout毫秒数,在此期间客户端连接将被阻塞: `client pause timeout(毫秒)`

    * client pause只对普通和发布订阅客户端有效,对主从复制(从节点内部伪装了一个客户端)是无效的,因此在期间主从复制是正常进行的
    * client pause可以用一种可控的方式将客户端连接从一个Redis节点切换到另一个Redis节点

  * **monitor**

    * monitor命令用于监控Redis正在执行的命令;

  * 客户端相关配置

    * timeout: 检测客户端空闲连接的超时时间,一旦idle时间达到了timeout,客户端将会被关闭,如果设置为0则不会进行检测;
    * maxclients: 客户端最大连接数
    * tcp-keepalive: 检测TCP连接活性的周期,默认值为0,也就是并进行检测,若要设置,则可以设置为60,那么Redis会每隔60秒对它创建的TCP连接进行活性检测,防止大量死链接占用系统资源.
    * tcp-backlog: TCP三次握手后,会将接受的连接放入队列中,tcp-backlog就是队列的大小,它在Redis中Redis中的默认值是511.通常来讲这个参数不需要调整,但是这个参数会受到操作系统的影响.

------

 * **持久化**

   * **RDB(Redis DataBase)**

     * RDB持久化是把当前进程数据生成快照保存到硬盘的过程,触发RDB持久化过程分为手动触发和自动触发.

     * 手动触发分别对应save和bgsave命令

       * `save`命令: 阻塞当前Redis服务器,直到RDB过程完成为止,对于内存比较大的实例会造成长时间阻塞[**废弃**]
       * `bgsave`命令:Redis进程执行fork操作创建子进程,RDB持久化过程有子进程负责,完成后自动结束.阻塞只发生在fork阶段,一般时间很短

     * 除了执行命令手动触发外,Redis内部还存在自动触发的RDB的持久化机制:

       * 使用save相关配置,如"save m n",表示m秒内数据集存在n次修改时,自动触发bgsave;
       * 若从节点执行全量复制操作,主节点自动执行bgsave生成RDB文件并发送给从节点;
       * 执行debug reload命令重新加载Redis,也会自动触发bgsave操作;
       * 默认情况下执行shutdown命令,若没有开启AOF持久化则自动执行bgsave;

     * bgsave是主流的触发RDB持久化方式

       * 执行bgsave命令,Redis父进程判断当前是否存在正在执行的子进程,如RDB/AOF子进程,若存在bgsave命令直接返回;
       * 父进程执行fork操作创建子进程,fork操作过程中父进程会阻塞,通过info stats命令查看latest_fork_usec选项,可以获取最近一个fork操作的耗时,单位为微妙;
       * 父进程fork完成后,bgsave命令返回"Backgroud saving started"信息并不再阻塞父进程,可以继续响应其他命令;
       * 子进程创建RDB文件,根据父进程内存生成临时快照文件,完成后对原有文件进行原子替换.执行lastsave命令可以获取最后一次生成RDB的时间,对应info统计的rdb_last_time选项;
       * 进程发送信号给父进程表示完成,父进程更新统计信息,具体见 info Persistence下rdb_*相关选项.

     * RDB文件的处理

       * 保存

         * RDB文件保存在dir配置指定的目录下,文件名通过dbfilename配置指定.可以通过执行`config set dir {newDir}`和`config set dbfilename {newFileName}`运行期动态执行,当下次运行是RDB文件会保存到新的目录中
         * 当遇到坏盘或磁盘写满等情况,可以通过`config set dir {newDir}`在线修改文件路径到可用磁盘路径,之后执行bgsave进行磁盘切换,同样试用于AOF持久化文件.

       * 压缩

         * Redis默认采用LZF算法对生成的RDB文件做压缩处理,压缩后的文件远远小于内存大小,默认开启,可以通过参数`config set rdbcompression {yes|no}`动态修改.
         * 虽然压缩RDB会消耗CPU,但可大幅降低文件的体积,方便保存到硬盘或者通过网络发送给从节点,因此线上建议开启.

       * 校验

         * 如果Redis加载损坏的RDB文件时拒绝启动,并打印如下日志:

           ```
           # Short read or OOM loading DB. Unrecoverable error,aborting now
           ```

         * 这时可以使用Redis提供的redis-check-dump工具检测RDB文件并获取对应的错误报告.

     * RDB的优缺点
     
       * 优点
         * RDB是一个紧凑压缩的二进制文件,代表Redis在某个时间点上的数据快照,非常使用与备份,全量复制等场景,比如每6个小时bgsave备份,并把EDB文件拷贝到远程机器或者文件系统中hdfs,用于灾难恢复.
         * Redis加载RDB恢复数据远远快于AOF的方式
       * 缺点
         * RDB方式数据没办法做到实时持久化/秒级持久化,因为bgsave每次运行都要执行fork操作创建子进程,属于重量级操作,频繁执行成本过高.
         * RDB文件使用特定二进制格式保存,Redis版本演进过程中有多个格式的RDB版本,存在老版本Redis服务无法兼容新版RDB格式的问题
     
   * **AOF(append only file)**

     > AOF持久化:以独立日志的方式记录每次命令,重启时再重新执行AOF文件中命令达到恢复数据的目的.
     >
     > AOF的主要作用是解决了数据持久化的实时性,目前已经是Redis持久化的主流方式.

     * 开启AOF功能需要设置配置:`appendonly yes`,默认不开启.

     * AOF文件名通过appendfilename配置设置,默认文件名是appendonly.aof.保存路径桶RDB持久化方式一致,通过dir配置指定.

     * AOF的工作流程操作:

       * 命令写入(append): 所有的写入命令会追加到aof_buf(缓冲区)中;

         * AOF命令写入的内容是文本协议格式,目的如下:
           * 文本协议具有很好的兼容性;
           * 开启AOF后,所有写入命令都包含追加操作,直接采用协议格式,避免了二次处理开销;
           * 文本协议具有可读性,方便直接修改和处理.
         * AOF把命令追加到aof_buf中,是因为Redis使用单线程响应命令,如果每次写AOF文件命令都直接追加到硬盘,那么性能完全取决于当前硬盘负载.先写入缓冲区aof_buf中,还有另一个好处,Redis可以提供多种缓冲区同步硬盘的策略,在性能和安全性方面做出平衡.

       * 文件同步(sync): AOF缓冲区根据对应的策略向硬盘做同步操作;

         * Redis提供多种AOF缓冲区同步文件策略,有参数`appendfsync`控制

           | 可配置值 | 说明                                                         |
           | -------- | ------------------------------------------------------------ |
           | always   | 命令写入aof_buf后调用系统fsync操作同步到AOF文件,fsync完成后线程返回 |
           | everysec | 命令写入aof_buf后调用系统write操作,write完成后线程返回,fsync同步操作由专门线程每秒调用一次 |
           | no       | 命令写入aof_buf后调用系统write操作,不对AOF文件做fsync同步,同步硬盘操作由操作系统负责,通常同步周期最长为30秒 |

         * 系统调用write和fsync说明:

           * write操作会触发延迟写(delayed write)机制.Linux在内核提供页缓冲区用来提高硬盘IO性能.write操作在写入系统缓冲区后直接返回.同步硬盘操作依赖于系统调用度机制,例如:缓冲区也空间写满或达到特定时间周期.同步文件之前,如果此时系统故障宕机,缓冲区内数据将丢失.
           * fsync针对单个文件操作(比如AOF文件),做强制硬盘同步,fsync将阻塞知道写入硬盘完成后返回,保证了数据持久化.

       * 文件重写(rewrite): 随着AOF文件越来越大,需要定期对AOF文件进行重写,达到压缩的目的;

         * 随着命令不断写入AOF,文件会越来越大,为了解决这个问题,Redis引入AOF重写机制来压缩文件体积,AOF文件重写是把Redis进程内的数据转化为写命令同步到新AOF文件的过程.
         * 重写后的AOF文件变小的原因:
           * 进程内已经超时的数据不再写入文件;
           * 旧的AOF文件含有无效命令,如 del key1,hdel key2...重写使用进程内数据直接生成,这样新的AOF文件只保留最终数据的写入命令;
           * 多条写明了可以合并为一个,为了防止单条命令过大造成客户端缓冲区溢出,对于list,set,hash,zset等类型操作,以64个元素为界拆分为多条.
         * AOF重写降低了文件占用空间,除此之外,另一个目的是,更小的AOF文件可以更快地被Redis加载;
         * AOF重写重写可以手动触发和自动触发:
           * **手动触发**: 直接调用`bgrewriteaof`命令

           * **自动触发**: 根据`auto-aof-rewrite-min-size`和`auto-aof-rewrite-percentage`参数确定自动触发时机;
             
             * `auto-aof-rewrite-min-size`: 表示运行AOF重写时文件最小体积,默认为64MB;
             * `auto-aof-rewrite-percentage`: 代表**当前AOF文件空间(aof_curent_size)**和**上次重写后AOF文件空间(aof_base_size)**的比值
             * 自动触发时机=**aof_current_size>auto-aof-rewrite-min-size&&(aof_current_size-aof_base_size)/aof_base_size>=auto-auto-aof-rewrite-percentage**
               * 其中aof_current_size和aof_base_size可以在`info Persistence`统计信息中查看
             
             ```mermaid
             graph TD
             bgrewriteaof -->|1|父进程
             父进程 -->|2| fork
             fork -->|3.1| aof_buff
             aof_buff --> 旧AOF文件
             fork -->|3.2| aof_rewrite_buf -->|5.2| 新AOF文件
             fork --> 子进程
             子进程 -->|5.1 信号通知父进程| 父进程
             子进程 -->|4| 新AOF文件
             新AOF文件 -->|5.3| 旧AOF文件
             ```
             
           * 1- 执行AOF重写请求

             * 若当前进程正在执行AOF重写,请求不执行并返回如下响应:

               > ERR Backgroud append only file rewrite already in progress

             * 若当前进程正在执行`bgsave`操作们,重写命令延迟到bgsave完成之后再执行,返回如下响应:

               > Backgroud append only file rewriting sceduled

           * 2-父进程执行fork创建子进程,开销等同于`bgsave`过程

           * 3.1-主进程fork操作完成后,继续响应其他命令.所有修改命令依然写入AOF缓冲区并更具appendfsync策略同步到硬盘,保证原有AOF机制正确性.

           * 3.2-由于fork操作运用**写时复制技术**,子进程只能共享fork操作时的内存数据.由于父进程依然响应命令,Redis使用"AOF重写缓冲区"保存这部分新数据,防止新AOF文件生成期间丢失这部分数据.

           * 4-子进程根据内存快照,按照命令合并规则写入新的AOF文件.每次批量写入硬盘数据量由配置`aof-rewrite-incremental-fsync`控制,默认为32MB,单次刷盘数据过多造成硬盘阻塞.

           * 5.1-新AOF文件写入万抽,子进程发送信号给父进程,父进程更新统计信息,具体见`info persistence`下aof_*相关统计.

           * 5.2-父进程吧AOF重写缓冲区的数据写入到新的AOF文件.

           * 5.3-使用新AOF文件替换老文件,完成AOF重写;

       * 重启加载(load): 当Redis服务器重启时,可以加载AOF文件进行数据恢复;

         * AOF和RDB文件都可以用于服务器重启时的数据恢复.
         
         * Redis持久化文件加载流程:
         
           ``` mermaid
           graph TD
           redis启动-->开启AOF{开启AOF}
           开启AOF{开启AOF}-->|no|存在RDB?{存在RDB?}
           存在RDB?{存在RDB?}-->|no|启动成功
           存在RDB?{存在RDB?}-->|yes|加载RDB
           加载RDB-->成功?{成功?}
           成功?{成功?}-->|yes|启动成功
           成功?{成功?}-->|no|启动失败
           开启AOF{开启AOF}-->|yes|存在AOF?{存在AOF?}
           存在AOF?{存在AOF?}-->|no|存在RDB?{存在RDB?}
           存在AOF?{存在AOF?}-->|yes|加载AOF
           加载AOF-->成功?{成功?}
           ```
         
           
         
         * AOF
         
         

​    

​    

​    

​    

