package net.thumbtack.school.notes.database.dao_impl;


import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;


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
}
