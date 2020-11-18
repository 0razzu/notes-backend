package net.thumbtack.school.notes.database.daoimpl;


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
    void testInsert() {
        userDao.insert(james);
        
        sessionDao.insert(admin, "token1");
        sessionDao.insert(james, "token2");
        
        assertAll(
                () -> assertTrue(sessionDao.isOnline(admin)),
                () -> assertTrue(sessionDao.isOnline("token1")),
                () -> assertTrue(sessionDao.isOnline(james)),
                () -> assertTrue(sessionDao.isOnline("token2"))
        );
    }
    
    
    @Test
    void testInsertSeveralTimes() {
        userDao.insert(james);
        
        sessionDao.insert(james, "token1");
        sessionDao.insert(james, "token2");
        sessionDao.insert(james, "token3");
        
        assertAll(
                () -> assertTrue(sessionDao.isOnline(james)),
                () -> assertFalse(sessionDao.isOnline("token1")),
                () -> assertFalse(sessionDao.isOnline("token2")),
                () -> assertTrue(sessionDao.isOnline("token3"))
        );
    }
    
    
    @Test
    void testGetUserByToken() {
        userDao.insert(mia);
        
        sessionDao.insert(admin, "adminToken");
        sessionDao.insert(mia, "miaToken");
        
        assertAll(
                () -> assertEquals(admin, sessionDao.getUserByToken("adminToken")),
                () -> assertEquals(mia, sessionDao.getUserByToken("miaToken")),
                () -> assertNull(sessionDao.getUserByToken("nobodyToken"))
        );
    }
    
    
    @Test
    void testIsOnlineNonExisting() {
        User alexey = new User("lex123", "34wer43Lj46", "Алексей", "Сергеевич", "Алексеев", UserType.SUPER);
        
        assertAll(
                () -> assertFalse(sessionDao.isOnline(alexey)),
                () -> assertFalse(sessionDao.isOnline("neverExistedToken"))
        );
    }
    
    
    @Test
    void testGetOnline() {
        userDao.insert(james);
        userDao.insert(mia);
        
        sessionDao.insert(admin, "adminToken");
        sessionDao.insert(james, "jamesToken");
        sessionDao.insert(mia, "miaToken");
        
        List<User> online = sessionDao.getOnline();
        online.sort(Comparator.comparing(User::getLogin));
        
        assertEquals(List.of(admin, james, mia), online);
    }
    
    
    @Test
    void testGetOnlineEmpty() {
        assertEquals(Collections.emptyList(), sessionDao.getOnline());
    }
    
    
    @Test
    void testGetOnlineAfterLogout() {
        userDao.insert(james);
        userDao.insert(mia);
        
        sessionDao.insert(admin, "adminToken");
        sessionDao.insert(james, "jamesToken");
        sessionDao.insert(mia, "miaToken");
        
        sessionDao.delete("adminToken");
        sessionDao.delete(mia);
        
        assertEquals(List.of(james), sessionDao.getOnline());
    }
}