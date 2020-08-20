package com.holelin.sundry.demo;

import com.holelin.sundry.domain.Employee;
import org.junit.jupiter.api.Test;

import java.io.*;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/8/4 18:53
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/8/4 18:53
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

public class IOTest {

    /**
     * 字节数组处理流，在内存中建立一个缓冲区作为流使用，从缓存区读取数据比从存储介质(如磁盘)的速率快
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        // ===============================================================
        // 字节数组处理流，在内存中建立一个缓冲区作为流使用，从缓存区读取数据比从存储介质(如磁盘)的速率快
        //用ByteArrayOutputStream暂时缓存来自其他渠道的数据
        //1024字节大小的缓存区
        ByteArrayOutputStream data = new ByteArrayOutputStream(1024);
        // 暂存用户输入数据
        data.write(System.in.read());

        //将data转为ByteArrayInputStream
        ByteArrayInputStream in = new ByteArrayInputStream(data.toByteArray());
    }

    /**
     * FileInputStream和FileOutputStream
     *
     * 访问文件，把文件作为InputStream，实现对文件的读写操作
     * ObjectInputStream和ObjectOutputStream
     *
     * 对象流，构造函数需要传入一个流，实现对JAVA对象的读写功能；可用于序列化，而对象需要实现Serializable接口
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    public void test2() throws IOException, ClassNotFoundException {
        // ===============================================================
        // java对象的写入
        FileOutputStream fileStream = new FileOutputStream("example.txt");
        ObjectOutputStream out = new ObjectOutputStream(fileStream);
        Employee employee = new Employee();
        out.writeObject(employee);

        // java对象的读取
        FileInputStream fileStream2 = new FileInputStream("example.txt");
        ObjectInputStream in = new ObjectInputStream(fileStream2);
        Employee employee2 = (Employee) in.readObject();
    }


    /**
     * 管道流，适用在两个线程中传输数据，一个线程通过管道输出流发送数据，另一个线程通过管道输入流读取数据，实现两个线程间的数据通信
     */
    public void test3(){
        // 创建一个发送者对象
        // 创建一个接收者对象
//        Sender sender = new Sender();
//        // 获取输出管道流
//        Receiver receiver = new Receiver();
//        // 获取输入输出管道流
//        PipedOutputStream outputStream = sender.getOutputStream();
//        PipedInputStream inputStream = receiver.getInputStream();
//         // 链接两个管道，这一步很重要，把输入流和输出流联通起来
//        outputStream.connect(inputStream);
//        // 启动发送者线程
//        sender.start();
//        // 启动接收者线程
//        receiver.start();
    }

    /**
     * 把多个InputStream合并为一个InputStream，允许应用程序把几个输入流连续地合并起来
     * @throws IOException
     */
    public void test4() throws IOException {
        InputStream in1 = new FileInputStream("example1.txt");
        InputStream in2 = new FileInputStream("example2.txt");
        SequenceInputStream sequenceInputStream = new SequenceInputStream(in1, in2);
        //数据读取
        int data = sequenceInputStream.read();
    }

    public void test5(){

    }
}
