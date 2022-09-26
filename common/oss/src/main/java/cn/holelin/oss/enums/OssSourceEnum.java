package cn.holelin.oss.enums;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/6/30 15:15
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/6/30 15:15
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public enum OssSourceEnum {

    /**
     * 阿里云OSS
     */
    ALI_YUN(1),
    /**
     * 本地MINIO OSS
     */
    MINIO(2);

    Integer code;

    OssSourceEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
