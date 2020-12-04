package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
import net.thumbtack.school.notes.view.UserView;

import java.util.List;


public interface UserDao {
    void insert(User user);
    void update(User user);
    
    void follow(User user, User followed);
    void unfollow(User user, User followed);
    void ignore(User user, User ignored);
    void unignore(User user, User ignored);
    
    User get(int id);
    List<User> getAll();
    List<UserView> getAllWithRating(String sortByRating, boolean selectSuper, Integer from, Integer count);
    List<UserView> getAllByType(UserType userType, String sortByRating, boolean selectSuper, Integer from, Integer count);
    List<UserView> getAllByRelationToUser(User user, String relation,
                                          String sortByRating, boolean selectSuper, Integer from, Integer count);
    
    void delete(User user);
    void deleteAll();
}
