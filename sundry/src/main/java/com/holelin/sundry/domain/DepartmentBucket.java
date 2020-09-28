package com.holelin.sundry.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/9/25 17:25
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/9/25 17:25
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
public class DepartmentBucket {
    /**
     * 组织机构的名称
     */
    private String departmentName;
    /**
     * 在配置项中的索引位置
     */
    private Integer index;
}
