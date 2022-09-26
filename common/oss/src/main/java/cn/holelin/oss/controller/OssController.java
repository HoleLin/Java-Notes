package cn.holelin.oss.controller;

import cn.holelin.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("oss")
public class OssController {

    @Autowired
    private OssService ossService;

    @GetMapping("/download")
    public void download(@RequestParam("objectName") String objectName,
                         @RequestParam(value = "type", required = false) Integer code, HttpServletResponse response) {

        ossService.download(objectName, code, response);
    }
}
