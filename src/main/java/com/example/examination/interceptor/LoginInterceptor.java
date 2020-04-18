package com.example.examination.interceptor;

import com.example.examination.util.Consts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
            throws Exception {
        String method = httpServletRequest.getMethod();
        String reqPath = httpServletRequest.getServletPath();
        log.info("access#{} {}", method, reqPath);
        if (HttpMethod.OPTIONS.name().equals(method)) {// 跨域的复杂请求会在请求前先发出一次OPTIONS请求
            return true;
        }

        // 只有返回true才会继续向下执行，返回false取消当前请求
        HttpSession session = httpServletRequest.getSession();
        log.info("sessionId#{}", session.getId());

        if (session == null || StringUtils.isEmpty(session.getAttribute(Consts.SEESION_UNAME))) {
            log.info("access#{}, not logged in, return", reqPath);
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {
//        log.debug("this is LoginInterceptor postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                                Exception e) throws Exception {
//        log.debug("this is LoginInterceptor afterCompletion");
    }
}
