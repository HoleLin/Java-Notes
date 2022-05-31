package com.holelin.sundry.controller;

import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
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
    private static final long CHUNK_SIZE = 10;
    private static final String FINAL_DIR_PATH = "/Users/holelin/Projects/MySelf/Java-Notes/sundry/src/main/resources/upload";

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

    private final static String utf8 = "utf-8";

    @PostMapping("/up")
    public void upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding(utf8);
        //长传时候会有多个分片，需要记录当前为那个分片
        Integer sChunk = null;
        //总分片数
        Integer sChunks = null;
        //名字
        String name = null;
        //文件目录
        String path = "/Users/holelin/Projects/MySelf/Java-Notes/sundry/src/main/resources/upload";
        BufferedOutputStream os = null;
        //Create a progress listener
        ProgressListener progressListener = new ProgressListener() {
            private long megaBytes = -1;

            public void update(long pBytesRead, long pContentLength, int pItems) {
                long mBytes = pBytesRead / (1024 * 1024 * 100);
                if (megaBytes == mBytes) {
                    return;
                }
                megaBytes = mBytes;
                System.out.println("We are currently reading item " + pItems);
                if (pContentLength == -1) {
                    System.out.println("So far, " + pBytesRead + " bytes have been read.");
                } else {
                    System.out.println("So far, " + pBytesRead + " of " + pContentLength
                            + " bytes have been read.");
                }
            }
        };

        try {
            // 设置缓冲区大小  先读到内存里在从内存写
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(1024);
            factory.setRepository(new File(path));
            // 解析
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 设置单个大小与最大大小
            upload.setFileSizeMax(5L * 1024L * 1024L * 1024L);
            upload.setSizeMax(10L * 1024L * 1024L * 1024L);
            upload.setProgressListener(progressListener);
            List<FileItem> items = upload.parseRequest(request);
            for (FileItem item : items) {
                if (item.isFormField()) {
                    //获取分片数赋值给遍量
                    if ("chunk".equals(item.getFieldName())) {
                        sChunk = Integer.parseInt(item.getString(utf8));
                    }
                    if ("chunks".equals(item.getFieldName())) {
                        sChunks = Integer.parseInt(item.getString(utf8));
                    }
                    if ("name".equals(item.getFieldName())) {
                        name = item.getString(utf8);
                    }
                } else {
                    // 判断当前对象是否为文件对象item.isFormField()为false代表为文件
                    // 有分片需要临时目录
                    String temFileName = name;
                    if (name != null) {
                        if (sChunk != null) {
                            temFileName = sChunk + "_" + name;
                        }
                        // 判断文件是否存在
                        File tempFile = new File(path, temFileName);
                        // 断点续传  判断文件是否存在，若存在则不传
                        if (!tempFile.exists()) {
                            item.write(tempFile);
                        }
                    }
                }
            }
            // 文件合并  当前分片为最后一个就合并
            // 文件合并schunk分片数不能为空才合并，分片数为空代表只有一个文件，没有分片,并且分片数要等于总分片数-1
            if (sChunk != null) {
                assert sChunks != null;
                if (sChunk == sChunks - 1) {
                    assert name != null;
                    File tempFile = new File(path, name);
                    os = new BufferedOutputStream(Files.newOutputStream(tempFile.toPath()));
                    //根据之前命名规则找到所有分片
                    for (int i = 0; i < sChunks; i++) {
                        File file = new File(path, i + "_" + name);
                        //并发情况 需要判断所有  因为可能最后一个分片传完，之前有的还没传完
                        while (!file.exists()) {
                            //不存在休眠100毫秒后在从新判断
                            Thread.sleep(100);
                        }
                        //分片存在  读入数组中
                        byte[] bytes = FileUtils.readFileToByteArray(file);
                        os.write(bytes);
                        os.flush();
                        Files.deleteIfExists(file.toPath());
                    }
                    os.flush();
                }
            }
            response.getWriter().write("上传成功");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
