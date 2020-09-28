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
    @ExcelProperty("部门名称")
    private String departmentName;

}
