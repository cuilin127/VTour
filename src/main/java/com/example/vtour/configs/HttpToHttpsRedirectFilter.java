package com.example.vtour.configs;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


public class HttpToHttpsRedirectFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (!request.isSecure()) {
            String httpsUrl = "https://" + request.getServerName() + ":" + request.getServerPort()
                    + request.getRequestURI();
            response.sendRedirect(httpsUrl);
        } else {
            chain.doFilter(request, response);
        }
    }
}
