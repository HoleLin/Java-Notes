package com.holelin.sundry.vo.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/5/28 19:10
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/5/28 19:10
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class MultipartFileRequest {

    /**
     * 用户id
     */
    private String uid;
    /**
     * 任务ID
     */
    private String id;
    /**
     * 总分片数量
     */
    private int chunks;
    /**
     * 当前为第几块分片
     */
    private int chunk;
    /**
     * 当前分片大小
     */
    private long size = 0L;
    /**
     * 文件名
     */
    private String name;
    /**
     * 分片对象
     */
    private MultipartFile file;
    /**
     * MD5
     */
    private String md5;
}
