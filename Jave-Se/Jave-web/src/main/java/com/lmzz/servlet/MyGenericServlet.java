package com.lmzz.servlet;

import javax.servlet.*;
import java.io.IOException;

public class MyGenericServlet extends GenericServlet {
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        servletResponse.getWriter().print("Hello World GenericServlet!");
    }
}
