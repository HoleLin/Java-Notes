package com.holelin.elasticsearch.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class ElasticSearchTest {
    public static void main(String[] args) {

        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

        
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
