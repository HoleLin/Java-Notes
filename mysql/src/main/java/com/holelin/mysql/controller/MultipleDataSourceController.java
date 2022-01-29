package com.holelin.mysql.controller;

import com.holelin.mysql.entity.MultipleDataSourcesEntity;
import com.holelin.mysql.service.MultipleDataSourceService;
import com.holelin.mysql.vo.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/7/30 10:21
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/7/30 10:21
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@RestController
@RequestMapping("/multiple-data-source")
public class MultipleDataSourceController {

    @Autowired
    private MultipleDataSourceService multipleDataSourceService;

    @GetMapping("/jpa")
    public List<MultipleDataSourcesEntity> testJpa() {
        return multipleDataSourceService.queryFromJpa();
    }
    @PostMapping("/jpa")
    public void addJpaData(@RequestBody Request request) {
        multipleDataSourceService.addJpa(request);
    }

    @GetMapping("/mybatis")
    public List<MultipleDataSourcesEntity> testMybatis() {
        return multipleDataSourceService.queryFromMyBatis();
    }

    @PostMapping("/mybatis")
    public List<MultipleDataSourcesEntity> addMybatis(@RequestBody Request request) {
        return multipleDataSourceService.queryFromMyBatis();
    }
    @GetMapping("/other")
    public List<MultipleDataSourcesEntity> other() {
        return multipleDataSourceService.queryFromMyBatisOtherSource();
    }
}
