package com.radeckiy.chat.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
public class HttpHandshakeInterceptor implements HandshakeInterceptor {
    private Environment env;
    private static final Logger logger = LoggerFactory.getLogger(HttpHandshakeInterceptor.class);

    public HttpHandshakeInterceptor(Environment env) {
        this.env = env;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession();

            String username = session.getAttribute("username") != null ? (String) session.getAttribute("username") : servletRequest.getServletRequest().getHeader("username");

            if(username != null) {
                String[] allowedUsers = env.getProperty("allowed_users", String[].class);
                username = username.trim();

                if (username.isEmpty() || allowedUsers == null)
                    return false;

                for (String allowedUser : allowedUsers) {
                    if(allowedUser.equals(username)) {
                        attributes.put("sessionId", session.getId());
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        HttpSession session = servletRequest.getServletRequest().getSession();

        logger.info("Put session " + session.getId());
    }
}