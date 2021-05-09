##  Elasticsearch 聚集

### 定义

> 聚集分为链各个主要类型: 度量型和桶型.
>
> **度量型(metrics)聚集**是指一组文档的统计分析,可以打得到最小值,最大值,标准差等度量值.相当于SQL中的group by
>
> * avg 
> * cardinality
> * stats
> * extended stats
> * percentiles
> * percentiles ranks
>
> **桶(bucket)聚集**将匹配的文档切分为一个或多个容器(桶),然后告诉你每个桶里的文档数量.
>
> * filter
> * range
> * missing
> * terms
> * date range
> * global aggregation
> * histogram
> * date histogram
> * ipv4 range
> * return only aggregation results

* 聚集请求的请求结构

  ```shell
  curl 'localhost:9200/get-together/_search?pretty' -d 
  '{
  	// 键"aggs"表明,这是该请求的聚集部分
  	"aggs":{
  		// 指定聚集的名称
  		"top_tags":{
  			// 指定聚集类型为此词条
  			"term":{
  				"field":"tags.verbatim"
  			}
  		}
  	}
  }'
  ```

* 运行在查询结果上的聚集

  ```shell
  // 聚集总是在所有和查询相匹配的结果上执行的
  curl 'localhost:9200/get-together/_search?pretty' -d 
  '{
  	"query":{
  		"match":{
  			"name":"xxx"
  		}
  	},
  	// 键"aggs"表明,这是该请求的聚集部分
  	"aggs":{
  		// 指定聚集的名称
  		"top_tags":{
  			// 指定聚集类型为此词条
  			"term":{
  				"field":"tags.verbatim"
  			}
  		}
  	}
  }'
  ```

* 过滤器和聚集

  * **后过滤器(post filter)**:该过滤器是在查询结果之后运行,和聚集操作相互独立.

    ```shell
    curl 'localhost:9200/get-together/_search?pretty' -d 
    '{
    	"post_filter":{
    		"term":{
    			"localtion":"denver"
    		}
    	}
    }'
    ```

  * 后过滤器和filtered查询中的过滤器有两点不同

    * **性能**--后过滤器在查询之后运行,确保查询在所有文档上运行,而过滤器只在和查询匹配的文档上有运行.整体的请求通常比对等 filtered查询执行更慢,因为filtered查询中过滤器是先运行的,减少了聚集执行是处理的文档数量.
    * **聚集处理的文档集合**--如果一篇文档和后过滤器不匹配,它任然会被聚集操作计算在内.

### 度量聚集

> 度量聚集从不同的分组中提取统计数据,这些统计通常来自数值型,如最小或者平均值.

* 统计数据

  ```shell
  curl 'localhost:9200/get-together/_search?pretty' -d 
  '{
  	"aggs": {
  		"aggs_stats_name": {
  			// 综合统计count min max avg sum value_count
  			"stats": {
  				"script": "doc['attendees'].values.length"
  			}
  		}
  	}
  }'
  // 返回值
  "aggs_stats_name": {
              "count": 488121,
              "min": 0.0,
              "max": 2.0,
              "avg": 1.6308333384550142,
              "sum": 796044.0
  }
  ```

* 高级统计

  ```shell
  curl 'localhost:9200/get-together/_search?pretty' -d 
  '{
  	"aggs": {
  		"aggs_extended_name": {
  			// 使用extended_stats聚集获取数值字段的平方值,方差,标准差
  			"extended_stats": {
  				"script": "doc['attendees'].values.length"
  			}
  		}
  	}
  }'
  // 返回值
  "aggs_extended_name": {
      "count": 488121,
      "min": 0,
      "max": 2,
      "avg": 1.6308333384550142,
      "sum": 796044,
      "sum_of_squares": 1592088,
      "variance": 0.6020492990937016,
      "std_deviation": 0.7759183585234349,
      "std_deviation_bounds": {
          "upper": 3.182670055501884,
          "lower": 0.07899662140814434
      }
  }
  ```

* 近似统计

  * 百分位 (percentiles)
  * 基数(cardinality)是某个字段中唯一值的数量

  ```shell
  curl 'localhost:9200/get-together/_search?pretty' -d 
  '{
  	"aggs": {
  		"aggs_percentiles_name": {
  			// 百分比统计
  			"percentiles": {
  				"script": "doc['attendees'].values.length",
  				"percents": [80,90]
  			}
  		}
  	}
  }'
  // 返回值
   "aggs_percentiles_name": {
              "values": {
              	// 80%的值不超过4
                  "80.0": 1.0,
                  // 90%的值不超过5
                  "90.0": 1.0
     }
  }
  curl 'localhost:9200?pretty' -d 
  '{
  	"aggs": {
  		"aggs_percentile_ranks_name": {
  			"percentile_ranks": {
  				"script": "doc['attendees'].values.length",
  				"vaules": [4,5]
  			}
  		}
  	}
  }'
  ```

* 基数

  ```shell
  curl 'localhost:9200/get-together/_search?pretty' -d 
  '{
  	"aggs": {
  		"members_cardinality": {
  			// 去重计数
  			"cardinality": {
  				"field": "status"
  			}
  		}
  	}
  }'
  ```

### <a id="多桶型聚集">多桶型聚集</a>

* **词条聚集(term aggregation)**: 查看文档中每个词条的频率;

  ```shell
  curl 'localhost:9200/get-together/_search?pretty' -d 
  '{
      "aggs": {
          "tags": {
              "terms": {
                  "field": "status",
                  "include": ".*search.*"
                  "order":{
                      "_count":"asc"
                  }
              }
          }
      },
      "size": 0
  }'
  // 返回值
  {
      "took": 0,
      "timed_out": false,
      "_shards": {
          "total": 5,
          "successful": 5,
          "skipped": 0,
          "failed": 0
      },
      "hits": {
          "total": 488121,
          "max_score": 0.0,
          "hits": []
      },
      "aggregations": {
          "tags": {
          	// 在最坏情况下,错误的上限.所有分批的这些数值之和组成了doc_count_error_upper_bound
              "doc_count_error_upper_bound": 0,
              // 未能排名靠前的词条之总数量
              "sum_other_doc_count": 0,
              "buckets": [
                  {
                      "key": 0,
                      "doc_count": 44
                  },
                  {
                      "key": 1,
                      "doc_count": 413915
                  }
              ]
          }
      }
  }
  ```

  * **显著词条**

    > 若想在目前的搜索结果中,查看词条比通常情况有更高的词频,可采用significant_terms聚集

    ```shell
    curl 'localhost:9200/get-together/_search?pretty' -d 
    '{
        "query": {
            "bool": {
                "filter": {
                    "bool": {
                        "must": {
                            "match": {
                                "type": "xxx"
                            }
                        }
                    }
                }
            }
        },
        "aggs": {
            "tags": {
                "significant_terms": {
                    "field": "status",
                    "min_doc_count": 2,
                    "exclude": "xx"
                }
            }
        },
        "size": 0
    }'
    ```

* **范围聚集(range aggregation)**: 根据文档落入哪些数值,日期或者IP地址的范围来创建不同的桶;

  * range聚集

    ```shell
    curl 'localhost:9200/get-together/_search?pretty' -d 
    '{
        "aggs": {
            "range_name": {
                "range": {
                    "field": "stauts",
                    "ranges": [
                        {
                            "to": 4
                        },
                        {
                            "to": 3,
                            "from": 6
                        }
                    ]
                }
            }
        },
        "size": 0
    }'
    ```

  * date range聚集

    ```shell
    curl 'localhost:9200/get-together/_search?pretty' -d 
    '{
        "aggs": {
           "date_range_name": {
                "date_range": {
                    "field":"updateTime",
                    "format":"YYYY.MM",
                    "ranges": [
                        {
                            "to": "2020.01"
                        },
                        {
                            "to": "2020.01",
                            "from": "2020.12"
                        }
                    ]
                }
            }
        },
        "size": 0
    }'
    ```

    

* **直方图聚集(histogram aggregation)**: 可能是数值,或者日期型,和范围聚集类似.但是它无须定义每个范围区间,而是需要定义间距值.

  * hisogram

    ```shell
    curl 'localhost:9200/get-together/_search?pretty' -d 
    '{
        "aggs": {
             "histogram_name":{
                "histogram":{
                    "field":"status",
                    "interval":50
                }
            }
        },
        "size": 0
    }'
    ```

  * date_hisogram

    ```shell
    curl 'localhost:9200/get-together/_search?pretty' -d 
    '{
        "aggs": {
            "date_histogram_name": {
                "date_histogram": {
                    "field": "updateTime",
                    "interval": "1y"
                }
            }
        },
        "size": 0
    }'
    ```

* **嵌套聚集(nested aggregation),反嵌套聚集(reverse aggregation),子聚集(children aggregation)**: 允许用户针对文档的关系来执行聚集.

* **地理距离聚集(geo distance aggregation),地理散列格聚集(geohash grid aggregation)**： 允许用户根据地理位置来创建桶.

### 嵌套聚集

