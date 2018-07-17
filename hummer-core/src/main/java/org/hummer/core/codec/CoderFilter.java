package org.hummer.core.codec;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CoderFilter extends OncePerRequestFilter {
    private static final String CODEC_MODE = "X-xSimple-EM";

    public CoderFilter() {
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String method = request.getMethod();
        if (RequestMethod.POST.toString().equals(method)) {
            String mode = request.getHeader("X-xSimple-EM");
            if (StringUtils.isNotEmpty(mode)) {
                if (!(request instanceof RequestWrapper)) {
                    request = new RequestWrapper(request, mode);
                }

                if (!(response instanceof ResponseWrapper)) {
                    response = new ResponseWrapper(response, mode);
                    response.setHeader("X-xSimple-EM", mode);
                }
            }
        }

        filterChain.doFilter((ServletRequest) request, (ServletResponse) response);
    }
}
