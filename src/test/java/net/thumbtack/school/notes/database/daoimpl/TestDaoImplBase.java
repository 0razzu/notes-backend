package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.database.dao.CommonDao;
import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.database.util.MyBatisUtil;
import net.thumbtack.school.notes.database.util.Properties;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class TestDaoImplBase {
    private static boolean setUp = false;
    private static final CommonDao commonDao = new CommonDaoImpl();
    private static final Properties properties = new Properties();
    protected static final SessionDao sessionDao = new SessionDaoImpl(properties);
    protected static final UserDao userDao = new UserDaoImpl(properties);
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
        commonDao.clear();
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
