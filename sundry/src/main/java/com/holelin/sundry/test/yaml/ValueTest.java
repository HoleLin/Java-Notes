package com.holelin.sundry.test.yaml;

import com.holelin.sundry.SundryApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/8/2 10:45
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/8/2 10:45
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SundryApplication.class)
public class ValueTest {

    @Value("${test.list.ids:1,2,3}")
    private List<String> list;

    @Value("${test.list.ids:1,2,3}")
    private String[] stringArray;

    @Value("#{'${test.list.topics}'.split(',')}")
    private List<String> topics;

    @Value("#{${test.maps}}")
    private Map<String, String> maps;


    @Value("${user.email}")
    private String userName;

    @Test
    public void testValueWithList() {
        log.info("list:{}", list);
    }

    @Test
    public void testValueWithArray() {
        log.info("stringArray:{}", stringArray.length);
    }


    @Test
    public void testValueWithSPEL() {
        log.info("topics:{}", topics);
    }

    @Test
    public void testValueWithMap() {
        log.info("maps:{}", maps);
    }

    @Test
    public void testCustomYml() {
        log.info("username:{}", userName);
    }
}
