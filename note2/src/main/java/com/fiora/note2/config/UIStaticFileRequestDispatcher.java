//package com.fiora.note2.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.ResourceLoader;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//@Order(Ordered.HIGHEST_PRECEDENCE)
//@WebFilter(filterName = "UIStaticFileRequestDispatcher", urlPatterns = "/ui/*")
//public class UIStaticFileRequestDispatcher implements Filter {
//    @Autowired
//    ResourceLoader resourceLoader;
//    /**
//     * Spring Boot serves static content from these 4 directories, namely /META-INF/resources,/resources,/static,/public.
//     * Currently, we use '/static' to serve static content. If we need any other directory to contain static content, add another element.
//     */
//    private static final String[] STATIC_RESOURCE_PATHS = {"classpath:static"};
//    private static final String ANGULAR_RESOURCE_ROOT = "/ui/index.html";
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        final HttpServletRequest req = (HttpServletRequest) request;
//        final String path = req.getServletPath();
//        if (fileExist(path)) {
//            chain.doFilter(request, response);
//        } else {
//            request.getRequestDispatcher(ANGULAR_RESOURCE_ROOT).forward(request, response);
//        }
//
//    }
//
//    /**
//     * Identify if the file path specified exists within {@link #STATIC_RESOURCE_PATHS}.
//     * @param requestURL namely the URL of request, part of the full file path.
//     * For example,
//     * '/ui/runtime.js' is the servlet path of the request.
//     * 'classpath:static/ui/runtime.js' is its full path of the file.
//     * @return {@literal true} if the path exists as static resource, else return {@literal false}.
//     */
//    public boolean fileExist(String requestURL) {
//        for (String staticPath : STATIC_RESOURCE_PATHS) {
//            String searchFile = staticPath + requestURL;
//            Resource res = resourceLoader.getResource(searchFile);
//            if (res.exists()) {
//                return true;
//            }
//        }
//        return false;
//    }
//}
