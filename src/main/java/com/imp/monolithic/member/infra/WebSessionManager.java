package com.imp.monolithic.member.infra;

import com.imp.monolithic.member.application.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class WebSessionManager implements SessionManager {

    @Override
    public void login(final long id) {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        final HttpSession session = request.getSession();
        session.setAttribute("id", id);
    }
}
