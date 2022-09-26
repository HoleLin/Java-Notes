package cn.holelin.oss.config;

import cn.holelin.oss.config.properties.OssProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/6/30 11:40
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/6/30 11:40
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "oss")
public class OssConfig {

    /**
     * 阿里云的配置
     */
    @NestedConfigurationProperty
    private OssProperties aliYunProperties = new OssProperties();

    /**
     * Minio的配置
     */
    @NestedConfigurationProperty
    private OssProperties minioProperties = new OssProperties();

}
