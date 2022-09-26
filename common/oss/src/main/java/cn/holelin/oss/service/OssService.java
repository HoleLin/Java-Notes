package cn.holelin.oss.service;

import cn.holelin.oss.config.OssConfig;
import cn.holelin.oss.constants.StringConstants;
import cn.holelin.oss.enums.FileBigCategoryEnum;
import cn.holelin.oss.enums.FileTypeEnum;
import cn.holelin.oss.enums.OssSourceEnum;
import cn.holelin.oss.util.FileFormatValidatorUtil;
import cn.holelin.oss.util.OssUtil;
import cn.holelin.oss.util.SnowflakeUtil;
import cn.hutool.core.lang.Assert;
import io.minio.MinioClient;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/7/1 16:36
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/7/1 16:36
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Service
public class OssService {
    @Qualifier("aliYunClient")
    @Autowired
    private MinioClient aliYunClient;

    @Qualifier("minioClient")
    @Autowired
    private MinioClient minioClient;

    @Autowired
    private OssConfig ossConfig;
    public static final String FILE_DIR_PATH = "classpath:files";

    public void download(String objectName, Integer code, HttpServletResponse response) {
        OssUtil ossUtil;
        if (code == 1) {
            ossUtil = new OssUtil(aliYunClient);
            ossUtil.download(ossConfig.getAliYunProperties().getBucketName(), objectName, response);

        } else {
            ossUtil = new OssUtil(minioClient);
            ossUtil.download(ossConfig.getMinioProperties().getBucketName(), objectName, response);
        }
    }

    public String upload(MultipartFile file, String bigFileCategory,
                         Integer storeSourceCode, String userId) {
        if (Objects.nonNull(file)) {
            File templateFile;
            final String originalFilename = file.getOriginalFilename();
            try {
                File dir = ResourceUtils.getFile(FILE_DIR_PATH);
                templateFile = new File(dir.getAbsolutePath() + File.separator + originalFilename);
                file.transferTo(templateFile);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            // 检查文件类型是否合法
            final FileBigCategoryEnum fileBigCategoryEnum = FileBigCategoryEnum.deduceFileType(bigFileCategory);
            Assert.isFalse(FileBigCategoryEnum.UNKNOWN.equals(fileBigCategoryEnum), "bigFileType传值有误,目前支持的传值有:{}", FileBigCategoryEnum.getLegalValues());
            final FileTypeEnum realFileTypeEnum = FileFormatValidatorUtil.deduceFileType(templateFile);
            final Map<FileBigCategoryEnum, Set<FileTypeEnum>> legalMap = FileBigCategoryEnum.LEGAL_MAP;
            final Set<FileTypeEnum> fileTypeEnums = legalMap.get(fileBigCategoryEnum);
            final List<String> legalFileTypeList = fileTypeEnums.stream().map(Enum::toString).collect(Collectors.toList());
            Assert.isTrue(fileTypeEnums.contains(realFileTypeEnum), "上传的的文件类型有误,大分类文件类型:{}下支持的具体的文件类型列表如下:{}", bigFileCategory, legalFileTypeList);


            // 校验通过后,根据/bigFileCategory/realFileType/filename_uid
            final String objectName = StringConstants.OSS_PREFIX + StringConstants.SYMBOL_Slash +
                    bigFileCategory + StringConstants.SYMBOL_Slash +
                    realFileTypeEnum + StringConstants.SYMBOL_Slash +
                    SnowflakeUtil.genId() +
                    realFileTypeEnum.getExtension();
            if (OssSourceEnum.ALI_YUN.getCode().equals(storeSourceCode)) {
                final OssUtil ossUtil = new OssUtil(aliYunClient);
                ossUtil.upload(ossConfig.getAliYunProperties().getBucketName(), objectName, templateFile.getAbsolutePath());
            } else {
                final OssUtil ossUtil = new OssUtil(minioClient);
                ossUtil.upload(ossConfig.getMinioProperties().getBucketName(), objectName, templateFile.getAbsolutePath());
            }
            try {
                Files.deleteIfExists(templateFile.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return objectName;
        }
        return StringConstants.EMPTY;
    }

    public static void main(String[] args) {
        try {
            final String path = ResourceUtils.getFile("classpath:static") + File.separator + "test.jpg";
            Thumbnails.of(path)
                    .scale(1f)
                    .outputQuality(0.25f)
                    .toFile("/Users/holelin/Projects/MySelf/Java-Notes/common/oss/src/main/resources/static/0.25.jpg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
