package com.example.examination.bean;

import com.example.examination.util.XssHttpServletRequestWrapper;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class XssFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(request);

        filterChain.doFilter(xssRequest, response);
    }

    @Override
    public void destroy() {

    }

}
