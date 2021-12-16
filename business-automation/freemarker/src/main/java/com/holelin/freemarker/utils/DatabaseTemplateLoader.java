package com.holelin.freemarker.utils;

import freemarker.cache.TemplateLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
/**
 * @Description: 数据库模板加载器
 * @Author: HoleLin
 * @CreateDate: 2021/10/28 10:42 上午
 * @UpdateUser: HoleLin
 * @UpdateDate: 2021/10/28 10:42 上午
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Component
public class DatabaseTemplateLoader implements TemplateLoader {


    @Override
    public Object findTemplateSource(String s) throws IOException {
        return null;
    }

    @Override
    public long getLastModified(Object o) {
        return 0;
    }

    @Override
    public Reader getReader(Object o, String s) throws IOException {
        return null;
    }

    @Override
    public void closeTemplateSource(Object o) throws IOException {

    }
}
