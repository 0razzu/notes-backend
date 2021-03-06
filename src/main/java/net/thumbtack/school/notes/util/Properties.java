package net.thumbtack.school.notes.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@PropertySource("classpath:application.yml")
public class Properties {
    public static final String JAVA_SESSION_ID = "JAVASESSIONID";
    @Value("${user_idle_timeout}")
    private int userIdleTimeout;
    @Value("${max_name_length}")
    private int maxNameLength;
    @Value("${min_password_length}")
    private int minPasswordLength;
    @Value("${allowed_origins}")
    private List<String> allowedOrigins;
    
    
    public int getUserIdleTimeout() {
        return userIdleTimeout;
    }
    
    
    public int getMaxNameLength() {
        return maxNameLength;
    }
    
    
    public int getMinPasswordLength() {
        return minPasswordLength;
    }
    
    
    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }
}
