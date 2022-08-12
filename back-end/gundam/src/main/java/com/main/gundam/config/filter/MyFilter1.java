package com.main.gundam.config.filter;

import javax.servlet.*;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class MyFilter1 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("MyFilter1");

        chain.doFilter(request, response);
    }
}
