package com.holelin.sundry.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/7/23 15:03
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/7/23 15:03
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
@Component
public class TestFilter implements Filter {
    /**
     * 在容器中创建当前过滤器的时候自动调用
     * 它在 Filter 的整个生命周期只会被调用一次。
     * 「注意」：这个方法必须执行成功，否则过滤器会不起作用。
     *
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.info("Filter 前置");
    }

    /**
     * 过滤的具体操作
     * 容器中的每一次请求都会调用该方法， FilterChain 用来调用下一个过滤器 Filter。
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        log.info("Filter 处理中");
        filterChain.doFilter(servletRequest, servletResponse);

    }

    /**
     * 在容器中销毁当前过滤器的时候自动调用
     * 在过滤器 Filter 的整个生命周期也只会被调用一次
     */
    @Override
    public void destroy() {
        Filter.super.destroy();
        log.info("Filter 后置");

    }
}
