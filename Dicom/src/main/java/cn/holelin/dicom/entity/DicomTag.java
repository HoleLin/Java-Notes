package cn.holelin.dicom.entity;

import cn.holelin.dicom.enums.DicomTagValueTypeEnum;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/3/23 4:43 PM
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/3/23 4:43 PM
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

public class DicomTag {
    private Integer id;
    /**
     * Tag标识
     */
    private String flag;
    /**
     * 标准坐标
     */
    private String coordinates;
    /**
     * 是否需要校验
     */
    private Boolean needCheck;
    /**
     * 值的类型
     *
     * @see DicomTagValueTypeEnum
     */
    private String valueType;
    /**
     * 描述
     */
    private String description;
}
