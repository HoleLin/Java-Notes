## ElasticSearch

#### 目录

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
    * <a href="#match查询">match查询</a>
    * <a href="#match_phrase查询">match_phrase查询</a>
    * <a href="#match_phrase_prefix查询">match_phrase_prefix查询</a>
    * <a href="#multi_match">multi_match来匹配多个字段</a>
    * <a href="#query_string查询">query_string查询</a>
    * <a href="#term查询和term过滤器">term查询和term过滤器</a>
    * <a href="#terms查询<">terms查询</a>
    * <a href="#match查询和term过滤器">match查询和term过滤器</a>
    * <a href="#词组查询行为">词组查询行为</a>
    * <a href="#phrase_prefix查询">phrase_prefix查询</a>
  * <a href="#组合查询或复合查询">组合查询或复合查询</a>
    * <a href="#bool查询">bool查询</a>
    * <a href="#bool过滤器">bool过滤器</a>
    * <a href="#range查询和过滤器">range查询和过滤器</a>
    * <a href="#prefix查询">prefix查询</a>
    * <a href="#wildcard查询">wildcard查询</a>
    * <a href="#exists过滤器">exists过滤器</a>

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
  curl 'localhost:9200/get-together/_search' -d 
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

  * <a id="match查询">match查询</a>

    ```shell
    // 1. match查询,子弹值默认不区分大小写
    curl 'localhost:9200/_search' -d 
    '{
    	"query": {
    		"bool": {
    			"must": {
    				"match": {
    					"firstname":"xxx"
    				}
    			},
    			"filter": {
    				"term":{
    					"xxx": ""
    				}
    			}
    		}
    	}
    }'
    // 2. fistname也可以替换为_all表示匹配所有字段
    curl 'localhost:9200/_search' -d 
    '{
    	"query": {
    		"bool": {
    			"must": {
    				"match": {
    					"_all":"xxx"
    				}
    			},
    			"filter": {
    				"term":{
    					"xxx": ""
    				}
    			}
    		}
    	}
    }'
    // 3. match查询也是一种boolean查询,也就是说match查询接受到text类型的数据后,会对数据进行分析,然后根据提供的text类型数据构造一个boolean查询
    // 即所谓的"分词".通过operator标记(默认值为or)来控制boolean语句
    // 分词: 针对中文,每个文字被拆分为一个待匹配值,针对英文,空格相隔的每个单词被拆分为一个待匹配值
    // 可在建立索引时,设置mapping,为索引的字段指定"index":"not_analyzed"来禁用分词
    POST /_serch?pretty
    {
    	// 会匹配包含"小"和"林"的文档记录
    	"query":{
            "match":{
            	"firstname":"小林"
            }
    	}
    }
    POST /_serch?pretty
    {
    	// 会匹配"小林"
    	"query":{
            "match":{
            	"firstname":"小林",
            	"operator":"and"
            }
    	}
    }
    ```

  * <a id="match_phrase查询">match_phrase查询</a>

    ```shell
    // 与match类似,不同点是没有"分词"功能 等价于match..."operator":"and"
    POST /get-together/_search?pretty
    {
    	"query":{
    		"match_phrase":{
    			"firstname":{
    				"query":"小林"
    			}
    		}
    	}
    	"_source":["firstname"]
    }
    // 等价于
    POST /get-together/_search?pretty
    {
    	"query":{
    		"match":{
    			"firstname":{
    				"query":"小林",
    				"type":"phrase"
    			}
    		}
    	}
    	"_source":["firstname"]
    }
    ```

  * <a id="match_phrase_prefix查询">match_phrase_prefix查询</a>

    ```shell
    // 与match_phrase类似,不同点在于输入文本中,最后一项term允许前缀匹配
    POST /get-together/_search?pretty
    {
    	"query":{
    		"match_phrase_prefix":{
    			"message":"clever brown fo",
    			// 为了防止前缀匹配到最后项的数量过多,造成因新词组过多导致的查询时间过长,可以添加max_expansions(默认为50)来控制匹配项的数量
    			"max_expansions":"10"
    		}
    	}
    	"_source":["firstname"]
    }
    // 查询会先创建词组clever brown(即目标记录必须包含 clever且后面跟brown,clever和brown之间至少一个空格或tab),然后查询fo开头的项,比如fox,fog....然后添加到词组,组成新的词组clever brown fox,clever brown fog,...然后以match_phrese的方式去查找这些匹配这些新词组的文档记录
    ```
    
  * <a id="multi_match">multi_match来匹配多个字段</a>

      ```shell
      // multi_match与match类似,不同的是针对一个待查询的内容,可在多个指定名称字段中进行匹配
      // 同match,multi_match要查询的内容不分大小写
      curl 'localhost:9200/get-together/_search' -d 
      '{
          "query": {
              "multi_match": {
              	// query: 待查询的内容
                  "query": "elasticsearch hadoop",
                  // fields: 指定待匹配的字段,支持通配符
                  "fields": [
                      "name",
                  "description"
                  ]
          }
        }
  }'
    // 在get-together索引中,在"na"和"desc*"开头的字段中查询包含"elasticsearch hadoop"的文档记录
    curl 'localhost:9200/get-together/_search' -d 
    '{
        "query": {
            "multi_match": {
            	// query: 待查询的内容
                "query": "elasticsearch hadoop",
                // fields: 指定待匹配的字段,支持通配符
                "fields": [
                    "na*",
                    "desc*"
                ]
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
    			"default_field": "description",
    			// 带解析的查询字符串
    			"query": "nosql",
    			// 多字段,支持通配符
    			"fields":["name","first*"],
    			// 指定默认的操作符
    			"default_operator":"OR"
    		}
    	}
    }'
    // Query String 语法
    // 1. Field Name
    // 1) field_name:content 字段field_name包含内容content
    curl -XPOST 'http://localhost:9200/get-together/_search?pretty' -d 
    '{
    	"query": {
    		"query_string": {
    			"query": "addr:suzhou"
    		}
    	}
    }'
    // 2) field_name:(content1 OR content2)/field_name:(content1 content2)  字段field_name包含内容content1或content2
    curl -XPOST 'http://localhost:9200/get-together/_search?pretty' -d 
    '{
    	"query": {
    		"query_string": {
    			"query": "addr:(suzhou OR shanghai)"
    		}
    	}
    }'
    // 3) field_name:"content1 content2" 字段field_name包含内容content1 content2
    curl -XPOST 'http://localhost:9200/get-together/_search?pretty' -d 
    '{
    	"query": {
    		"query_string": {
    			"query": "addr:\"suzhou wuzhong\""
    		}
    	}
    }'
    // 4) field_name:"content1 content2" 字段field_name包含内容content1 content2
    curl -XPOST 'http://localhost:9200/get-together/_search?pretty' -d 
    '{
    	"query": {
    		"query_string": {
    			"query": "addr:\"suzhou wuzhong\""
    		}
    	}
    }'
    // 5) _exists_:field_name 存在字段field_name且field_name不为空,不为null
    curl -XPOST 'http://localhost:9200/get-together/_search?pretty' -d 
    '{
    	"query": {
    		"query_string": {
    			"query": "_exists_:addr"
    		}
    	}
    }'
    // 5) Range 支持date,numeric,字符串字段.闭区间[min TO max],开区间{min TO max}
    curl -XPOST 'http://localhost:9200/get-together/_search?pretty' -d 
    '{
    	"query": {
    		"query_string": {
    			"query": "birthday:[2020-12-01 TO 2020-12-31]"
    			// "query": "birthday:[* TO 2020-12-31}"
    			// "query": "age:[10 TO 30]"
    			// "query": "age:[15 TO *]" <==> "query": "age:>=15"
    			// "query": "age:[15 TO 25}" <==> "query": "age:(>=15 AND <25)"
    		}
    	}
    }'
    // 6) bool操作符
    // +content1: 包含content
    // -content: 不包含content
    curl -XPOST 'http://localhost:9200/get-together/_search?pretty' -d 
    '{
    	"query": {
    		"query_string": {
    			"query": "addr:suzhou +wuzhong -huqiu"
    		}
    	}
    }'
    ```

  * <a id="term查询和term过滤器">term查询和term过滤器</a>

    ```shell
    // term 用于精确查询
    // 说明: 字段值类型若为text,被视为full text,会传递给被解析器进行解析,当置为中文,空格,tab键分割的单词是,会被拆分成多个待查询的term.如"Quick Brown Fox"会被解析为[Quick,Brown,Fox];若为keyword则视为精确值,精确值(如数字,日期,keyword)拥有准确的整定值
    // 词条查询
    curl 'localhost:9200/get-together/_search' -d 
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
    curl 'localhost:9200/get-together/_search' -d 
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
    curl 'localhost:9200/get-together/_search' -d 
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
    curl 'localhost:9200/get-together/_search' -d 
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

  * 

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
    
  * ##### <a id="range查询和过滤器">range查询和过滤器</a>
  
    ```shell
    // range查询
    // gte 大于等于(Greater-than or equal to)
    // gt  大于 (Greater-than)
    // lte 小于等于(Less-than or equal to)
    // lt  小于(Less-than)
    // 可作用与支持date,numeric,字符串字段
    curl 'localhost:9200/get-together/_search' -d 
    '{
        "query": {
            "range": {
                "updateTime": {
                    "gt": "1591321611000",
                    "lt": "1607132852000"
                }
            }
        }
    }'
    // 查找birthday大于等于2020-08-17 11:20:00 小于等于2020-08-18的文档记录
    curl 'localhost:9200/get-together/_search' -d 
    '{
        "query": {
            "range": {
                "updateTime": {
                    "gte": "2020-08-17T11:20:00||-1d",
                    "lte": "2020-08-18"
                }
            }
        }
    }'
    // 比较操作符合"向下舍"的关系
    // gt: 2020-11-18||/M  ES解析:  gt:  2020-11-30T23:59:59.999
    // gte: 2020-11-18||/M ES解析:  gte: 2020-11-01
    // lt: 2020-11-18||/M  ES解析:  lt:  2020-11-01
    // lte: 2020-11-18||/M ES解析:  lte: 2020-11-30T23:59:59.999
    curl 'localhost:9200/get-together/_search' -d 
    '{
        "query": {
            "range": {
                "updateTime": {
                    "gte": "now-8h/d",
                    "lte": "now/d"
                }
            }
        }
    }'
    // 查询参数date格式化
    curl 'localhost:9200/get-together/_search' -d 
    '{
        "query": {
            "range": {
                "updateTime": {
                    "gte": "17/08/2020",
                    "lte": "18/08/2020",
                    "format":"dd/MM/yyyy||yyyy"
                }
            }
        }
    }'
    // Date Math
    // 可在基准日志(anchor date 比如now或者以||结束的date字符串)之后添加1个或多个数学表达式:
    // +1h 加上1小时
    // -1d 减去1天
    // /d  向下舍至最近的一天
    // 例子:
    // now+1h    当前时间加上一个小时
    // now+1h+1m 当前时间加上1个小时1分钟
    // now+1h/d  当前时间加上1个小时,向下舍至最近的一天
    // 2020-01-01||+1M/d 2020-01-01加上一个月,向下舍至最近的一天
    // 支持的时间单位有:
    // 单位		含义
    //  y		  年
    //  M		  月
    //  w		  周
    //  d		  日
    //  h		  小时
    //  H		  小时
    //  m		  分钟
    //  s		  秒
    // range过滤器
    curl 'localhost:9200/get-together/_search' -d 
    '{
        "query": {
            "bool": {
                "must": {
                    "match_all": {}
                },
                "filter": {
                    "range": {
                        "updateTime": {
                            "gt": "1591321611000",
                            "lt": "1607132852000"
                        }
                    }
                }
            }
        }
    }'
    ```
  
  * ##### <a id="prefix查询">prefix查询</a>
  
    ```shell
    // prefix查询
    // 默认情况下,prefix的匹配对象是针对单个单词的
    curl 'localhost:9200/get-together/_search' -d 
    '{
    	"query": {
    		"prefix": {
    			"title": "liber"
    		}
    	}
    }'
    // prefix过滤器
    curl 'localhost:9200/get-together/_search' -d 
    '{
        "query": {
            "bool": {
                "must": {
                    "match_all": {}
                },
                "filter": {
                    "prefix": {
                        "title": "liber"
                    }
                }
            }
        }
    }'
    ```
  
  * ##### <a id="wildcard查询">wildcard查询</a>
  
    > wildcard查询可以使用类似shell通配符globbing的工作方式.列如运行 `ls *foo?ar`会匹配"myfoobar","foocar"和"thefoobar"这样的单词.
    >
    > 可以混合使用多个*和?字符来匹配更为复杂的通配模板,但当字符串被分析后,默认情况下空格会被去除,如果空格没被索引,那么?是无法匹配上空格的.
  
    ```shell
    curl 'localhost:9200/get-together/_search' -d 
    '{
    	"query": {
    		"wildcard": {
    			"title": {
    				"wildcard": "ba*n"
    			}
    		}
    	}
    }'
    ```
  
  * <a id="exists过滤器">字段存在性过滤器--exists过滤器</a>
  
    ```shell
    // exists过滤器允许过滤文档,只查找那些特定字段有值的文档,无论其值是多少
    curl 'localhost:9200/get-together/_search' -d 
    '{
        "query": {
            "bool": {
                "must": {
                    "match_all": {}
                },
                "filter": {
                    "exists": {
                        "field": "xxx"
                    }
                }
            }
        },
        "_source":["xxx","xxx","xxxx"]
    }'
    ```
  
  * missing过滤器 **废弃**
  
    ```shell
    // missing过滤器可以搜索字段里面没有值,或者是映射时指定的默认的文档(也叫作null值,即映射里面的null_value)
    curl 'localhost:9200/get-together/_search' -d 
    '{
    	"query": {
    		"bool": {
    			"must": {
    				"match_all": {}
    			},
    			"filter": {
    				"missing": {
    					"field": "reviews",
    					"existence": "true",
    					"null_value": "true"
    				}
    			}
    		}
    	}
    }'
    // Missing Query Deprecated in 2.2.0. Use exists query inside a must_not clause instead
    {
        "query": {
            "bool": {
                "must": {
                    "match_all": {}
                },
                "must_not": {
                    "exists": {
                        "field": "reviews"
                    }
                }
            }
        }
    }
    ```
  
  * 将任何查询转变为过滤器 **废弃**
  
    ```shell
    curl 'localhost:9200/get-together/_search' -d 
    '{
    	"query": {
    		"bool": {
    			"must": {
    				"match_all": {}
    			},
    			"filter": {
    				"query": {
    					"query_string": {
    						"query": "name:\"dever clojure\""
    					}
    				}
    			}
    		}
    	}
    }'
    ```
  
  * <a id="regexp">regexp</a>
  
    ```shell
    curl 'localhost:9200/get-together/_search' -d 
    '{
    	"query": {
    		"regexp":{
    			"name":"苏州."
    		}
    	}
    }'
    ```
  
    

