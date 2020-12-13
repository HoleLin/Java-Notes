##  Elasticsearch 聚集

### 定义

> 聚集分为链各个主要类型: 度量型和桶型.
>
> **度量型(metrics)聚集**是指一组文档的统计分析,可以打得到最小值,最大值,标准差等度量值.
>
> **桶(bucket)聚集**将匹配的文档切分为一个或多个容器(桶),然后告诉你每个桶里的文档数量.

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

  

### 度量聚集

* 

### 单个和多桶聚集

### 嵌套聚集