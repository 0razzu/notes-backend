package net.thumbtack.school.notes;


import net.thumbtack.school.notes.database.util.MyBatisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class MainApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);
    
    
    public static void main(String[] args) {
        if (MyBatisUtil.initSqlSessionFactory())
            SpringApplication.run(MainApp.class);
        
        else
            LOGGER.error("Failed to initialize SQL session factory");
    }
}
