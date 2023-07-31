package cn.holelin.springdemo.bean.controller;

import cn.holelin.springdemo.bean.bean.User;
import cn.holelin.springdemo.bean.service.DataService;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/8/30 16:05
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/8/30 16:05
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
@RequestMapping("/data-service")
public class DataServiceController implements Closeable {

    @Qualifier("dataServiceController.InnerDataService")
    @Autowired
    DataService innerDataService;

    List<User> users;

    public DataServiceController(List<User> users) {
        this.users = users;
    }

    @PostMapping("/users")
    public void getUserInfo(@RequestParam("test") String test) {
        System.out.println(test);
    }

    @Override
    public void close() throws IOException {
        System.out.println("closed");
    }


    @Service
    public static class InnerDataService implements DataService {

        @Override
        public String getDataSourceName() {
            return "InnerDataService";
        }
    }

}
