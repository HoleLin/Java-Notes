package com.holelin.sundry.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/download")
public class DownLoadController {
    private final static String utf8 = "utf-8";
    final String pathname = "/Users/holelin/data/ISO/win11-22000-arm64.ISO";


    @RequestMapping("/down")
    public void downLoadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding(utf8);
        //定义文件路径
        File file = new File("/Users/holelin/Projects/MySelf/Java-Notes/sundry/src/main/resources/upload/win10-21354-arm64.ISO");
        InputStream is = null;
        OutputStream os = null;
        try {
            //分片下载
            long fSize = file.length();//获取长度
            response.setContentType("application/x-download");
            String fileName = URLEncoder.encode(file.getName(), utf8);
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            //根据前端传来的Range  判断支不支持分片下载
            response.setHeader("Accept-Range", "bytes");
            //获取文件大小
            response.setHeader("fSize", String.valueOf(fSize));
            response.setHeader("fName", fileName);
            //定义断点
            long pos = 0, last = fSize - 1, sum = 0;
            //判断前端需不需要分片下载
            if (null != request.getHeader("Range")) {
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                String numRange = request.getHeader("Range").replaceAll("bytes=", "");
                String[] strRange = numRange.split("-");
                if (strRange.length == 2) {
                    pos = Long.parseLong(strRange[0].trim());
                    last = Long.parseLong(strRange[1].trim());
                    //若结束字节超出文件大小 取文件大小
                    if (last > fSize - 1) {
                        last = fSize - 1;
                    }
                } else {
                    //若只给一个长度  开始位置一直到结束
                    pos = Long.parseLong(numRange.replaceAll("-", "").trim());
                }
            }
            long rangeLenght = last - pos + 1;
            String contentRange = new StringBuffer("bytes").append(pos).append("-").append(last).append("/").append(fSize).toString();
            response.setHeader("Content-Range", contentRange);
            response.setHeader("Content-Length", String.valueOf(rangeLenght));
            os = new BufferedOutputStream(response.getOutputStream());
            is = new BufferedInputStream(new FileInputStream(file));
            //跳过已读的文件
            is.skip(pos);
            byte[] buffer = new byte[1024];
            int length = 0;
            //相等证明读完
            while (sum < rangeLenght) {
                length = is.read(buffer, 0, (rangeLenght - sum) <= buffer.length ? (int) (rangeLenght - sum) : buffer.length);
                sum = sum + length;
                os.write(buffer, 0, length);

            }
            System.out.println("下载完成");
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    @GetMapping("/download-by-nio")
    public void downloadByNIO(HttpServletResponse response) {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final File file = new File(pathname);
        OutputStream os = null;
        //分配缓冲区 单位kb
        ByteBuffer buffer = ByteBuffer.allocateDirect(786432);

        try {
            // 取得输出流
            os = response.getOutputStream();
            String contentType = Files.probeContentType(Paths.get(file.getAbsolutePath()));
            response.setHeader("Content-Type", contentType);
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes("utf-8"), "ISO8859-1"));
            FileInputStream fileInputStream = new FileInputStream(file);
            WritableByteChannel writableByteChannel = Channels.newChannel(os);
            FileChannel fileChannel = fileInputStream.getChannel();
            //将通道中数据存入缓冲区
            while (fileChannel.read(buffer) != -1) {
                //Flips this buffer. The limit is set to the current position and then the position is set to zero. If the mark is defined then it is discarded
                //将缓存字节数组的指针设置为数组的开始序列即数组下标0。这样就可以从buffer开头，对该buffer进行遍历读取,读出当前缓冲区中的数据，不是按照缓冲区容量而是内容长度
                buffer.flip();
                //将缓冲区数据写入到输出通道中
                writableByteChannel.write(buffer);
                //写完数据清除缓冲区
                buffer.clear();
            }
            fileChannel.close();
            os.flush();
            writableByteChannel.close();
            stopWatch.stop();
            log.info("nio下载共耗时:{}ms,{}s", stopWatch.getTotalTimeMillis(), stopWatch.getTotalTimeSeconds());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //文件的关闭放在finally中
        finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/download-by-nio2")
    public void downloadByNIO2(HttpServletResponse response) {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final File file = new File(pathname);
        OutputStream os = null;
        //分配缓冲区 单位kb
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            // 取得输出流
            os = response.getOutputStream();
            String contentType = Files.probeContentType(Paths.get(file.getAbsolutePath()));
            response.setHeader("Content-Type", contentType);
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes("utf-8"), "ISO8859-1"));
            FileInputStream fileInputStream = new FileInputStream(file);
            FileChannel fileChannel = fileInputStream.getChannel();

            byte[] byteArr = new byte[1024];
            int nRead, nGet;

            while ((nRead = fileChannel.read(buffer)) != -1) {
                if (nRead == 0) {
                    continue;
                }
                buffer.position(0);
                buffer.limit(nRead);
                while (buffer.hasRemaining()) {
                    nGet = Math.min(buffer.remaining(), 1024);
                    // read bytes from disk
                    buffer.get(byteArr, 0, nGet);
                    // write bytes to output
                    response.getOutputStream().write(byteArr);
                }
                buffer.clear();

            }
            fileChannel.close();
            stopWatch.stop();
            log.info("nio下载共耗时:{}ms,{}s", stopWatch.getTotalTimeMillis(), stopWatch.getTotalTimeSeconds());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //文件的关闭放在finally中
        finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response) {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final File file = new File(pathname);
        OutputStream os = null;
        try {
            // 取得输出流
            os = new BufferedOutputStream(response.getOutputStream());
            String contentType = Files.probeContentType(Paths.get(file.getAbsolutePath()));
            response.setHeader("Content-Type", contentType);
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes("utf-8"), "ISO8859-1"));
            InputStream fileInputStream = new BufferedInputStream(new FileInputStream(file));
            final byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(buffer, 0, 1024)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
            stopWatch.stop();
            log.info("下载共耗时:{}ms,{}s", stopWatch.getTotalTimeMillis(), stopWatch.getTotalTimeSeconds());

        } catch (IOException e) {
            e.printStackTrace();
        }
        //文件的关闭放在finally中
        finally {
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