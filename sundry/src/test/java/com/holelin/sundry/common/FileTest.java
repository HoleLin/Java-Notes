package com.holelin.sundry.common;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileTest {

    @Test
   public void testCreateFile1() throws IOException {
        String fileName = "C:\\Users\\Administrator\\Desktop\\新建文本文档\\新建文本文档2.txt";

        Path path = Paths.get(fileName);
        // 使用newBufferedWriter创建文件并写文件
        // 这里使用了try-with-resources方法来关闭流，不用手动关闭
        try (BufferedWriter writer =
                     Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write("Hello World -创建文件!!");
        }
        //追加写模式
        try (BufferedWriter writer =
                     Files.newBufferedWriter(path,
                             StandardCharsets.UTF_8,
                             StandardOpenOption.APPEND)) {
            writer.write("Hello World -字母哥!!");
        }
    }

    @Test
    public void testCreateFile2() throws IOException {
        String fileName = "C:\\Users\\Administrator\\Desktop\\新建文本文档\\新建文本文档3.txt";

        // 从JDK1.7开始提供的方法
        // 使用Files.write创建一个文件并写入
        Files.write(Paths.get(fileName),
                "Hello World -创建文件!!".getBytes(StandardCharsets.UTF_8));

        // 追加写模式
        Files.write(
                Paths.get(fileName),
                "Hello World -字母哥!!".getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.APPEND);
    }
    @Test
    public void testCreateFile3() throws IOException {
        String fileName = "C:\\Users\\Administrator\\Desktop\\新建文件夹\\新建文本文档4.txt";

        // JSD 1.5开始就已经存在的方法
        try (PrintWriter writer = new PrintWriter(fileName, "UTF-8")) {
            writer.println("Hello World -创建文件!!");
            writer.println("Hello World -字母哥!!");
        }

        // Java 10进行了改进，支持使用StandardCharsets指定字符集
//        try (PrintWriter writer = new PrintWriter(fileName, StandardCharsets.UTF_8)) {
//
//            writer.println("first line!");
//            writer.println("second line!");
//
//        }
    }

    @Test
    public void testCreateFile4() throws IOException {
        String fileName = "C:\\Users\\Administrator\\Desktop\\新建文件夹\\新建文本文档5.txt";

        File file = new File(fileName);

        // 返回true表示文件成功
        // false 表示文件已经存在
        if (file.createNewFile()) {
            System.out.println("创建文件成功！");
        } else {
            System.out.println("文件已经存在不需要重复创建");
        }
        // 使用FileWriter写文件
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Hello World -创建文件!!");
        }
    }
    @Test
    public void testCreateFile5() throws IOException {
        String fileName = "C:\\Users\\Administrator\\Desktop\\新建文件夹\\新建文本文档6.txt";
        try(FileOutputStream fos = new FileOutputStream(fileName);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);){
            bw.write("Hello World -创建文件!!");
            bw.flush();
        }
    }


}
