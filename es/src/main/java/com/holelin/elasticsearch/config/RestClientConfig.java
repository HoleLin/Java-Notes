package com.holelin.elasticsearch.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * @Description: ElasticSearch配置类
 * @Author: HoleLin
 * @CreateDate: 2020/11/26 16:01
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/11/26 16:01
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Configuration
public class RestClientConfig extends AbstractElasticsearchConfiguration {
    @Override
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();

        return RestClients.create(clientConfiguration).rest();       }
}
