package com.holelin.sundry.demo;

import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/7/29 15:24
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/7/29 15:24
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

public class GetResourceTest {
    public static void main(String[] args) throws IOException {
        // path不以'/'开头,默认是从此类所在的包下面取资源
        System.out.println(GetResourceTest.class.getResource(""));
        // path以'/'开头,则是从classpath根下获取
        System.out.println(GetResourceTest.class.getResourceAsStream("/"));
        // path是从classpath根获取的
        System.out.println(GetResourceTest.class.getClassLoader().getResource(""));
        // path不能以'/'开头
        System.out.println(GetResourceTest.class.getClassLoader().getResource("/"));
        // 在Spring框架下
        File file = org.springframework.util.ResourceUtils.getFile("classpath:application.yml");
        File dir = org.springframework.util.ResourceUtils.getFile("classpath:template");
        // File dir2 = org.springframework.util.ResourceUtils.getFile("classpath:template");
        ClassPathResource classPathResource = new ClassPathResource("template/");
        // ClassPathResource classPathResource2 = new ClassPathResource("template");
        System.out.println("isFile:" + classPathResource.getFile().isFile());
        System.out.println("isDirectory:" + classPathResource.getFile().isDirectory());
        try (Stream<Path> paths = Files.walk(classPathResource.getFile().toPath())) {
            paths.forEach(System.out::println);
        }
    }
}
