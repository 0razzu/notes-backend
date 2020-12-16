package net.thumbtack.school.notes.service;


import net.thumbtack.school.notes.database.util.Properties;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static net.thumbtack.school.notes.database.util.Properties.JAVA_SESSION_ID;


@Service
public class BaseService {
    protected final Properties properties;
    
    
    protected BaseService(Properties properties) {
        this.properties = properties;
    }
    
    
    protected void setTokenCookie(HttpServletResponse response, String token, int maxAge) {
        Cookie cookie = new Cookie(JAVA_SESSION_ID, token);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
}
