package com.holelin.sundry.test.io;

import com.google.common.io.ByteStreams;
import com.google.common.io.LineProcessor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/7/27 18:21
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/7/27 18:21
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class NIOTest {

    public static final String SMALL_FILE_PATH = "/Users/holelin/Downloads/手术-示例报文.txt";
    public static final String LARGE_FILE_PATH = "/Users/holelin/Downloads/win11-22000-arm64.ISO";
    public static final String DEMO_FILE_PATH = "/Users/holelin/Downloads/demo.txt";
    public static final String RENAME_FILE_PATH = "/Users/holelin/Downloads/demo2.txt";
    public static final String MOVE_FILE_PATH = "/Users/holelin/demo2.txt";
    public static final String READ_ONLY_FILE_PATH = "/Users/holelin/Downloads/read_only.txt";
    public static final String fileName = "bootstrap.yml";

    @Test
    public void readSmallFile() throws IOException {
        try (RandomAccessFile aFile = new RandomAccessFile(SMALL_FILE_PATH, "r");
             FileChannel inChannel = aFile.getChannel();) {

            long fileSize = inChannel.size();

            //Create buffer of the file size
            ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
            inChannel.read(buffer);
            buffer.flip();

            // Verify the file content
            for (int i = 0; i < fileSize; i++) {
                System.out.print((char) buffer.get());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readLargeFile() {
        try (RandomAccessFile aFile = new RandomAccessFile(SMALL_FILE_PATH, "r");
             FileChannel inChannel = aFile.getChannel();) {

            //Buffer size is 1024
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (inChannel.read(buffer) > 0) {
                buffer.flip();
                for (int i = 0; i < buffer.limit(); i++) {
                    System.out.print((char) buffer.get());
                }
                buffer.clear(); // do something with the data and clear/compact it.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readUsingMappedByteBuffer() {
        try (RandomAccessFile aFile = new RandomAccessFile(SMALL_FILE_PATH, "r");
             FileChannel inChannel = aFile.getChannel();) {

            MappedByteBuffer buffer = inChannel
                    .map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());

            buffer.load();
            for (int i = 0; i < buffer.limit(); i++) {
                System.out.print((char) buffer.get());
            }
            buffer.clear(); // do something with the data and clear/compact it.

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void writeUsingChannel() {
        Path filePath = Path.of("demo.txt");
        String content = "hello world !!";
        try (
                RandomAccessFile stream = new RandomAccessFile(filePath.toFile(), "rw");
                FileChannel channel = stream.getChannel();) {

            byte[] strBytes = content.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(strBytes.length);

            buffer.put(strBytes);
            buffer.flip();
            channel.write(buffer);
        } catch (Exception e) {

        }
    }

    @Test
    public void writeUsingFileOutputStream() {
        Path filePath = Path.of("demo.txt");
        String content = "hello world !!1";

        try (FileOutputStream outputStream
                     = new FileOutputStream(filePath.toFile())) {

            byte[] strToBytes = content.getBytes();
            outputStream.write(strToBytes);
        } catch (Exception e) {

        }
    }

    @Test
    public void outputStreamToInputStream() {
        try (FileOutputStream fos = new FileOutputStream(new File("/Users/holelin/Projects/MySelf/Java-Notes/sundry/demo.txt"));
             FileInputStream fis = new FileInputStream(new File("/Users/holelin/Projects/MySelf/Java-Notes/sundry/demo2.txt"));) {

            FileChannel outputChannel = fos.getChannel();
            FileChannel inputChannel = fis.getChannel();

            outputChannel.transferTo(0, inputChannel.size(), inputChannel);
        } catch (Exception e) {

        }
    }

    @Test
    public void channelTransfer() throws IOException {
        //Input files
        String[] inputFiles = new String[]{"demo.txt", "demo2.txt", "demo3.txt"};

        //Files contents will be written in these files
        String outputFile = "outputFile.txt";

        //Get channel for output file
        FileOutputStream fos = new FileOutputStream(new File(outputFile));
        WritableByteChannel targetChannel = fos.getChannel();

        for (int i = 0; i < inputFiles.length; i++) {
            //Get channel for input files
            FileInputStream fis = new FileInputStream(inputFiles[i]);
            FileChannel inputChannel = fis.getChannel();

            //Transfer data from input channel to output channel
            inputChannel.transferTo(0, inputChannel.size(), targetChannel);

            //close the input channel
            inputChannel.close();
            fis.close();
        }

        //finally close the target channel
        targetChannel.close();
        fos.close();

    }

    @Test
    public void createFile() throws IOException {
        // crate
        final Path path = Paths.get(DEMO_FILE_PATH);
        if (!path.toFile().exists()) {
            Files.createFile(path);
        }
        // create read only file
        final Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("r--r--r--");
        final FileAttribute<Set<PosixFilePermission>> attribute = PosixFilePermissions.asFileAttribute(permissions);
        final Path readOnlyPath = Paths.get(READ_ONLY_FILE_PATH);
        if (!readOnlyPath.toFile().exists()) {
            Files.createFile(readOnlyPath, attribute);
        }
    }

    @Test
    public void readFileToByte() throws IOException {
        // Java 8
        final Path path = Paths.get(SMALL_FILE_PATH);
        final byte[] bytes = Files.readAllBytes(path);

        // Java 6
        final File file = new File(SMALL_FILE_PATH);
        FileInputStream fileInputStream = null;
        final byte[] bfile = new byte[(int) file.length()];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bfile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Apache Commons IO
        //Using FileUtils.readFileToByteArray()
        final byte[] bytes1 = FileUtils.readFileToByteArray(file);
        //Using IOUtils.toByteArray
        final byte[] bytes2 = IOUtils.toByteArray(new FileInputStream(file));

        // Guava
        final byte[] bytes3 = com.google.common.io.Files.toByteArray(file);
        final byte[] bytes4 = ByteStreams.toByteArray(new FileInputStream(file));
    }

    @Test
    public void readLines() {
        final Path path = Paths.get(SMALL_FILE_PATH);
        try (final Stream<String> lines = Files.lines(path)) {
            final List<String> filteredLines = lines.filter(s -> s.contains("body")).collect(Collectors.toList());
            filteredLines.forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        File file = new File(SMALL_FILE_PATH);
        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr);) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            List<String> lines = com.google.common.io.Files.readLines(file,
                    Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // With LineProcessor
        LineProcessor<List<String>> lineProcessor = new LineProcessor<>() {
            final List<String> result = new ArrayList<>();

            @Override
            public boolean processLine(final String line) throws IOException {
                result.add(StringUtils.capitalize(line));
                return true; // keep reading
            }

            @Override
            public List<String> getResult() {
                return result;
            }
        };

        try {
            List<String> lines = com.google.common.io.Files
                    .asCharSource(file, Charset.defaultCharset())
                    .readLines(lineProcessor);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            List<String> lines = FileUtils.readLines(file, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readFileFromClassPath() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final URL resource = classLoader.getResource("bootstrap.yml");
        if (Objects.isNull(resource)) {
            throw new FileNotFoundException("file is not found!");
        } else {
            final File file = new File(resource.getFile());
            try {
                List<String> lines = FileUtils.readLines(file, Charset.defaultCharset());
                lines.forEach(System.out::println);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void readFileFromResourceDirectory() {
        final InputStream is = this.getClass().getClassLoader()
                .getResourceAsStream(fileName);
        try (InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr);) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            is.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final URL resource = this.getClass().getClassLoader().getResource(fileName);
        final File file = new File(resource.getFile());
    }


    @Test
    public void writeFile() {
        // fast writing fileChannel and bytebuffer
        Path filePath = Path.of(DEMO_FILE_PATH);
        String content = "hello world !!";
        try (
                RandomAccessFile stream = new RandomAccessFile(filePath.toFile(), "rw");
                FileChannel channel = stream.getChannel();) {

            byte[] strBytes = content.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(strBytes.length);

            buffer.put(strBytes);
            buffer.flip();
            channel.write(buffer);
        } catch (Exception e) {

        }

        content = "hello world !!2";
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(filePath.toFile()))) {
            writer.write(content);
        } catch (Exception e) {

        }

        // Using FileOutputStream
        try (FileOutputStream outputStream
                     = new FileOutputStream(filePath.toFile())) {
            byte[] strToBytes = content.getBytes();
            outputStream.write(strToBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Using DataOutputStream
        try (FileOutputStream outputStream
                     = new FileOutputStream(filePath.toFile());
             DataOutputStream dataOutStream
                     = new DataOutputStream(new BufferedOutputStream(outputStream));) {
            dataOutStream.writeUTF(content);
            dataOutStream.writeInt(10);
            dataOutStream.writeLong(100L);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void appendToFile() throws IOException {
        // Using NIO Files
        String textToAppend = "Happy Learning !!";
        Path path = Paths.get(DEMO_FILE_PATH);
        Files.write(path, textToAppend.getBytes(), StandardOpenOption.APPEND);

        // Using BufferedWriter
        try (FileWriter fw = new FileWriter(DEMO_FILE_PATH, true);
             BufferedWriter writer = new BufferedWriter(fw);) {

            writer.write(textToAppend);
        }

        // Using FileOutputStream
        try (FileOutputStream outputStream
                     = new FileOutputStream(fileName, true)) {

            byte[] strToBytes = textToAppend.getBytes();
            outputStream.write(strToBytes);
        }
    }

    @Test
    public void writeFileWithEncode() {
        // Writing UTF-8 Encoded Data into a File
        try (Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(DEMO_FILE_PATH), StandardCharsets.UTF_8))) {

            out.append("Test")
                    .append("\r\n")
                    .append("UTF-8 Demo")
                    .append("\r\n")
                    .append("冲冲冲")
                    .append("\r\n");

            out.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void moveAndRename() throws IOException {

        // File.renameTo()
        File originalFile = new File(DEMO_FILE_PATH);
        File renamedFile = new File(RENAME_FILE_PATH);
        File movedFile = new File(MOVE_FILE_PATH);
        final boolean b = originalFile.renameTo(renamedFile);
        final boolean b1 = renamedFile.renameTo(movedFile);

        // NIO Files.move
        final Path path = Path.of(DEMO_FILE_PATH);
        // rename in same directory
        Files.move(path, path.resolveSibling("test.txt"));

        // FileUtils
        FileUtils.moveFile(originalFile, renamedFile);
        File targetDirectory = new File(DEMO_FILE_PATH);
        FileUtils.moveFileToDirectory(originalFile, targetDirectory, true);
    }

    @Test
    public void copyFile() throws IOException {
        // Files.copy
        final Path source = Paths.get(DEMO_FILE_PATH);
        final Path target = Paths.get(source.resolveSibling("test.txt").toString());
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

        // FileChannel.transferTo()
        final File file = source.toFile();
        final FileInputStream sourceInputStream = new FileInputStream(file);
        final FileChannel inChannel = sourceInputStream.getChannel();
        final FileOutputStream targetOutputStream = new FileOutputStream(target.toFile());
        final FileChannel outChannel = targetOutputStream.getChannel();

        inChannel.transferTo(0, file.length(), outChannel);
        sourceInputStream.close();
        targetOutputStream.close();

    }

    @Test
    public void deleteFile() throws IOException {
        final Path path = Paths.get(DEMO_FILE_PATH);
        final File file = path.toFile();

        // java.nio.file.Files
        Files.delete(path);
        Files.deleteIfExists(path);

        // org.apache.commons.io.FileUtils
        FileUtils.delete(file);
        FileUtils.deleteQuietly(file);
        FileUtils.deleteDirectory(new File("/Users/holelin/Downloads/a"));
    }

    @Test
    public void fileSize() throws IOException {
        final Path path = Paths.get(DEMO_FILE_PATH);
        final long size = Files.size(path);
        System.out.println(size);
        final long sizeOf = FileUtils.sizeOf(path.toFile());
        System.out.println(FileUtils.byteCountToDisplaySize(sizeOf));
    }

}
