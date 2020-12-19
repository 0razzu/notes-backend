package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TestSessionDaoImpl extends TestDaoImplBase {
    private static final User james = new User("blackDragon", "1234-Asepgk", "James", null, "Black", UserType.USER);
    private static final User mia = new User("mia", "M1AmiaMIA", "Mia", null, "Evans", UserType.SUPER);
    
    
    @Test
    void testInsert() throws ServerException {
        userDao.insert(james);
        
        sessionDao.insert(admin, "token1");
        sessionDao.insert(james, "token2");
        
        assertAll(
                () -> assertNotNull(sessionDao.getUserByToken("token1")),
                () -> assertNotNull(sessionDao.getUserByToken("token2"))
        );
    }
    
    
    @Test
    void testInsertSeveralTimes() throws ServerException {
        userDao.insert(james);
        
        sessionDao.insert(james, "token1");
        sessionDao.insert(james, "token2");
        sessionDao.insert(james, "token3");
        
        assertAll(
                () -> assertNull(sessionDao.getUserByToken("token1")),
                () -> assertNull(sessionDao.getUserByToken("token2")),
                () -> assertNotNull(sessionDao.getUserByToken("token3"))
        );
    }
    
    
    @Test
    void testGetUserByToken() throws ServerException {
        userDao.insert(mia);
        
        sessionDao.insert(admin, "adminToken");
        sessionDao.insert(mia, "miaToken");
        
        assertAll(
                () -> assertEquals(admin, sessionDao.getUserByToken("adminToken")),
                () -> assertEquals(mia, sessionDao.getUserByToken("miaToken")),
                () -> assertNull(sessionDao.getUserByToken("nobodyToken"))
        );
    }
}