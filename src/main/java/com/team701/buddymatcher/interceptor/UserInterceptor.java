package com.team701.buddymatcher.interceptor;

import com.team701.buddymatcher.config.JwtTokenUtil;
import io.jsonwebtoken.MalformedJwtException;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserInterceptor implements HandlerInterceptor {

    /**
     * Executed before actual handler is executed
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object object) {
        try {
            if (isValidURL(request.getRequestURI()))
                return true;

            String token = isValidToken(request.getHeader("Authorization"));
            if (token == null)
                return false;
            addToUserDetails(request.getSession(), token);
            return true;
        } catch (IllegalArgumentException | MalformedJwtException e) {
            return false;
        }
    }

    /**
     * Add UserId to request session so that the controller doesn't have to decode the JWT to get it
     * To get the ID from the session, call request.getSession().getAttribute("UserId")
     */
    private void addToUserDetails(HttpSession session, String UserId) {
        session.setAttribute("UserId", Long.parseLong(UserId));
    }

    public boolean isValidURL(@NotNull String url) {
        return (url.contains("swagger") ||
                url.contains("api-docs") ||
                url.contains("webjars") ||
                url.contains("login") ||
                url.contains("register") ||
                url.contains("verify"));
    }

    public String isValidToken(String token) {
        try {
            if (token == null || token.equals(""))
                return null;
            token = token.replace("Bearer ", "");
            JwtTokenUtil validator = new JwtTokenUtil();
            if (validator.validateToken(token))
                return validator.getIdFromToken(token);
            return null;
        } catch (MalformedJwtException e) {
            return null;
        }
    }
}
