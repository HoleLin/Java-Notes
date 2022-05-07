package cn.holelin.dicom.util.validator;

import cn.holelin.dicom.entity.DicomFrame;

/**
 * @Description: 验证器
 * @Author: HoleLin
 * @CreateDate: 2022/5/7 7:58 PM
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/5/7 7:58 PM
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public interface Validator {
    /**
     * dicom Tag值验证
     *
     * @param dicomFrame dicom对象
     * @return true--验证通过,false--验证不通过
     */
    Boolean validated(DicomFrame dicomFrame);

}
