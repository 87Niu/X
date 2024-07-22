package com.ruijie.filter;


import com.alibaba.fastjson2.JSON;
import com.ruijie.common.BaseContext;
import com.ruijie.common.R;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;


@Slf4j
@WebFilter(filterName = "loginChechFilter", urlPatterns = "/*")
@Component
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String reuqestURL = request.getRequestURI();

        String[] urls = new String[] {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };

        boolean check = check(urls, reuqestURL);
        if (check) {
            filterChain.doFilter(request, response);
            return ;
        }

        // 已经登录
        if (request.getSession().getAttribute("employee") != null) {

            Long empID = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empID);
            filterChain.doFilter(request, response);
            return ;
        }

        if (request.getSession().getAttribute("user") != null) {
            Long userID = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userID);

            filterChain.doFilter(request, response);

            return ;
        }
        log.info("未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

    }
    public boolean check(String[] urls, String requersURL) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requersURL);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
