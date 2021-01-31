package com.holelin.jpademo;

import com.holelin.jpademo.dao.UserRepository;
import com.holelin.jpademo.dto.UserOnlyName;
import com.holelin.jpademo.entity.User;
import com.holelin.jpademo.dto.UserOnlyNameEmailDTO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    public void testSaveUser() {
        User user = userRepository.save(User.builder().name("jackxx").email("123456@126.com").build());
        Assert.assertNotNull(user);
        List<User> users= userRepository.findAll();
        System.out.println(users);
        Assert.assertNotNull(users);
    }

    @Test
    public void testProjections() {
        userRepository.save(User.builder().id(1L).name("jack12").email("123456@126.com").sex("man").address("shanghai").build());
        UserOnlyNameEmailDTO userOnlyNameEmailDto =  userRepository.findByEmail("123456@126.com");
        System.out.println(userOnlyNameEmailDto);
    }

    @Test
    public void testProjections2() {
        userRepository.save(User.builder().name("jack12").email("123456@126.com").sex("man").address("shanghai").build());
        UserOnlyName userOnlyName = userRepository.findByAddress("shanghai");
        System.out.println(userOnlyName);
    }

    @Test
    public void testQueryAnnotation() {
//新增一条数据方便测试      userDtoRepository.save(User.builder().name("jackxx").email("123456@126.com").sex("man").address("shanghai").build());
        //调用上面的方法查看结果
        User user2 = userRepository.findByQuery("jack");
        System.out.println(user2);
    }
}
