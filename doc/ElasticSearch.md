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

* #### 使用URL限制搜索范围

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
  curl  'localhost:9200/+get-toge*,-get-together/_search' -d '...'
  ```

* #### 搜索请求的基本模块

  > **query** : 搜索请求最重要的组成部分.
  >
  > **size** : 代表了返回文档的数量
  >
  > **from** : 和size一起使用,fom用于分页操作. from的参数是从0开始的
  >
  > **_source** : 指定`_source`字段如何返回.默认是返回完整的`_source`字段.通过配置`_source`,将过滤返回的字段
  >
  > **sort** : 默认的排序是基于文档的得分

* #### 基于URL的搜索请求

  ```shell
  curl 'localhost:9200/get-together/_search?from=10&size=10&sort=date:asc&_source=title,date&q=elasticsearch' 
  ```

* #### 基于请求主体的搜索请求

  ```shell
  curl 'localhost:9200/get-together/_search' -d 
  '{
  	"query": {
  		"match_all":{}
  	},
  	// 返回从第10页开始的结果
  	"from": 10,
  	// 总共返回最多10个结果
  	"size": 10
  	// 搜索回复中返回名字和日期字段
  	"_sourcce": ["name","date"]
  }'
  ```

  * ##### `_source` 返回字段中通配符

    ```shell
    curl 'localhost:9200/get-together/_search' -d
    '{
    	"query": {
    		"match_all":{}
    	},
    	"_source": {
    		// 在搜索回复中返回以location开头的字段和日期字段
    		"include": ["location.*","deate"],
    		// 不要返回location.getolocation
    		"exclude": ["location.geolocation"]
    	}
    }'
    ```

  * ##### 结果的排序

    ```shell
    curl 'localhost:9200/get-together/_search' -d 
    '{
    	"query": {
    		"match_all": {}
    	},
    	"sort": [
    		// 首先按照创建日期来排序 从最老到最新的
    		{"created_on": "asc"},
    		// 然后按照分组的名称来排序 按倒排的字母顺序
    		{"name": "desc"},
    		// 最终按照相关性得分(_score)来排序
    		"_score"
    	]
    }'
    ```

  * ##### 返回的结果

    ```json
    {
        //查询所用的毫秒数
        "took": 27,
        // 表明是否分片超时,也就是说是否只返回了部分结果
        "timed_out": false,
        "_shards": {
            // 成功响应该请求和未能成功响应该请求的分片数量
            "total": 15,
            "successful": 15,
            "skipped": 0,
            "failed": 0
        },
        // 返回中的包含了命中(hits)的键,其值是命中文档的数组
        "hits": {
            "total": {
                // 该请求所有匹配结果的数量
                "value": 3,
                "relation": "eq"
            },
            // 这个搜索结果中的最大得分
            "max_score": 2.0000367,
            // 命中(hits)关键词元素中的命中文档数组
            "hits": [
                {
                    // 结果文档的索引
                    "_index": "xxxxx",
                    // 结果文档的Elasticsearch类型
                    "_type": "_doc",
                    // 结果文档的ID
                    "_id": "vZATIXYB5sSlphETLNfC",
                    // 结果的相关性得分
                    "_score": 2.0000367,
                    "_source": {}
                }
            ]
        }
    }
    ```



* #### 查询和过滤器DSL

  * ##### match查询和term过滤器

    ```shell
    curl 'localhost:9200/get-together/event/_search' -d 
    '{
    	"query": {
    		"match": {
    			"title": "hadoop"
    		}
    	}
    }'
    ```

    ![match与term过滤器的区别.png](https://github.com/HoleLin/springboot-demos/blob/master/doc/images/match%E4%B8%8Eterm%E8%BF%87%E6%BB%A4%E5%99%A8%E7%9A%84%E5%8C%BA%E5%88%AB.png?raw=true)

    
    * 使用过滤器的查询

      ```shell
      curl 'localhost:9200/get-together/_search' -d 
      '{
          "query": {
              "bool": {
              	// 查询类型 这里指定了一个附上过滤器的查询
                  "must": {
                      "match": {
                          "title": "hadoop"
                      }
                  },
                  // 额外的过滤器将查询结果限制为用户名为andy的数据
                  "filter": {
                      "term": {
                          "username": "andy"
                      }
                  }
              }
          }
      }'
      ```

  * ##### 常用的基础查询和过滤器

    * match_all查询

      ```shell
      curl 'localhost:9200/_search' -d 
      '{
      	"query": {
      		"bool": {
      			"must": {
      				"match_all": {}
      			},
      			"filter": {
      				"term":{
      					"xxx": ""
      				}
      			}
      		}
      	}
      }'
      ```

    * query_string查询

      ```shell
      curl -XPOST 'http://localhost:9200/get-together/_search?pretty' -d 
      '{
      	"query": {
      		"query_string": {
      			// 由于查询中没有指定字段,所以使用了默认字段"description"
      			"default_field": "description"
      			"query": "nosql"
      		}
      	}
      }'
      ```

    * term查询和term过滤器

      ```shell
      // 词条查询
      curl 'localhost:9200/get-together/group/_search' -d 
      '{
      	"query": {
      		"term": {
      			"tags": "elasticsearch"
      		}
      	},
      	"_source": ["name","tags"]
      }'
      // 词条过滤器
      curl 'localhost:9200/get-together/_search' -d 
      '{
          "query": {
              "bool": {
                  "must": {
                      "match_all": {}
                  },
                  "filter": {
                      "term": {
                          "xxx": "xxx"
                      }
                  }
              }
          },
          "_source": [
              "name",
              "tags"
          ]
      }'
      ```

    * terms查询

      ```shell
      // 使用多词条搜索多个词条
      curl 'localhost:9200/get-together/group/_search' -d 
      '{
      	"query": {
      		"terms": {
      			"tags": ["jvm","hadoop"],
      		}
      	},
      	"_source": ["name","tags"]
      }'
      // 强制规定每一篇文档中匹配词条的最小数量
      {
        "query": {
          "bool": {
            "minimum_should_match": 2,
            "should": [
              {
                "term": {
                  "xxx": "xxx"
                 }
              },
              {
                "term": {
                  "xxx": "xxx"
                }
              }
            ]
          }
        }
      }
      ```

    * match查询和term过滤器

      ```shell
      // 默认情况下,match查询会使用布尔行为和OR操作符
      curl 'localhost:9200/get-together/_search' -d 
      '{
      	"query": {
      		"match": {
      			// 对于name的值,使用映射,而不是字符串
      			"name": {
      				// 在query的键里面指定要搜索的字符串
      				"query": "Elasticsearch Denver",
      				// 使用and操作符,而不是默认的or操作符
      				"operator": "and"
      			}
      		}
      	}
      }'
      ```

    * 词组查询行为

      ```shell
      curl 'localhost:9200/get-together/group/_search' -d 
      '{
      	"query": {
      		"match": {
      			"name": {
      				// 使用match_phrase查询,而不是普通的match查询
      				"type": "phrase",
      				"query": "enterprise london",
      				// 将slop设置为1,表明允许词条之间有间隔
      				"slop": 1
      			}
      		}
      	},
      	"_source": ["name","description"]
      }'
      ```

    * phrase_prefix查询 **存在问题**

      ```shell
      // 和match_phrase查询类似,phrase_prefix查询可以更进一步搜索词组,不过它是和词组中最后一个词条进行前缀匹配,对于提供搜索框里面自动完成功能而言,这个行为是非常有用的.
      // 当使用这种行为的搜索时,最好通过max_expansions来设置最大的前缀扩展数量
      curl 'localhost:9200/get-together/group/_search' -d 
      '{
      	"query": {
      		"match": {
      			"name": {
      				"type": "phrase_prefix",
      				"query": "Elasticsearch den",
      				"max_expansions": 1
      			}
      		}
      	},
      	"_source": ["name"]
      }'
      
      ```

    * multi_match来匹配多个字段

      ```shell
      curl 'localhost:9200/get-together/_search' -d 
      '{
      	"query": {
      		"multi_match": {
      			"query": "elasticsearch hadoop",
      			"fields": ["name","description"]
      		}
      	}
      }'
      ```

      