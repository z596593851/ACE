package com.hxm.trade.user.server;

import com.hxm.trade.common.constants.TradeEnums;
import org.eclipse.jetty.security.jaspi.ServletCallbackHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class UserRestServer {
    public static void main(String[] args) throws Exception{
        Server server=new Server(TradeEnums.RestServerEnum.USER.getServerPort());
        ServletContextHandler springMvcHandler=new ServletContextHandler();
        springMvcHandler.setContextPath("/"+TradeEnums.RestServerEnum.USER.getContextPath());
        XmlWebApplicationContext context=new XmlWebApplicationContext();
        context.setConfigLocation("classpath:springmvc.xml");
        springMvcHandler.addEventListener(new ContextLoaderListener(context));
        springMvcHandler.addServlet(new ServletHolder(new DispatcherServlet(context)),"/*");
        server.setHandler(springMvcHandler);
        server.start();
        server.join();
    }
}
