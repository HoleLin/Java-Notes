package cn.holelin.springdemo.bean.bean;

import lombok.Builder;
import lombok.Data;
import org.springframework.core.Ordered;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/8/14 18:32
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/8/14 18:32
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
public class User implements Ordered {
    private String id;
    private String userName;
    private String email;

    private int order;

    @Override
    public int getOrder() {
        return order;
    }
}
