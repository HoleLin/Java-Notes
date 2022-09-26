package cn.holelin.oss.config.properties;


import lombok.Data;

/**
 * @Description: OSS配置类
 * @Author: HoleLin
 * @CreateDate: 2022/6/30 11:19
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/6/30 11:19
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class OssProperties {

    private String bucketName;

    private String region;

    private String endpoint;

    private String accessKey;

    private String accessSecret;

}
