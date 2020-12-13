##  ElasticSeach

### 目录

* <a href="#分析">分析</a>
* <a href="#为文档使用分析器">为文档使用分析器</a>

### <a id="分析">分析</a>

* 什么是分析

  > 分析(anslysis)是在文档被发送并加入倒排索引之前,Elasticsearch在其主体上进行的操作.
  * **字符过滤**: 使用字符过滤转变为字符;
  * **文本切分为分词**: 将文本切分为单个或多个分词;
  * **分词过滤**: 使用分词过滤器转变为每个分词;
  * **分词索引**: 将这些分词存储到索引中;

* 字符过滤

  > Elasticsearch首先运行字符过滤器,这些过滤器将特定的字符序列转变为其他的字符序列,可以用于将HTML从文本中剥离,或者是将任意数量的字符转化为其他字符(例: 将"I love u 2" 转变为"I love you too").

* 切分分词

  > 在应用了字符过滤器后,文本需要被分割为可以操作的片段.Lucene自己不会对大块字符串数据进行操作,相反,它使用被称为**分词(token)**的来处理数据.分词是从文本片段生成的,可能会产生任意数量(甚至是0)的分词.

* 分词过滤器

  > 一旦文本块被转换为分词后,Elasticsearch将会对每个分词运用**分词过滤器(token filter)**.这些分词过滤器可以将一个分词作为输入,然后根据需要进行修改,添加或者是删除.

* 分词索引

  > 当分词经历了零个或多个分词过滤器,它们将被发送到Lucene进行文档索引.这些分词组成了倒排索引,所有这些不同的部分,组成了一个**分析器(analyzer)**,它可以定义为零个或多个字符过滤器,一个分词器,零个或者多个分词过滤器.

### <a id="为文档使用分析器">为文档使用分析器</a>

* 为指定字段使用分析器

  * 当创建索引的时候,为特定的索引进行设置.
  * 在Elasticsearch的配置文件中,设置全局的分析器.

* 在索引创建时增加分析器

  ```shell
  curl XPOST 'localhost:9200/myindex' -d 
  '{
  	"settings": {
  		// 为索引指定定制化的设置,这里指定了2份主分片
  		"number_of_shards": 2,
  		"number_of_replicas": 1,
  		// 其他索引级别的设置
  		"index": {
  			// 索引的分析设置
  			"analysis": {
  				// 在分析器对象中设置定制分析器
  				"analyzer": {
  					"myCustomAnalyzer": {
  						// 定制化的类型
  						"type": "custom",
  						// 设置定制的字符过滤器,这会在其他分析步骤之前运行
  						"char_filter": ["myCustomCharFilter"],
  						// 使用myCustomTokenizer对文本进行分词
  						"tokenizer": "myCustomToken",
  						// 指定文本需要经过的两个过滤器
  						"filter": ["myCustomilter1","myCustomFilter2"]
  					},
  					"char_filter": {
  						"myCustomCharFilter": {
  							"type": "mapping",
  							"mappings": ["ph=>f","u=>you"]
  						}
  					},
  					"tokenizer": {
  						"myCustomTokenizer": {
  							"type": "letter"
  						}
  					},
  					"filter": {
  						"myCustomFilter1": {
  							"type": "lowercase"
  						},
  						"myCustomFilter2": {
  							"type": "kstem"
  						}
  					}
  				}
  			}
  		}
  	},
  	"mapping": {
  		// 索引映射
  		....
  	}
  }'
  ```

* 在Elasticsearch的配置中添加分析器

  ```yaml
  // 在elasticsearch.yml中设置
  index: 
  	analysis:
  		analyzer:
  			myCustomAnalyzer: 
  				type: custom
  				char_filter: myCustomCharFilter
  				tokenizer: myCustomTokenizer
  				filter: [myCustomFilter1,myCustomFilter2]
  		char_filter: 
  			myCustomCharFilter:
  				type: mapping
  				mappings: ["ph=>f","u=>you"]
  		tokenizer: 
          	myCustomTokenizer:
          		type: letter
          filter: 
          	myCustomFilter1:
          		type: lowercase
          	myCustomFilter2:
          		type: kstem
  ```

* 在映射中指定某个字段的分析器

  ```
  {
  	"mappings": {
  		"document": {
  			"properties": {
  				// 为name字段指定myCustomAnalyzer的分析器
  				"name":{
  					"type": "text",
  					"analyzer": "myCustomAnalyzer"
  				},
  				// 指定不要分析title字段
  				"title": {
  					"type": "text",
  					"index": "not_analyzed"
  				}
  			}
  		}
  	}
  }
  ```

* 使用多字段类型来存储分析方式不同的文本

  ```
  curl -XPOST 'localhost:9200/get-together' -d 
  '{
  	"mappings": {
  		"document": {
  			"properties": {
  				"name":{
  					"type": "text",
  					// 初始的分析,使用标准的分析器,该分析器是默认值,此处可以省略配置
  					"analyzer": "standard"
  					"fields": {
  						"raw": {
  							// 字段的原始版本,没有进过分析
  							"index": "not_analyzed",
  							"type": "text"
  						}
  					}
  				}
  			}
  		}
  	}
  }'
  ```

  

