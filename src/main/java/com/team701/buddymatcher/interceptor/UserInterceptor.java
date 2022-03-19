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
        try {
            String url = request.getRequestURI();
            String token = request.getHeader("Authorization");
            if (url.contains("swagger")
                    || url.contains("api-docs")
                    || url.contains("webjars")
                    || url.contains("login")
                    || url.contains("register")
            ) {
                return true;
            }

            if (token == null || token == "") {
                return false;
            }

            token = token.replace("Bearer ","");

            JwtTokenUtil validator = new JwtTokenUtil();
            if(validator.validateToken(token)){
                String UserId = validator.getIdFromToken(token);
                addToUserDetails(request.getSession(), UserId);
                return true;
            }
        return false;
        }catch(IllegalArgumentException e) {
            return false;
        }
    }

    // Add UserId to request session so that the controller doesn't have to decode the JWT to get it
    // To get the ID from the session, call request.getSession().getAttribute("UserId");
    private void addToUserDetails(HttpSession session, String UserId) {
        session.setAttribute("UserId", Long.parseLong(UserId));
    }
}
