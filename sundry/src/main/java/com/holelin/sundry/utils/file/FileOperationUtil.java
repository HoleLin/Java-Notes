package com.holelin.sundry.utils.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/9/28 15:03
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/9/28 15:03
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class FileOperationUtil {
    /**
     * NIO读写通道（双通道）进行文件的读写操作，数据使用一个缓冲区
     * @param source      源文件
     * @param target      目标文件
     * @throws Exception
     */
    public void nioCopyFile(String source, String target) throws  Exception{
        FileInputStream fis = new FileInputStream(source);
        FileOutputStream fos = new FileOutputStream(target);
        //读文件通道
        FileChannel readChannel = fis.getChannel();
        //写文件通道
        FileChannel writeChannel = fos.getChannel();
        //缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024*8);
        buffer.clear();
        while (readChannel.read(buffer) != -1){
            buffer.flip();
            writeChannel.write(buffer);
            buffer.clear();
        }
        readChannel.close();
        writeChannel.close();
        fis.close();
        fos.close();
    }

    /**
     * NIO 将文件映射到内存进行读和写
     * @param source      源文件
     * @param target      目标文件
     * @throws Exception
     */
    public void nioCopyFile2(String source, String target) throws  Exception{
        FileInputStream fis = new FileInputStream(source);
        //读文件通道
        FileChannel readChannel = fis.getChannel();
        //写文件通道
        FileChannel writeChannel = new RandomAccessFile(target,"rw").getChannel();
        int fileLen = fis.available();
        CharBuffer charReadBuffer = readChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileLen).asCharBuffer();
        CharBuffer charWriteBuffer = writeChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileLen).asCharBuffer();
        charWriteBuffer.put(charReadBuffer);
        readChannel.close();
        fis.close();
        writeChannel.close();

    }

    /**
     * IO读写文件，使用了缓冲区
     * @param source      源文件
     * @param target      目标文件
     * @throws Exception
     */
    public void ioCopyFile(String source, String target) throws  Exception{
        FileInputStream fis = new FileInputStream(source);
        BufferedInputStream bis = new BufferedInputStream(fis);
        FileOutputStream fos = new FileOutputStream(target);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        //缓冲区
        byte[] buffer = new byte[1024*8];
        int lenth = 0;
        while ((lenth = bis.read(buffer)) != -1){
            bos.write(buffer, 0 , lenth);
            bos.flush();
        }
        fis.close();
        bis.close();
        fos.close();
        bos.close();
    }
}
