package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.debug.DebugDao;
import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.database.util.MyBatisUtil;
import net.thumbtack.school.notes.database.util.Properties;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestDaoImplBase {
    private static boolean setUp = false;
    @Autowired
    private DebugDao debugDao;
    @Autowired
    private Properties properties;
    @Autowired
    protected SessionDao sessionDao;
    @Autowired
    protected UserDao userDao;
    protected static final User admin = new User("admin", "adminPa55word", "admin", null, "admin", UserType.SUPER);
    
    
    @BeforeAll
    static void initSession() {
        if (!setUp) {
            MyBatisUtil.initSqlSessionFactory();
            setUp = true;
        }
    }
    
    
    @BeforeEach
    void clear() throws ServerException {
        debugDao.clear();
        userDao.insert(admin);
    }
    
    
    protected User insertUser(
            String login, String password, String firstName, String patronymic, String lastName, UserType type)
            throws ServerException {
        User user = new User(login, password, firstName, patronymic, lastName, type);
        
        userDao.insert(user);
        assertNotEquals(0, user.getId());
        
        return user;
    }
}
