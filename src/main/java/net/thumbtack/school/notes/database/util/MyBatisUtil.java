package net.thumbtack.school.notes.database.util;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;


public class MyBatisUtil {
    private static SqlSessionFactory sqlSessionFactory;
    private static final Logger LOGGER = LoggerFactory.getLogger(MyBatisUtil.class);
    
    
    public static boolean initSqlSessionFactory() {
        try (Reader reader = Resources.getResourceAsReader("mybatis.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            
            return true;
        } catch (Exception e) {
            LOGGER.error("Error loading mybatis.xml", e);
            
            return false;
        }
    }
    
    
    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
