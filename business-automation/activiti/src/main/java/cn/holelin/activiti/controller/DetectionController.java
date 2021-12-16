package cn.holelin.activiti.controller;

import cn.holelin.activiti.service.DetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2021/11/22 4:16 下午
 * @UpdateUser: HoleLin
 * @UpdateDate: 2021/11/22 4:16 下午
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
@ResponseBody
@RequestMapping("/detection")
public class DetectionController {


    @Autowired
    private DetectionService detectionService;
    @PostMapping
    public void start() {

        detectionService.start();
    }

}
