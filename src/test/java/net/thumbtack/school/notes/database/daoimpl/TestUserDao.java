package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.*;
import net.thumbtack.school.notes.view.UserView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.Math.abs;
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
    void testGetByLogin() throws ServerException {
        User matteo = insertUser("matt30", "432ouN0F(", "Matteo", null, "Russo", UserType.USER);
        
        assertEquals(matteo, userDao.getByLogin("matt30"));
    }
    
    
    private boolean compareUserViews(UserView view1, UserView view2) {
        return view1.getId() == view2.getId() &&
                view1.getFirstName().equals(view2.getFirstName()) &&
                Objects.equals(view1.getPatronymic(), view2.getPatronymic()) &&
                view1.getLastName().equals(view2.getLastName()) &&
                view1.getLogin().equals(view2.getLogin()) &&
                view2.getTimeRegistered() != null &&
                view1.isOnline() == view2.isOnline() &&
                view1.isDeleted() == view2.isDeleted() &&
                Objects.equals(view1.getIsSuper(), view2.getIsSuper()) &&
                abs(view1.getRating() - view2.getRating()) < 1E-6;
    }
    
    
    private boolean compareUserViewLists(List<UserView> list1, List<UserView> list2, boolean sort) {
        if (sort) {
            list1.sort(Comparator.comparing(UserView::getId));
            list2.sort(Comparator.comparing(UserView::getId));
        }
        
        if (list1.size() != list2.size())
            return false;
        
        for (int i = 0; i < list1.size(); i++)
            if (!compareUserViews(list1.get(i), list2.get(i)))
                return false;
        
        return true;
    }
    
    
    @Test
    void testGetAllWithRating() throws ServerException {
        User matteo = insertUser("matt30", "432ouN0F(", "Matteo", null, "Russo", UserType.USER);
        User selenia = insertUser("selenia", "Jev3g2-0", "Selenia", null, "Valenti", UserType.SUPER);
        Section section = new Section("s", admin);
        sectionDao.insert(section);
        
        Note note1 = new Note(matteo, LocalDateTime.of(2021, 1, 1, 0, 0), section);
        Note note2 = new Note(selenia, LocalDateTime.of(2021, 1, 1, 1, 0), section);
        NoteRevision revision1 = new NoteRevision("subj1", "body1", note1.getCreated(), note1);
        NoteRevision revision2 = new NoteRevision("subj2", "body2", note2.getCreated(), note2);
        noteDao.insert(note1, revision1);
        noteDao.insert(note2, revision2);
        
        ratingDao.insert(new Rating(5, admin), note1);
        ratingDao.insert(new Rating(4, matteo), note2);
        
        UserView adminView = new UserView(
                admin.getId(),
                admin.getFirstName(), admin.getPatronymic(), admin.getLastName(),
                admin.getLogin(), null,
                false, false, null, 0
        );
        UserView matteoView = new UserView(
                matteo.getId(),
                matteo.getFirstName(), matteo.getPatronymic(), matteo.getLastName(),
                matteo.getLogin(), null,
                false, false, null, 5
        );
        UserView seleniaView = new UserView(
                selenia.getId(),
                selenia.getFirstName(), selenia.getPatronymic(), selenia.getLastName(),
                selenia.getLogin(), null,
                false, false, null, 4
        );
        
        List<UserView> withSuper = userDao.getAllWithRating(null, true, null, null);
        List<UserView> withoutSuperAsc = userDao.getAllWithRating("asc", false, null, null);
        List<UserView> withSuperDesc = userDao.getAllWithRating("desc", true, null, null);
        
        assertTrue(compareUserViewLists(List.of(adminView, seleniaView, matteoView), withoutSuperAsc, false));
        
        adminView.setIsSuper(true);
        matteoView.setIsSuper(false);
        seleniaView.setIsSuper(true);
        
        assertAll(
                () -> assertTrue(compareUserViewLists(Arrays.asList(adminView, matteoView, seleniaView), withSuper, true)),
                () -> assertTrue(compareUserViewLists(List.of(matteoView, seleniaView, adminView), withSuperDesc, false)),
                () -> assertEquals(2, userDao.getAllWithRating(null, false, 1, null).size()),
                () -> assertEquals(2, userDao.getAllWithRating(null, false, null, 2).size()),
                () -> assertEquals(1, userDao.getAllWithRating(null, false, 2, 2).size())
        );
    }
    
    
    @Test
    void testGetAllByRatingType() throws ServerException {
        User matteo = insertUser("matt30", "432ouN0F(", "Matteo", null, "Russo", UserType.USER);
        User selenia = insertUser("selenia", "Jev3g2-0", "Selenia", null, "Valenti", UserType.SUPER);
        insertUser("eddie", "32dfw3hF", "Eddie", null, "Black", UserType.USER);
        Section section = new Section("s", admin);
        sectionDao.insert(section);
        
        Note note1 = new Note(matteo, LocalDateTime.of(2021, 1, 1, 0, 0), section);
        Note note2 = new Note(selenia, LocalDateTime.of(2021, 1, 1, 1, 0), section);
        Note note3 = new Note(admin, LocalDateTime.of(2021, 1, 1, 1, 1), section);
        NoteRevision revision1 = new NoteRevision("subj1", "body1", note1.getCreated(), note1);
        NoteRevision revision2 = new NoteRevision("subj2", "body2", note2.getCreated(), note2);
        NoteRevision revision3 = new NoteRevision("subj3", "body3", note3.getCreated(), note3);
        noteDao.insert(note1, revision1);
        noteDao.insert(note2, revision2);
        noteDao.insert(note3, revision3);
        
        ratingDao.insert(new Rating(5, admin), note1);
        ratingDao.insert(new Rating(4, matteo), note2);
        ratingDao.insert(new Rating(5, matteo), note3);
        
        UserView adminView = new UserView(
                admin.getId(),
                admin.getFirstName(), admin.getPatronymic(), admin.getLastName(),
                admin.getLogin(), null,
                false, false, null, 5
        );
        UserView matteoView = new UserView(
                matteo.getId(),
                matteo.getFirstName(), matteo.getPatronymic(), matteo.getLastName(),
                matteo.getLogin(), null,
                false, false, null, 5
        );
        UserView seleniaView = new UserView(
                selenia.getId(),
                selenia.getFirstName(), selenia.getPatronymic(), selenia.getLastName(),
                selenia.getLogin(), null,
                false, false, null, 4
        );
        
        List<UserView> highRating = userDao.getAllByRatingType("highRating", false, null, null);
        List<UserView> lowRating = userDao.getAllByRatingType("lowRating", false, null, null);
        List<UserView> lowRatingWithSuper = userDao.getAllByRatingType("lowRating", true, null, null);
        
        assertAll(
                () -> assertTrue(compareUserViewLists(Arrays.asList(adminView, matteoView), highRating, true)),
                () -> assertTrue(compareUserViewLists(List.of(seleniaView), lowRating, false)),
                () -> assertEquals(1, userDao.getAllByRatingType("highRating", false, 1, null).size()),
                () -> assertEquals(1, userDao.getAllByRatingType("highRating", false, null, 1).size()),
                () -> assertEquals(1, userDao.getAllByRatingType("highRating", false, 1, 1).size())
        );
        
        seleniaView.setIsSuper(true);
        
        assertTrue(compareUserViewLists(List.of(seleniaView), lowRatingWithSuper, false));
    }
    
    
    @Test
    void testGetAllByType() throws ServerException {
        User matteo = insertUser("matt30", "432ouN0F(", "Matteo", null, "Russo", UserType.USER);
        User selenia = insertUser("selenia", "Jev3g2-0", "Selenia", null, "Valenti", UserType.SUPER);
        Section section = new Section("s", admin);
        sectionDao.insert(section);
        
        Note note1 = new Note(matteo, LocalDateTime.of(2021, 1, 1, 0, 0), section);
        Note note2 = new Note(selenia, LocalDateTime.of(2021, 1, 1, 1, 0), section);
        NoteRevision revision1 = new NoteRevision("subj1", "body1", note1.getCreated(), note1);
        NoteRevision revision2 = new NoteRevision("subj2", "body2", note2.getCreated(), note2);
        noteDao.insert(note1, revision1);
        noteDao.insert(note2, revision2);
        
        ratingDao.insert(new Rating(5, admin), note1);
        ratingDao.insert(new Rating(4, matteo), note2);
        
        userDao.delete(selenia);
        
        UserView adminView = new UserView(
                admin.getId(),
                admin.getFirstName(), admin.getPatronymic(), admin.getLastName(),
                admin.getLogin(), null,
                false, false, null, 0
        );
        UserView matteoView = new UserView(
                matteo.getId(),
                matteo.getFirstName(), matteo.getPatronymic(), matteo.getLastName(),
                matteo.getLogin(), null,
                false, false, null, 5
        );
        UserView seleniaView = new UserView(
                selenia.getId(),
                selenia.getFirstName(), selenia.getPatronymic(), selenia.getLastName(),
                selenia.getLogin(), null,
                false, true, null, 4
        );
        
        List<UserView> deleted = userDao.getAllByType("deleted", null, false, null, null);
        List<UserView> deletedWithSuperAsc = userDao.getAllByType("deleted", "asc", true, null, null);
        List<UserView> superWithoutSuper = userDao.getAllByType("super", null, false, null, null);
        List<UserView> superWithSuper = userDao.getAllByType("super", null, true, null, null);
        List<UserView> superWithSuperDesc = userDao.getAllByType("super", "desc", true, null, null);
        
        assertAll(
                () -> assertTrue(compareUserViewLists(List.of(seleniaView), deleted, false)),
                () -> assertTrue(compareUserViewLists(Arrays.asList(adminView, matteoView, seleniaView),
                        superWithoutSuper, true))
        );
        
        adminView.setIsSuper(true);
        matteoView.setIsSuper(false);
        seleniaView.setIsSuper(true);
        
        assertAll(
                () -> assertTrue(compareUserViewLists(List.of(seleniaView), deletedWithSuperAsc, false)),
                () -> assertTrue(compareUserViewLists(Arrays.asList(adminView, seleniaView), superWithSuper, true)),
                () -> assertTrue(compareUserViewLists(List.of(seleniaView, adminView), superWithSuperDesc, false)),
                () -> assertEquals(1, userDao.getAllByType("super", null, true, 1, null).size()),
                () -> assertEquals(1, userDao.getAllByType("super", null, true, null, 1).size()),
                () -> assertEquals(1, userDao.getAllByType("super", null, true, 1, 1).size())
        );
    }
    
    
    @Test
    void testGetAllByRelationToUser() throws ServerException {
        User matteo = insertUser("matt30", "432ouN0F(", "Matteo", null, "Russo", UserType.USER);
        User selenia = insertUser("selenia", "Jev3g2-0", "Selenia", null, "Valenti", UserType.SUPER);
        Section section = new Section("s", admin);
        sectionDao.insert(section);
        
        Note note1 = new Note(matteo, LocalDateTime.of(2021, 1, 1, 0, 0), section);
        Note note2 = new Note(selenia, LocalDateTime.of(2021, 1, 1, 1, 0), section);
        NoteRevision revision1 = new NoteRevision("subj1", "body1", note1.getCreated(), note1);
        NoteRevision revision2 = new NoteRevision("subj2", "body2", note2.getCreated(), note2);
        noteDao.insert(note1, revision1);
        noteDao.insert(note2, revision2);
        
        ratingDao.insert(new Rating(5, admin), note1);
        ratingDao.insert(new Rating(4, matteo), note2);
        
        userDao.follow(matteo, admin);
        userDao.follow(matteo, selenia);
        userDao.ignore(selenia, matteo);
        
        UserView adminView = new UserView(
                admin.getId(),
                admin.getFirstName(), admin.getPatronymic(), admin.getLastName(),
                admin.getLogin(), null,
                false, false, null, 0
        );
        UserView matteoView = new UserView(
                matteo.getId(),
                matteo.getFirstName(), matteo.getPatronymic(), matteo.getLastName(),
                matteo.getLogin(), null,
                false, false, null, 5
        );
        UserView seleniaView = new UserView(
                selenia.getId(),
                selenia.getFirstName(), selenia.getPatronymic(), selenia.getLastName(),
                selenia.getLogin(), null,
                false, false, null, 4
        );
        
        List<UserView> matteoFollowingDesc = userDao.getAllByRelationToUser(matteo, "following", "desc", false, null, null);
        List<UserView> matteoFollowers = userDao.getAllByRelationToUser(matteo, "followers", null, false, null, null);
        List<UserView> matteoIgnoredBy = userDao.getAllByRelationToUser(matteo, "ignoredBy", null, false, null, null);
        List<UserView> seleniaFollowers = userDao.getAllByRelationToUser(selenia, "followers", null, false, null, null);
        List<UserView> seleniaIgnoreSuper = userDao.getAllByRelationToUser(selenia, "ignore", null, true, null, null);
        
        assertAll(
                () -> assertTrue(compareUserViewLists(List.of(seleniaView, adminView), matteoFollowingDesc, false)),
                () -> assertTrue(matteoFollowers.isEmpty()),
                () -> assertTrue(compareUserViewLists(List.of(seleniaView), matteoIgnoredBy, false)),
                () -> assertTrue(compareUserViewLists(List.of(matteoView), seleniaFollowers, false))
        );
        
        matteoView.setIsSuper(false);
        
        assertAll(
                () -> assertTrue(compareUserViewLists(List.of(matteoView), seleniaIgnoreSuper, false)),
                () -> assertEquals(1, userDao.getAllByRelationToUser(matteo, "following", null, true, 1, null).size()),
                () -> assertEquals(1, userDao.getAllByRelationToUser(matteo, "following", null, true, null, 1).size()),
                () -> assertEquals(1, userDao.getAllByRelationToUser(matteo, "following", null, true, 1, 1).size())
        );
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
