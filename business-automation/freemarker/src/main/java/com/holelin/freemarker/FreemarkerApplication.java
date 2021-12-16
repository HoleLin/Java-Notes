package com.holelin.freemarker;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.holelin.freemarker.bean.TempBean;
import com.holelin.freemarker.domain.Attribute;
import com.holelin.freemarker.domain.TemplateClass;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

@Slf4j
@SpringBootApplication
public class FreemarkerApplication implements ApplicationRunner {

    @Autowired
    private Configuration configuration;

    public static void main(String[] args) {
        SpringApplication.run(FreemarkerApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("/templates");
        FileOutputStream fos = new FileOutputStream("business-automation/freemarker/src/main/java/com/holelin/freemarker/bean/TempBean.java");

        configuration.setDirectoryForTemplateLoading(classPathResource.getFile());
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        Template template = configuration.getTemplate("Bean.ftl");
        TemplateClass templateClass = new TemplateClass();
        templateClass.setClassName("TempBean");
        templateClass.setClassPath("com.holelin.freemarker.bean");
        List<Attribute> attributes = Lists.newArrayList();
        Attribute name = Attribute.builder().name("name").type("String").build();
        Attribute age = Attribute.builder().name("age").type("Integer").build();
        attributes.add(name);
        attributes.add(age);
        templateClass.setAttributes(attributes);
        Writer out = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"),10240);
        template.process(templateClass,out);
        String json="{\"name\":\"测试\",\"age\":\"21\"}";
        TempBean o = (TempBean) JSONUtil.toBean(json, Class.forName("com.holelin.freemarker.bean.TempBean"));
        log.info("object: {}",o);
//        out.flush();
//        String result = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateClass);
    }
}
