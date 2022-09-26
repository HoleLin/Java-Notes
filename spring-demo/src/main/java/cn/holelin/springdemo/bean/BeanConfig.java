package cn.holelin.springdemo.bean;

import cn.holelin.springdemo.bean.bean.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/8/30 16:38
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/8/30 16:38
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Configuration
public class BeanConfig {


    @Bean
    public List<User> users(){
        final ArrayList<User> users = new ArrayList<>();
        users.add(User.builder().userName("123").order(Integer.MAX_VALUE).build());
        users.add(User.builder().userName("234").order(Integer.MIN_VALUE).build());
        return users;
    }
}
