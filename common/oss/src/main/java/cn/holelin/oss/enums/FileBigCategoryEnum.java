package cn.holelin.oss.enums;

import cn.holelin.oss.constants.NumberConstants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/7/1 10:59
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/7/1 10:59
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public enum FileBigCategoryEnum {
    /**
     * 压缩包
     */
    ZIP,
    /**
     * 文稿
     */
    DOC,
    /**
     * 图片
     */
    IMAGES,
    /**
     * 未知
     */
    UNKNOWN;

    public static List<String> getLegalValues(){
        return Arrays.stream(FileBigCategoryEnum.values()).map(Enum::toString).filter(it->!FileBigCategoryEnum.UNKNOWN.toString().equals(it)).collect(Collectors.toList());
    }
    public static FileBigCategoryEnum deduceFileType(String fileType) {
        for (FileBigCategoryEnum value : FileBigCategoryEnum.values()) {
            if (value.toString().equals(fileType)) {
                return value;
            }
        }
        return FileBigCategoryEnum.UNKNOWN;
    }

    /**
     * 合法支持的文件格式
     */
    public static Map<FileBigCategoryEnum, Set<FileTypeEnum>> LEGAL_MAP = new HashMap<>(NumberConstants.DEFAULT_LIST_CAPACITY);

    static Set<FileTypeEnum> LEGAL_ZIP_SET = new HashSet<>(NumberConstants.DEFAULT_LIST_CAPACITY);
    static Set<FileTypeEnum> LEGAL_IMAGE_SET = new HashSet<>(NumberConstants.DEFAULT_LIST_CAPACITY);
    static Set<FileTypeEnum> LEGAL_DOC_SET = new HashSet<>(NumberConstants.DEFAULT_LIST_CAPACITY);

    static {
        LEGAL_ZIP_SET.add(FileTypeEnum.ZIP);
        LEGAL_ZIP_SET.add(FileTypeEnum.RAR);
        LEGAL_MAP.put(FileBigCategoryEnum.ZIP, LEGAL_ZIP_SET);

        LEGAL_IMAGE_SET.add(FileTypeEnum.JPG);
        LEGAL_IMAGE_SET.add(FileTypeEnum.PNG);
        LEGAL_MAP.put(FileBigCategoryEnum.IMAGES, LEGAL_IMAGE_SET);

        LEGAL_DOC_SET.add(FileTypeEnum.PDF);
        LEGAL_DOC_SET.add(FileTypeEnum.XLS_2003);
        LEGAL_DOC_SET.add(FileTypeEnum.XLSX_2007);
        LEGAL_MAP.put(FileBigCategoryEnum.DOC, LEGAL_DOC_SET);

    }
}
