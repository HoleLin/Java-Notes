package com.holelin.sundry.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/9/7 16:47
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/9/7 16:47
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Data
public class ExcelData {
    /**
     * 组织机构名称
     */
    @ExcelProperty("公安机关")
    private String departmentName;
    /**
     * 国标ID
     */
    @ExcelProperty("设备编码")
    private String gbid;
    /**
     * 设备名称
     */
    @ExcelProperty("设备名称")
    private String deviceName;

}
