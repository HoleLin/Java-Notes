package com.holelin.sundry.domain;

import lombok.Data;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/8/3 18:35
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/8/3 18:35
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Data
public class DepartmentConfigInfo {
    /**
     * 组织机构编码
     */
    private String departmentCode;
    /**
     * 组织机构名称
     */
    private String departmentName;
    /**
     * 处理完成后的组织机构编码
     */
    private String handleAfterDepartmentCode;
    /**
     * 排序字段
     */
    private Integer sort;
}
