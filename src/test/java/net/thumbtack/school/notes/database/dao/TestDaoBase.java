package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.database.util.MyBatisUtil;
import net.thumbtack.school.notes.util.Properties;
import net.thumbtack.school.notes.debug.DebugDao;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;


@SpringBootTest
public class TestDaoBase {
    private static boolean setUp = false;
    @Autowired
    private Properties properties;
    @Autowired
    protected CommentDao commentDao;
    @Autowired
    private DebugDao debugDao;
    @Autowired
    protected NoteDao noteDao;
    @Autowired
    protected NoteRevisionDao noteRevisionDao;
    @Autowired
    protected RatingDao ratingDao;
    @Autowired
    protected SectionDao sectionDao;
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
