package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.view.ShortUserView;
import net.thumbtack.school.notes.view.UserView;

import java.util.List;


public interface UserDao {
    void insert(User user) throws ServerException;
    void insertAndLogin(User user, String token) throws ServerException;
    void update(User user) throws ServerException;
    
    void follow(User user, User followed) throws ServerException;
    void unfollow(User user, User followed) throws ServerException;
    void ignore(User user, User ignored) throws ServerException;
    void unignore(User user, User ignored) throws ServerException;
    
    User get(int id) throws ServerException;
    User getByLogin(String login) throws ServerException;
    ShortUserView getShort(User user, String requestedLogin) throws ServerException;
    List<UserView> getAllWithRating(String sortByRating, boolean selectSuper, Integer from, Integer count)
            throws ServerException;
    List<UserView> getAllByRatingType(String ratingType, boolean selectSuper, Integer from, Integer count)
            throws ServerException;
    List<UserView> getAllByType(String userType, String sortByRating, boolean selectSuper, Integer from, Integer count)
            throws ServerException;
    List<UserView> getAllByRelationToUser(User user, String relation, String sortByRating, boolean selectSuper,
                                          Integer from, Integer count) throws ServerException;
    
    void delete(User user) throws ServerException;
}
