package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class TestUserDao extends TestDaoBase {
    static Stream<Arguments> correctUserFields() {
        return Stream.of(
                Arguments.of("ol1ver2000", "ol1versPa55word!", "Oliver", null, "Berg", UserType.USER),
                Arguments.of("p3tr0v", "Er34gd45bf!rw", "Иван", "Александрович", "Петров", UserType.SUPER)
        );
    }
    
    
    static Stream<User> incorrectUsers() {
        String login = "ol1ver2000";
        String password = "ol1versPa55word!";
        String firstName = "Oliver";
        String lastName = "Berg";
        
        return Stream.of(
                null,
                new User(),
                new User(null, password, firstName, null, lastName, UserType.USER),
                new User(login, null, firstName, null, lastName, UserType.SUPER),
                new User(login, password, null, null, lastName, UserType.USER),
                new User(login, password, firstName, null, null, UserType.SUPER),
                new User(login, password, firstName, null, lastName, null)
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("correctUserFields")
    void testInsert(
            String login, String password, String firstName, String patronymic, String lastName, UserType type)
            throws ServerException {
        insertUser(login, password, firstName, patronymic, lastName, type);
    }
    
    
    @ParameterizedTest
    @MethodSource("incorrectUsers")
    void testInsertIncorrect(User incorrectUser) {
        assertThrows(ServerException.class, () -> userDao.insert(incorrectUser));
    }
    
    
    @Test
    void testInsertAndLogin() throws ServerException {
        User petrov = new User("p3tr0v", "Er34gd45bf!rw", "Иван", "Александрович", "Петров", UserType.SUPER);
        
        userDao.insertAndLogin(petrov, "token");
        
        assertAll(
                () -> assertEquals(petrov, userDao.get(petrov.getId())),
                () -> assertEquals(petrov, sessionDao.getUser("token"))
        );
    }
    
    
    @Test
    void testUpdate1() throws ServerException {
        int id = insertUser("aNИa", ";lsafd3-Usd2", "Анна", "Петровна", "Птицына", UserType.USER).getId();
        
        userDao.update(new User(
                id,
                "aNИa",
                "Jlaefl41!e",
                "Анна",
                "Петровна",
                "Дроздова",
                UserType.USER
        ));
        User updatedUser = userDao.get(id);
        
        assertAll(
                () -> assertEquals(id, updatedUser.getId()),
                () -> assertEquals("aNИa", updatedUser.getLogin()),
                () -> assertEquals("Jlaefl41!e", updatedUser.getPassword()),
                () -> assertEquals("Анна", updatedUser.getFirstName()),
                () -> assertEquals("Петровна", updatedUser.getPatronymic()),
                () -> assertEquals("Дроздова", updatedUser.getLastName()),
                () -> assertSame(UserType.USER, updatedUser.getType())
        );
    }
    
    
    @Test
    void testUpdate2() throws ServerException {
        int id = insertUser("andy123", "K3rdv-223k", "Andy", null, "Johnson", UserType.USER).getId();
        
        userDao.update(new User(
                id,
                "andy321",
                "Bcafas-345fgh",
                "Andy",
                null,
                "Johnsson",
                UserType.SUPER
        ));
        User updatedUser = userDao.get(id);
        
        assertAll(
                () -> assertEquals(id, updatedUser.getId()),
                () -> assertEquals("andy123", updatedUser.getLogin(), "login must remain unchanged"),
                () -> assertEquals("Bcafas-345fgh", updatedUser.getPassword()),
                () -> assertEquals("Andy", updatedUser.getFirstName()),
                () -> assertNull(updatedUser.getPatronymic()),
                () -> assertEquals("Johnsson", updatedUser.getLastName()),
                () -> assertSame(UserType.SUPER, updatedUser.getType())
        );
    }
    
    
    @Test
    void testFollow() throws ServerException {
        User anna = insertUser("aNИa", ";lsafd3-Usd2", "Анна", "Петровна", "Птицына", UserType.USER);
        User andy = insertUser("andy123", "K3rdv-223k", "Andy", null, "Johnson", UserType.USER);
        User matteo = insertUser("matt30", "432ouN0F(", "Matteo", null, "Russo", UserType.USER);
        
        userDao.follow(anna, andy);
        userDao.follow(andy, anna);
        userDao.follow(matteo, andy);
        
        User annaDb = userDao.get(anna.getId());
        User andyDb = userDao.get(andy.getId());
        User matteoDb = userDao.get(matteo.getId());
        
        assertAll(
                () -> assertEquals(List.of(andyDb), annaDb.getFollowing()),
                () -> assertEquals(List.of(andyDb), annaDb.getFollowers()),
                () -> assertEquals(List.of(annaDb), andyDb.getFollowing()),
                () -> assertEquals(Set.of(annaDb, matteoDb), Set.copyOf(andyDb.getFollowers())),
                () -> assertEquals(List.of(andyDb), matteoDb.getFollowing()),
                () -> assertTrue(matteoDb.getFollowers().isEmpty())
        );
    }
    
    
    @Test
    void testIgnore() throws ServerException {
        User anna = insertUser("aNИa", ";lsafd3-Usd2", "Анна", "Петровна", "Птицына", UserType.USER);
        User andy = insertUser("andy123", "K3rdv-223k", "Andy", null, "Johnson", UserType.USER);
        User matteo = insertUser("matt30", "432ouN0F(", "Matteo", null, "Russo", UserType.USER);
        
        userDao.ignore(anna, andy);
        userDao.ignore(andy, anna);
        userDao.ignore(matteo, andy);
        
        User annaDb = userDao.get(anna.getId());
        User andyDb = userDao.get(andy.getId());
        User matteoDb = userDao.get(matteo.getId());
        
        assertAll(
                () -> assertEquals(List.of(andyDb), annaDb.getIgnore()),
                () -> assertEquals(List.of(andyDb), annaDb.getIgnoredBy()),
                () -> assertEquals(List.of(annaDb), andyDb.getIgnore()),
                () -> assertEquals(Set.of(annaDb, matteoDb), Set.copyOf(andyDb.getIgnoredBy())),
                () -> assertEquals(List.of(andyDb), matteoDb.getIgnore()),
                () -> assertTrue(matteoDb.getIgnoredBy().isEmpty())
        );
    }
    
    
    @Test
    void testUnfollow() throws ServerException {
        User anna = insertUser("aNИa", ";lsafd3-Usd2", "Анна", "Петровна", "Птицына", UserType.USER);
        User andy = insertUser("andy123", "K3rdv-223k", "Andy", null, "Johnson", UserType.USER);
        User matteo = insertUser("matt30", "432ouN0F(", "Matteo", null, "Russo", UserType.USER);
        
        userDao.follow(anna, andy);
        userDao.follow(andy, anna);
        userDao.follow(matteo, andy);
        userDao.unfollow(matteo, andy);
        
        User annaDb = userDao.get(anna.getId());
        User andyDb = userDao.get(andy.getId());
        User matteoDb = userDao.get(matteo.getId());
        
        assertAll(
                () -> assertEquals(List.of(andyDb), annaDb.getFollowing()),
                () -> assertEquals(List.of(andyDb), annaDb.getFollowers()),
                () -> assertEquals(List.of(annaDb), andyDb.getFollowing()),
                () -> assertEquals(List.of(annaDb), andyDb.getFollowers()),
                () -> assertTrue(matteoDb.getFollowing().isEmpty()),
                () -> assertTrue(matteoDb.getFollowers().isEmpty())
        );
    }
    
    
    @Test
    void testUnignore() throws ServerException {
        User anna = insertUser("aNИa", ";lsafd3-Usd2", "Анна", "Петровна", "Птицына", UserType.USER);
        User andy = insertUser("andy123", "K3rdv-223k", "Andy", null, "Johnson", UserType.USER);
        User matteo = insertUser("matt30", "432ouN0F(", "Matteo", null, "Russo", UserType.USER);
        
        userDao.ignore(anna, andy);
        userDao.ignore(andy, anna);
        userDao.ignore(matteo, andy);
        userDao.unignore(andy, anna);
        
        User annaDb = userDao.get(anna.getId());
        User andyDb = userDao.get(andy.getId());
        User matteoDb = userDao.get(matteo.getId());
        
        assertAll(
                () -> assertEquals(List.of(andyDb), annaDb.getIgnore()),
                () -> assertTrue(annaDb.getIgnoredBy().isEmpty()),
                () -> assertTrue(andyDb.getIgnore().isEmpty()),
                () -> assertEquals(Set.of(annaDb, matteoDb), Set.copyOf(andyDb.getIgnoredBy())),
                () -> assertEquals(List.of(andyDb), matteoDb.getIgnore()),
                () -> assertTrue(matteoDb.getIgnoredBy().isEmpty())
        );
    }
    
    
    @Test
    void testGetAll() throws ServerException {
        User matteo = insertUser("matt30", "432ouN0F(", "Matteo", null, "Russo", UserType.USER);
        User selenia = insertUser("selenia", "Jev3g2-0", "Selenia", null, "Valenti", UserType.SUPER);
        
        List<User> users = userDao.getAll();
        users.sort(Comparator.comparing(User::getLogin));
        
        assertEquals(List.of(admin, matteo, selenia), users);
    }
    
    
    @Test
    void testDelete() throws ServerException {
        User matteo = insertUser("matt30", "432ouN0F(", "Matteo", null, "Russo", UserType.USER);
        User selenia = insertUser("selenia", "Jev3g2-0", "Selenia", null, "Valenti", UserType.SUPER);
        
        userDao.delete(matteo);
        
        assertAll(
                () -> assertTrue(userDao.get(matteo.getId()).isDeleted()),
                () -> assertEquals(selenia, userDao.get(selenia.getId()))
        );
    }
}
