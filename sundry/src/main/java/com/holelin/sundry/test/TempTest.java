package com.holelin.sundry.test;

import org.springframework.util.DigestUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TempTest {

    public static void main(String[] args) throws Exception {
//        Path path = Paths.get("C:\\Users\\YW\\Downloads\\test_stl\\648c20f23927af7c65be7aad.stl");
//        Path outputFile = Path.of("C:\\Users\\YW\\Downloads\\test_stl\\output4.stl");
        Path path = Paths.get("C:\\Users\\YW\\Downloads\\test_stl\\output2.stl");
        Path outputFile = Path.of("C:\\Users\\YW\\Downloads\\test_stl\\output4.stl");
        test4(path, outputFile);
    }

    private static void test1(Path path, Path outputFile) throws IOException {
        InputStream inputStream = new FileInputStream(path.toFile());
        // 通过文件通道进行文件读写操作
        try (FileInputStream is = new FileInputStream(path.toFile());
             FileChannel outChannel = new FileOutputStream(outputFile.toFile()).getChannel();
        ) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            byte[] bytes = new byte[1024];
            long length = path.toFile().length();
            long encryptStartPosition = length - 1024;
            if (encryptStartPosition < 0) {
                encryptStartPosition = 0;
            }
            int len;
            while ((len = is.read(bytes)) != -1) {
                buffer.put(bytes, 0, len);
                if (is.available() == 0 && outChannel.position() >= encryptStartPosition) {
                    buffer.flip();
                    byte[] encryptBytes = new byte[buffer.remaining()];
                    buffer.get(encryptBytes);
                    encryptBytes = AESUtils.aesDecryptBytesToBytes(encryptBytes, AESUtils.KEY);
                    buffer.clear();
                    buffer.put(encryptBytes);
                }
                buffer.flip();
                while (buffer.hasRemaining()) {
                    outChannel.write(buffer);
                }
                buffer.clear();
            }
            outChannel.force(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        String md51 = DigestUtils.md5DigestAsHex(new FileInputStream(path.toFile()));
        String md52 = DigestUtils.md5DigestAsHex(new FileInputStream(outputFile.toFile()));
        System.out.println(md51);
        System.out.println(md52);
    }
    private static void test2(Path path, Path outputFile) throws Exception {
        File file = path.toFile();
        InputStream inputStream = new FileInputStream(file);
        byte[] bytes = inputStreamToByteArray(inputStream);
        byte[] bytes1 = AESUtils.encryptFile(bytes);
        byteArrayToFile(bytes1,outputFile.toFile());

        String md51 = DigestUtils.md5DigestAsHex(new FileInputStream(file));
        String md52 = DigestUtils.md5DigestAsHex(new FileInputStream(outputFile.toFile()));
        System.out.println(md51);
        System.out.println(md52);
    }

    private static void test3(Path path, Path outputFile) throws Exception {
        File file = path.toFile();
        try (FileInputStream is = new FileInputStream(file);
             FileChannel outChannel = new FileOutputStream(outputFile.toFile()).getChannel();
        ) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            byte[] bytes = new byte[1024];
            long length = file.length();
            long encryptStartPosition = length - 1024;
            if (encryptStartPosition < 0) {
                encryptStartPosition = 0;
            }
            int len;
            while ((len = is.read(bytes)) != -1) {
                buffer.put(bytes, 0, len);
                if (is.available() == 0 && outChannel.position() >= encryptStartPosition) {
                    buffer.flip();
                    byte[] encryptBytes = new byte[buffer.remaining()];
                    buffer.get(encryptBytes);
                    encryptBytes = AESUtils.aesDecryptBytesToBytes(encryptBytes, AESUtils.KEY);
                    buffer.clear();
                    buffer.put(encryptBytes);
                }
                buffer.flip();
                while (buffer.hasRemaining()) {
                    outChannel.write(buffer);
                }
                buffer.clear();
            }
            outChannel.force(true);
        }

        String md51 = DigestUtils.md5DigestAsHex(new FileInputStream(file));
        String md52 = DigestUtils.md5DigestAsHex(new FileInputStream(outputFile.toFile()));
        System.out.println(md51);
        System.out.println(md52);
    }
    private static void test4(Path path, Path outputFile) throws Exception {
        File file = path.toFile();
        InputStream inputStream = new FileInputStream(file);
        byte[] bytes = inputStreamToByteArray(inputStream);
        byte[] bytes1 = AESUtils.decryptFile(bytes);
        byteArrayToFile(bytes1,outputFile.toFile());

        String md51 = DigestUtils.md5DigestAsHex(new FileInputStream(file));
        String md52 = DigestUtils.md5DigestAsHex(new FileInputStream(outputFile.toFile()));
        System.out.println(md51);
        System.out.println(md52);
    }
    public static void byteArrayToFile(byte[] byteArray, File file) throws IOException {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            outputStream.write(byteArray);
        }
    }
    public static byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[1024];

        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toByteArray();
    }
}
