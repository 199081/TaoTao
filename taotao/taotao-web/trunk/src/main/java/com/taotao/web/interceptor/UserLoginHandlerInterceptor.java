package com.taotao.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.util.CookieUtils;
import com.taotao.web.bean.User;
import com.taotao.web.service.UserService;
import com.taotao.web.threadlocal.UserThreadLocal;

public class UserLoginHandlerInterceptor implements HandlerInterceptor{
    
    public static final String COOKIE_NAME = "TT_TOKEN";
    
    @Autowired
    private UserService userService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        UserThreadLocal.set(null);
        String loginUrl = this.userService.TAOTAO_SSO_URL + "/user/login.html";
        String token = CookieUtils.getCookieValue(request, COOKIE_NAME);
        if(StringUtils.isEmpty(token)){
            //未登录
            response.sendRedirect(loginUrl);
            return false;
        }
        //通过SSO系统查询用户是否处于登录状态
        User user = this.userService.queryUserByToken(token);
        if(null == user){
            // 登录超时
            response.sendRedirect(loginUrl);
            return false;
        }
        // 登录成功
        UserThreadLocal.set(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        
    }

}
