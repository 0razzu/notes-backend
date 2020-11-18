package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class TestUserDaoImpl extends TestDaoImplBase {
    private static final UserDao userDao = new UserDaoImpl();
    
    
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
            String login, String password, String firstName, String patronymic, String lastName, UserType type) {
        insertUser(login, password, firstName, patronymic, lastName, type);
    }
    
    
    @ParameterizedTest
    @MethodSource("incorrectUsers")
    void testInsertIncorrect(User incorrectUser) {
        assertThrows(RuntimeException.class, () -> userDao.insert(incorrectUser));
    }
    
    
    @Test
    void testUpdate1() {
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
    void testUpdate2() {
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
    void testGetAll() {
        User matteo = insertUser("matt30", "432ouN0F(", "Matteo", null, "Russo", UserType.USER);
        User selenia = insertUser("selenia", "Jev3g2-0", "Selenia", null, "Valenti", UserType.SUPER);
        
        List<User> users = userDao.getAll();
        users.sort(Comparator.comparing(User::getLogin));
        
        assertEquals(List.of(admin, matteo, selenia), users);
    }
    
    
    @Test
    void testDelete() {
        User matteo = insertUser("matt30", "432ouN0F(", "Matteo", null, "Russo", UserType.USER);
        User selenia = insertUser("selenia", "Jev3g2-0", "Selenia", null, "Valenti", UserType.SUPER);
    
        userDao.delete(matteo);
        
        assertAll(
                () -> assertNull(userDao.get(matteo.getId())),
                () -> assertEquals(selenia, userDao.get(selenia.getId()))
        );
    }
}
