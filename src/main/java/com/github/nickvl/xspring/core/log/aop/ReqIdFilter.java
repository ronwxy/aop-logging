package com.github.nickvl.xspring.core.log.aop;

import org.slf4j.MDC;

import javax.servlet.*;
import java.io.IOException;

public class ReqIdFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        MDC.put("reqId", ObjectId.get().toHexString());
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }

    }

    @Override
    public void destroy() {

    }
}
