package cn.holelin.oss.util;

import cn.holelin.oss.enums.FileTypeEnum;
import cn.hutool.core.util.HexUtil;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Optional;

/**
 * @Description: 文件格式验证器
 * @Author: HoleLin
 * @CreateDate: 2022/3/29 4:58 PM
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/3/29 4:58 PM
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class FileFormatValidatorUtil {

    /**
     * 文件签名长度
     */
    private static final int FILE_SIGNATURE_LENGTH = 8;
    /**
     * 文件只读模式
     */
    private static final String FILE_ACCESS_MODE_READ = "r";

    /**
     * 根据文件对象以及偏移量推断文件类型
     *
     * @param file   具体文件对象
     * @param offset 偏移量
     * @return 文件类型
     */
    public static FileTypeEnum deduceFileType(File file, int offset) {
        final byte[] bytes = new byte[FILE_SIGNATURE_LENGTH];
        try (final RandomAccessFile randomAccessFile =
                     new RandomAccessFile(file, FILE_ACCESS_MODE_READ)) {
            randomAccessFile.skipBytes(offset);
            randomAccessFile.read(bytes);
            final String magicNumber = HexUtil.encodeHexStr(bytes).toUpperCase();
            final Optional<FileTypeEnum> fileTypeOptional = Arrays.stream(FileTypeEnum.values())
                    .filter(it -> magicNumber.startsWith(it.getSignature()))
                    .findFirst();
            if (fileTypeOptional.isPresent()) {
                return fileTypeOptional.get();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FileTypeEnum.UNKNOWN;
    }

    /**
     * 根据文件对象推断文件类型
     *
     * @param file 具体文件对象
     * @return 文件类型
     */
    public static FileTypeEnum deduceFileType(File file) {
        return deduceFileType(file, 0);
    }

    /**
     * 根据文件路径推断文件类型
     *
     * @param filePath 文件路径
     * @return 文件类型
     */
    public static FileTypeEnum deduceFileType(String filePath, int offset) {
        final File file = new File(filePath);
        return deduceFileType(file, offset);
    }

    /**
     * 判断文件是否是DICOM文件
     *
     * @param file 文件对象
     * @return true-是,false-否
     */
    public static Boolean isDicom(File file) {
        return FileTypeEnum.DICOM == deduceFileType(file, FileTypeEnum.DICOM.getOffset());
    }

    /**
     * 判断文件是否是ZIP包
     *
     * @param file 文件对象
     * @return true-是,false-否
     */
    public static Boolean isZip(File file) {
        return FileTypeEnum.ZIP == deduceFileType(file, FileTypeEnum.ZIP.getOffset());
    }

    /**
     * 判断文件是否是PDF
     *
     * @param file 文件对象
     * @return true-是,false-否
     */
    public static Boolean isPdf(File file) {
        return FileTypeEnum.PDF == deduceFileType(file, FileTypeEnum.PDF.getOffset());
    }

}
