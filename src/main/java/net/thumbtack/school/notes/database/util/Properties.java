package net.thumbtack.school.notes.database.util;


import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
@PropertySource("classpath:application.properties")
public class Properties {
    private final Environment environment;
    
    
    public Properties(Environment environment) {
        this.environment = environment;
    }
    
    
    public int getUserIdleTimeout() {
        return environment.getProperty("user_idle_timeout", int.class);
    }
    
    
    public int getMaxNameLength() {
        return environment.getProperty("max_name_length", int.class);
    }
    
    
    public int getMinPasswordLength() {
        return environment.getProperty("min_password_length", int.class);
    }
}
