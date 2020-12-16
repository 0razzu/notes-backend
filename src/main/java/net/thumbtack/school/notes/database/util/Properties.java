package net.thumbtack.school.notes.database.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Component
@PropertySource("classpath:application.properties")
public class Properties {
    public static final String JAVA_SESSION_ID = "JAVASESSIONID";
    @Value("${user_idle_timeout}")
    private int userIdleTimeout;
    @Value("${max_name_length}")
    private int maxNameLength;
    @Value("${min_password_length}")
    private int minPasswordLength;
    
    
    public int getUserIdleTimeout() {
        return userIdleTimeout;
    }
    
    
    public int getMaxNameLength() {
        return maxNameLength;
    }
    
    
    public int getMinPasswordLength() {
        return minPasswordLength;
    }
}
