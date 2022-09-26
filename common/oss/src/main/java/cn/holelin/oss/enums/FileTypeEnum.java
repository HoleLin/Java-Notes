package cn.holelin.oss.enums;

import java.util.HashSet;
import java.util.Set;

/**
 * @Description: 真实文件类型枚举类
 * @Author: HoleLin
 * @CreateDate: 2022/3/30 1:36 PM
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/3/30 1:36 PM
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public enum FileTypeEnum {

    /**
     * 压缩包
     */
    ZIP("504B0304", ".zip", 0),
    RAR("526172211A07", ".rar", 0),
    /**
     * DICOM影像
     * 文件开头会有128字节的导言，这部分数据没有内容。接着是4字节DICOM文件标识，存储这"DICOM"。
     */
    DICOM("4449434D", ".dcm", 0x80),
    /**
     * 图片
     */
    JPG("FFD8", ".jpg", 0),
    PNG("89504E470D0A1A0A", ".png", 0),

    /**
     * Excel
     */
    XLS_2003("D0CF11E0A1B11AE1", ".xls", 0),
    XLSX_2007("504B0304", ".xlsx", 0),
    /**
     * PDF
     */
    PDF("25504446", ".pdf", 0),
    /**
     * 未知
     */
    UNKNOWN("", "", 0);
    /**
     * 签名
     */
    String signature;
    /**
     * 文件扩展名
     */
    String extension;
    /**
     * 偏移量
     */
    int offset;

    public String getSignature() {
        return signature;
    }

    public String getExtension() {
        return extension;
    }

    public int getOffset() {
        return offset;
    }

    FileTypeEnum(String signature, String extension, int offset) {
        this.signature = signature;
        this.extension = extension;
        this.offset = offset;
    }

    public static FileTypeEnum deduceFileType(String fileType) {
        for (FileTypeEnum value : FileTypeEnum.values()) {
            if (value.toString().equals(fileType)) {
                return value;
            }
        }
        return FileTypeEnum.UNKNOWN;
    }

    /**
     * 支持的
     */
    static Set<FileTypeEnum> IMAGE_FILE_SET = new HashSet<>(16);

    static {
        IMAGE_FILE_SET.add(FileTypeEnum.JPG);
        IMAGE_FILE_SET.add(FileTypeEnum.PNG);
    }
}
