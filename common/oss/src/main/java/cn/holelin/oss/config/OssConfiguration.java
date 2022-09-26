package cn.holelin.oss.config;

import cn.holelin.oss.config.properties.OssProperties;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: OSS配置类
 * @Author: HoleLin
 * @CreateDate: 2022/6/30 11:01
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/6/30 11:01
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Configuration
public class OssConfiguration {


    @Autowired
    private OssConfig ossConfig;

    @Bean
    public MinioClient aliYunClient() {
        final OssProperties aliYunProperties = ossConfig.getAliYunProperties();
        return MinioClient.builder()
                .endpoint(aliYunProperties.getEndpoint())
                .region(aliYunProperties.getRegion())
                .credentials(aliYunProperties.getAccessKey(), aliYunProperties.getAccessSecret())
                .build();
    }

    @Bean
    public MinioClient minioClient() {
        final OssProperties minioProperties = ossConfig.getMinioProperties();
        return MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getAccessSecret())
                .build();
    }

}
