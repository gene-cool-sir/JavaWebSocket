package com.bing.webchat.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bing.webchat.entity.CurrentUser;

/**
 * 登录拦截器
 * @author Administrator
 *
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private List<String> IGNORE_URI;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取URI后缀
        String requestUri = request.getServletPath();

        if(requestUri.equalsIgnoreCase("/"))    return true;

        //过滤不需要拦截的地址
        for (String uri : IGNORE_URI) {
            if (requestUri.startsWith(uri)) {
                return true;
            }
        }
        HttpSession session = request.getSession();
        CurrentUser user = (CurrentUser) session.getAttribute("currentUser");
        if(session != null && user != null){
            return true;
        }else{
        	String path=request.getServletContext().getContextPath() +"/user/login";
            response.sendRedirect(path);
            return false;
        }
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    public List<String> getIGNORE_URI() {
        return IGNORE_URI;
    }

    public void setIGNORE_URI(List<String> IGNORE_URI) {
        this.IGNORE_URI = IGNORE_URI;
    }
}
