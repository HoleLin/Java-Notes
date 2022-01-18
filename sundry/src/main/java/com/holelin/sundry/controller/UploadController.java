package com.holelin.sundry.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 文件上传
 * @Author: HoleLin
 * @CreateDate: 2022/1/17 2:59 PM
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/1/17 2:59 PM
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {
    private static final Map<String, String> CACHE = new HashMap<>(16);
    private static final String PREFIX = "pass_upload_";
    private static final String SUFFIX = ".temp";
    private static final String EMPTY = "";

    @RequestMapping("/pass")
    public String passUpload(@RequestParam("file") MultipartFile file) {
        try {
            final String digest = SecureUtil.md5(file.getInputStream());
            // 使用Map模拟Redis
            if (CACHE.containsKey(digest)) {
                log.info("重复文件");
                return CACHE.get(digest);
            } else {
                log.info("新文件");
                final File tempFile = File.createTempFile(PREFIX, SUFFIX);
                file.transferTo(tempFile);
                final String absolutePath = tempFile.getAbsolutePath();
                CACHE.put(digest, absolutePath);
                return absolutePath;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EMPTY;
    }
}
