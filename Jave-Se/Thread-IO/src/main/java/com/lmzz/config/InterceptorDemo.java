package com.lmzz.config;

import com.lmzz.threadlocal.userDemo.User;
import com.lmzz.threadlocal.util.MyThreadLocal;
import com.lmzz.threadlocal.util.ThreadLocalUtil2;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InterceptorDemo extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("start................");
        MyThreadLocal.put(new User("zs", 12));

        //ThreadLocalUtil2
        ThreadLocalUtil2.put("USER", new User("ls", 18));
        ThreadLocalUtil2.put("TOKEN", "1234");
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("end................");
        MyThreadLocal.remove();

        //ThreadLocalUtil2
        ThreadLocalUtil2.remove("USER");
        ThreadLocalUtil2.clear();
        super.afterCompletion(request, response, handler, ex);
    }
}
