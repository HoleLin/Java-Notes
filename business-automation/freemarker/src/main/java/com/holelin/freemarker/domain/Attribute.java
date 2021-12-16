package com.holelin.freemarker.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2021/10/28 11:34 上午
 * @UpdateUser: HoleLin
 * @UpdateDate: 2021/10/28 11:34 上午
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
public class Attribute {
    private String type;
    private String name;
}
