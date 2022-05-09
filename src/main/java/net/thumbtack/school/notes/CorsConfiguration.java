package net.thumbtack.school.notes;


import net.thumbtack.school.notes.util.Properties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfiguration implements WebMvcConfigurer {
    private final Properties properties;
    
    
    public CorsConfiguration(Properties properties) {
        this.properties = properties;
    }
    
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(properties.getAllowedOrigins().toArray(new String[]{}))
                .allowedMethods("POST", "PUT", "GET", "DELETE")
                .allowCredentials(true);
    }
}
