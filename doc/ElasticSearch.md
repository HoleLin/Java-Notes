## ElasticSearch

### 1. 安装

* UNIX类的操作系统

  ```shell
  tar zxf elasticsearch-*.tar.gz
  cd elasticsearch-*
  /bin/elasticsearch
  ```

* Window下载,解压,执行elasticsearch.bat脚本

### 2. 搜索

* 使用URL限制搜索范围

  ```shell
  // 搜索整个集群
  curl  'localhost:9200/_search' -d '... '
  // 搜索单个索引
  curl  'lcoalhost:9200/get-together/_search' -d '...'
  // type将被移除
  curl  'lcoalhost:9200/get-together/event/_search' -d '...'
  curl  'lcoalhost:9200/_all/event/_search' -d '...'
  curl  'lcoalhost:9200/*/event/_search' -d '...'
  // 在get-together和其他索引中事件和分组类型
  curl  'localhost:9200/get-together,other/event,group/_search' -d '...'
  // 搜索所有名字以get-toge开头的索引,但是不包括get-together
  ```

  

