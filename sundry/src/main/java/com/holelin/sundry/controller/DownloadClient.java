package com.holelin.sundry.controller;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class DownloadClient {
    private final static long per_page = 1024L * 1024L * 50L;
    //分片存储临时目录 当分片下载完后在目录中找到文件合并
    private final static String down_path = "/Users/holelin/Projects/MySelf/Java-Notes/sundry/src/main/resources/template";
    //多线程下载
    ExecutorService pool = Executors.newFixedThreadPool(10);

    //文件大小 分片数量 文件名称
    //使用探测 获取变量
    //使用多线程分片下载
    //最后一个分片下载完 开始合并
    @RequestMapping("/downloadFile")
    public String downloadFile() throws IOException {
        FileInfo fileInfo = download(0, 10, -1, null);
        if (fileInfo != null) {
            long pages = fileInfo.fSize / per_page;
            for (int i = 0; i <= pages; i++) {
                pool.submit(new Download(i * per_page, (i + 1) * per_page - 1, i, fileInfo.fName));
            }
        }

        return "成功";
    }

    class Download implements Runnable {
        long start;
        long end;
        long page;
        String fName;

        public Download(long start, long end, long page, String fName) {
            this.start = start;
            this.end = end;
            this.page = page;
            this.fName = fName;
        }

        @Override
        public void run() {
            try {
                FileInfo fileInfo = download(start, end, page, fName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //返回文件名 跟大小
    private FileInfo download(long start, long end, long page, String fName) throws IOException {
        //断点下载 文件存在不需要下载
        File file = new File(down_path, page + "-" + fName);
        //探测必须放行 若下载分片只下载一半就锻炼需要重新下载所以需要判断文件是否完整
        if (file.exists() && page != -1 && file.length() == per_page) {
            return null;
        }
        //需要知道  开始-结束 = 分片大小
        HttpClient client = HttpClients.createDefault();
        //httpclient进行请求
        HttpGet httpGet = new HttpGet("http://127.0.0.1:8094/down");
        //告诉服务端做分片下载
        httpGet.setHeader("Range", "bytes=" + start + "-" + end);
        HttpResponse response = client.execute(httpGet);
        String fSize = response.getFirstHeader("fSize").getValue();
        fName = URLDecoder.decode(response.getFirstHeader("fName").getValue(), "utf-8");
        //获取文件流对象
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        //临时存储分片文件
        FileOutputStream fos = new FileOutputStream(file);
        //定义缓冲区
        byte[] buffer = new byte[1024];
        int ch;
        while ((ch = is.read(buffer)) != -1) {
            fos.write(buffer, 0, ch);
        }
        is.close();
        fos.flush();
        fos.close();
        //判断是不是最后一个分片
        if (end - Long.valueOf(fSize) > 0) {
            //合并
            try {
                mergeFile(fName, page);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new FileInfo(Long.valueOf(fSize), fName);
    }

    private void mergeFile(String fName, long page) throws Exception {
        //归并文件位置
        File file = new File(down_path, fName);
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
        for (int i = 0; i <= page; i++) {
            File tempFile = new File(down_path, i + "-" + fName);
            //分片没下载或者没下载完需要等待
            while (!file.exists() || (i != page && tempFile.length() < per_page)) {
                Thread.sleep(100);
            }
            byte[] bytes = FileUtils.readFileToByteArray(tempFile);
            os.write(bytes);
            os.flush();
            tempFile.delete();
        }
        File file1 = new File(down_path, -1 + "-null");
        file1.delete();
        os.flush();
        os.close();
    }

    //使用内部类实现
    class FileInfo {
        long fSize;
        String fName;

        public FileInfo(long fSize, String fName) {
            this.fSize = fSize;
            this.fName = fName;
        }
    }
}