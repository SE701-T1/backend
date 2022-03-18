package com.team701.buddymatcher.interceptor;

import com.team701.buddymatcher.config.JwtTokenUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserInterceptor implements HandlerInterceptor {

    /**
     * Executed before actual handler is executed
     **/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        String token = request.getHeader("token");
        if (request.getRequestURI().contains("login")){
            return true;
        }
        JwtTokenUtil validator = new JwtTokenUtil();
        if(validator.validateToken(token)){
            String username = validator.getUsernameFromToken(token);
            System.out.println("get Username from token: " + username);
            addToUserDetails(request.getSession(), username);
            return true;
        }
        return false;
    }

    private void addToUserDetails(HttpSession session, String username) {
        session.setAttribute("Username", username);
    }
}
