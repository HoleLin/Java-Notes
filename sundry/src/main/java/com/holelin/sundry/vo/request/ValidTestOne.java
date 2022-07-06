package com.holelin.sundry.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/7/18 22:55
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/7/18 22:55
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class ValidTestOne {

    @Min(value = 10000000000000000L, groups = Update.class)
    private Long userId;

    @NotNull(groups = {Save.class, Update.class})
    @Length(min = 2, max = 10, groups = {Save.class, Update.class})
    private String userName;

    @NotNull(groups = {Save.class, Update.class})
    @Length(min = 6, max = 20, groups = {Save.class, Update.class})
    private String account;

    @NotNull(groups = {Save.class, Update.class})
    @Length(min = 6, max = 20, groups = {Save.class, Update.class})
    private String password;

    /**
     * 保存的时候校验分组
     */
    public interface Save {
    }

    /**
     * 更新的时候校验分组
     */
    public interface Update {
    }
}
