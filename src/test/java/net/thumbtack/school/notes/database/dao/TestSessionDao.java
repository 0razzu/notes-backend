package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class TestSessionDao extends TestDaoBase {
    private static final User james = new User("blackDragon", "1234-Asepgk", "James", null, "Black", UserType.USER);
    private static final User mia = new User("mia", "M1AmiaMIA", "Mia", null, "Evans", UserType.SUPER);
    
    
    @Test
    void testInsert() throws ServerException {
        userDao.insert(james);
        
        sessionDao.insert(admin, "token1");
        sessionDao.insert(james, "token2");
        
        assertAll(
                () -> assertNotNull(sessionDao.getUser("token1")),
                () -> assertNotNull(sessionDao.getUser("token2"))
        );
    }
    
    
    @Test
    void testInsertSeveralTimes() throws ServerException {
        userDao.insert(james);
        
        sessionDao.insert(james, "token1");
        sessionDao.insert(james, "token2");
        sessionDao.insert(james, "token3");
        
        assertAll(
                () -> assertNull(sessionDao.getUser("token1")),
                () -> assertNull(sessionDao.getUser("token2")),
                () -> assertNotNull(sessionDao.getUser("token3"))
        );
    }
    
    
    @Test
    void testGetUserByToken() throws ServerException {
        userDao.insert(mia);
        
        sessionDao.insert(admin, "adminToken");
        sessionDao.insert(mia, "miaToken");
        
        assertAll(
                () -> assertEquals(admin, sessionDao.getUser("adminToken")),
                () -> assertEquals(mia, sessionDao.getUser("miaToken")),
                () -> assertNull(sessionDao.getUser("nobodyToken"))
        );
    }
    
    
    @Test
    void testDelete() throws ServerException {
        userDao.insert(james);
        
        sessionDao.insert(james, "token");
        sessionDao.delete("token");
        
        assertNull(sessionDao.getUser("token"));
    }
}