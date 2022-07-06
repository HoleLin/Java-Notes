package com.holelin.sundry.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/7/23 15:09
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/7/23 15:09
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Component
public class TestInterceptor implements HandlerInterceptor {
    /**
     * 这个方法将在请求处理之前进行调用。
     * 「注意」：如果该方法的返回值为false ，将视为当前请求结束，不仅自身的拦截器会失效，还会导致其他的拦截器也不再执行。
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * 只有在 preHandle() 方法返回值为true 时才会执行。会在Controller 中的方法调用之后，DispatcherServlet 返回渲染视图之前被调用。
     * 「有意思的是」：postHandle() 方法被调用的顺序跟 preHandle() 是相反的，
     * 先声明的拦截器  preHandle() 方法先执行，而postHandle()方法反而会后执行。
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler the handler (or {@link HandlerMethod}) that started asynchronous
     * execution, for type and/or instance examination
     * @param modelAndView the {@code ModelAndView} that the handler returned
     * (can also be {@code null})
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 只有在 preHandle() 方法返回值为true 时才会执行。在整个请求结束之后，
     * DispatcherServlet 渲染了对应的视图之后执行。
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler the handler (or {@link HandlerMethod}) that started asynchronous
     * execution, for type and/or instance examination
     * @param ex any exception thrown on handler execution, if any; this does not
     * include exceptions that have been handled through an exception resolver
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
