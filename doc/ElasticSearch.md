## ElasticSearch

* <a id ="目录">目录</a>
* <a href="#使用URL限制搜索范围">安装</a>
* <a href="#使用URL限制搜索范围">搜索基础</a>
  * <a href="#使用URL限制搜索范围">使用URL限制搜索范围</a>
  * <a href="#搜索请求的基本模块">搜索请求的基本模块</a>
  * <a href="#基于URL的搜索请求">基于URL的搜索请求</a>
  * <a href="#基于请求主体的搜索请求">基于请求主体的搜索请求</a>
* <a href="#各种查询">各种查询</a>
  * <a href="#match查询和term过滤器">match查询和term过滤器</a>
  * <a href="#常用的基础查询和过滤器">常用的基础查询和过滤器</a>
    * <a href="#match_all查询">match_all查询</a>
    * <a href="#query_string查询">query_string查询</a>
    * <a href="#term查询和term过滤器">term查询和term过滤器</a>
    * <a href="#terms查询<">terms查询</a>
    * <a href="#match查询和term过滤器">match查询和term过滤器</a>
    * <a href="#词组查询行为">词组查询行为</a>
    * <a href="#phrase_prefix查询">phrase_prefix查询</a>
  * <a href="#组合查询或复合查询">组合查询或复合查询</a>
    * <a href="#bool查询">bool查询</a>
    * <a href="#bool过滤器">bool过滤器</a>

### 1.<a id="安装"> 安装</a>

* UNIX类的操作系统

  ```shell
  tar zxf elasticsearch-*.tar.gz
  cd elasticsearch-*
  /bin/elasticsearch
  ```

* Window下载,解压,执行elasticsearch.bat脚本

### 2. <a id="搜索">搜索</a>

* #### <a id="使用URL限制搜索范围">使用URL限制搜索范围</a>

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

* #### <a id="搜索请求的基本模块">搜索请求的基本模块</a>

  > **query** : 搜索请求最重要的组成部分.
  >
  > **size** : 代表了返回文档的数量
  >
  > **from** : 和size一起使用,fom用于分页操作. from的参数是从0开始的
  >
  > **_source** : 指定`_source`字段如何返回.默认是返回完整的`_source`字段.通过配置`_source`,将过滤返回的字段
  >
  > **sort** : 默认的排序是基于文档的得分

* #### <a id="基于URL的搜索请求">基于URL的搜索请求</a>

  ```shell
  curl 'localhost:9200/get-together/_search?from=10&size=10&sort=date:asc&_source=title,date&q=elasticsearch' 
  ```

* #### <a id="基于请求主体的搜索请求">基于请求主体的搜索请求</a>

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

### 3. <a id="查询和过滤器DSL">查询和过滤器DSL</a>

* ##### <a id="match查询和term过滤器">match查询和term过滤器</a>

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

* ##### <a id="常用的基础查询和过滤器">常用的基础查询和过滤器</a>

  * <a id="match_all查询">match_all查询</a>

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

  * <a id="query_string查询">query_string查询</a>

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

  * <a id="term查询和term过滤器">term查询和term过滤器</a>

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

  * <a id="terms查询">terms查询</a>

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

  * <a id="match查询和term过滤器">match查询和term过滤器</a>

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

  * <a id="词组查询行为">词组查询行为</a>

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

  * <a id="phrase_prefix查询 ">phrase_prefix查询 **存在问题**</a>

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

  * <a id="multi_match来匹配多个字段">multi_match来匹配多个字段</a>

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

* ##### <a id="组合查询或复合查询">组合查询或复合查询</a>

  * ###### <a id=">bool查询">bool查询</a>

    > bool查询允许你在单独查询中组合任意数量的查询,执行查询子句表明哪些部分是**必须(must)匹配**,**应该(should)匹配**或者是**不能(must_not)匹配**上Elasticsearch索引里的数据
    >
    > *  如果执行bool查询的某部分是must匹配,只有匹配上这些查询的结果才会被返回;
    > * 如果指定了bool查询的某部分是should匹配,只有匹配上指定数量子句的文档才会被返回;
    > * 如果没有执行must匹配的子句,文档至少要匹配一个should子句才能返回;
    > * must_not子句会使得匹配其的文档被移除结果集合;
    
    ```shell
  curl 'localhost:9200/get-together/_search' -d 
    '{
    	"query": {
    		"bool": {
    			// 结果文档必须(must)匹配的查询
    			"must":[
  				{
    					"term": {
  						"attendees": "david"
    					}
    				}
    			],
  			// 文档应该(should)匹配的第二个查询
    			"should": [
    				{
    					"term": {
    						"attendees": "clint"
    					}
    				},
    				{
                        "term": {
    						"attendees": "andy"
                        }
    				}
    			],
    			// 结果文档不能(must_not)匹配的查询
    			"must_not": [
    				{
    					"range": {
    						"date": {
    							"lt": "2020-06-30"
    						}
    					}
    				}
    			],
    			// 最小的should子句匹配数,满足这个数量文档才能作为结果返回
    			"minimum_should_match": 1
    		}
    	}
    }'
    ```

    | bool查询子句 | 等价的二元操作                                               | 含义                                                         |
    | ------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
    | must         | 为了组合多个子句,使用二元操作and<br />(query1 AND query2 AND query3) | 在must子句中的任何搜索必须匹配上文档,小写的and是功能,大写的AND是操作符 |
    | must_not     | 使用二元操作not组合多个子句                                  | 在must_not子句中任何搜索不能是文档的一部分.多个子句通过not二元操作符进行组合(NOT query1 AND NOT query2 AND NOT query3) |
    | should       | 使用二元操作or组合多个子句(query1 OR query2 OR query3)       | 在should子句中搜索,可以匹配也可以不匹配一篇文档,但是匹配数至少达到minimum_should_match参数所设置的数量(如果没有使用must那么默认私用1,如果使用了must,默认是0),和二元查找操作OR类型(query1 OR query2 OR query3) 类似 |

  * ###### <a id="bool过滤器">bool过滤器</a>

    ```shell
    curl 'localhost:9200/get-together/_seach' -d 
    '{
    "query": {
            "bool": {
                "must": [
                    {
                        "term": {
                            "attendees": "david"
                        }
                    }
                ],
                "filter": {
                    "bool": {
                        "must": [
                            {
                                "term": {
                                    "attendees": "david"
                                }
                            }
                        ],
                        "should": [
                            {
                                "term": {
                                    "attendees": "clint"
                                }
                            },
                            {
                                "term": {
                                    "attendees": "andy"
                                }
                            }
                        ],
                        "must_not": [
                            {
                                "range": {
                                    "date": {
                                        "lt": "2020-06-30"
                                    }
                                }
                            }
                        ]
                    }
                }
            }
        }
    }'
    ```
    
    