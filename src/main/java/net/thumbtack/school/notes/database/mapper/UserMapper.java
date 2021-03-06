package net.thumbtack.school.notes.database.mapper;


import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.view.ShortUserView;
import net.thumbtack.school.notes.view.UserView;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;


public interface UserMapper {
    @Insert("INSERT INTO user (login, password, first_name, patronymic, last_name, type) " +
            "VALUES (#{login}, #{password}, #{firstName}, #{patronymic}, #{lastName}, #{type})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(User user);
    
    
    @Update("UPDATE user SET password = #{password}, " +
            "first_name = #{firstName}, patronymic = #{patronymic}, last_name = #{lastName}, " +
            "type = #{type}, deleted = #{deleted} " +
            "WHERE id = #{id}")
    void update(User user);
    
    
    @Insert("INSERT INTO user_followed(user_id, followed_id) VALUES(#{user.id}, #{followed.id})")
    void follow(@Param("user") User user, @Param("followed") User followed);
    
    
    @Insert("DELETE FROM user_followed WHERE user_id = #{user.id} AND followed_id = #{followed.id}")
    void unfollow(@Param("user") User user, @Param("followed") User followed);
    
    
    @Insert("INSERT INTO user_ignored(user_id, ignored_id) VALUES(#{user.id}, #{ignored.id})")
    void ignore(@Param("user") User user, @Param("ignored") User ignored);
    
    
    @Insert("DELETE FROM user_ignored WHERE user_id = #{user.id} AND ignored_id = #{ignored.id}")
    void unignore(@Param("user") User user, @Param("ignored") User ignored);
    
    
    @Select("SELECT id, login, password, first_name, patronymic, last_name, type, deleted " +
            "FROM user WHERE id = #{id}")
    @Results(id = "userFields", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "notes", column = "id", javaType = List.class,
                    many = @Many(
                            select = "net.thumbtack.school.notes.database.mapper.NoteMapper.getByAuthor",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(property = "comments", column = "id", javaType = List.class,
                    many = @Many(
                            select = "net.thumbtack.school.notes.database.mapper.CommentMapper.getByAuthor",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(property = "ratings", column = "id", javaType = List.class,
                    many = @Many(
                            select = "net.thumbtack.school.notes.database.mapper.RatingMapper.getByAuthor",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(property = "sections", column = "id", javaType = List.class,
                    many = @Many(
                            select = "net.thumbtack.school.notes.database.mapper.SectionMapper.getByCreator",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(property = "following", column = "id", javaType = List.class,
                    many = @Many(
                            select = "net.thumbtack.school.notes.database.mapper.UserMapper.getFollowing",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(property = "followers", column = "id", javaType = List.class,
                    many = @Many(
                            select = "net.thumbtack.school.notes.database.mapper.UserMapper.getFollowers",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(property = "ignore", column = "id", javaType = List.class,
                    many = @Many(
                            select = "net.thumbtack.school.notes.database.mapper.UserMapper.getIgnore",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(property = "ignoredBy", column = "id", javaType = List.class,
                    many = @Many(
                            select = "net.thumbtack.school.notes.database.mapper.UserMapper.getIgnoredBy",
                            fetchType = FetchType.LAZY
                    )
            )
    })
    User get(int id);
    
    
    @Select("SELECT id, login, password, first_name, patronymic, last_name, type, deleted " +
            "FROM user WHERE login = #{login}")
    @ResultMap("userFields")
    User getByLogin(String login);
    
    
    @Select({
            "<script>",
            "   SELECT id, login, first_name, patronymic, last_name, (type = 'SUPER') AS isSuper,",
            "       (followed_id is not NULL) as is_followed, (ignored_id is not NULL) as is_ignored",
            "   FROM user",
            "   LEFT JOIN (",
            "       SELECT followed_id from user_followed where user_followed.user_id = #{user.id}",
            "   ) as s1 ON s1.followed_id = id",
            "   LEFT JOIN (",
            "       SELECT ignored_id from user_ignored where user_ignored.user_id = #{user.id}",
            "   ) as s2 ON s2.ignored_id = id",
            "   <if test='requestedLogin == null'>",
            "       WHERE user.id = #{id}",
            "   </if>",
            "   <if test='id == null'>",
            "       WHERE user.login = #{requestedLogin}",
            "   </if>",
            "</script>"
    })
    ShortUserView getShort(
            @Param("user") User user,
            @Param("id") Integer id,
            @Param("requestedLogin") String requestedLogin
    );
    
    
    @Select("SELECT id, login, password, first_name, patronymic, last_name, type " +
            "FROM user JOIN user_followed ON user.id = user_followed.followed_id " +
            "WHERE user_followed.user_id = #{id}")
    @ResultMap("userFields")
    List<User> getFollowing(User user);
    
    
    @Select("SELECT id, login, password, first_name, patronymic, last_name, type " +
            "FROM user JOIN user_followed ON user.id = user_followed.user_id " +
            "WHERE user_followed.followed_id = #{id}")
    @ResultMap("userFields")
    List<User> getFollowers(User user);
    
    
    @Select("SELECT id, login, password, first_name, patronymic, last_name, type " +
            "FROM user JOIN user_ignored ON user.id = user_ignored.ignored_id " +
            "WHERE user_ignored.user_id = #{id}")
    @ResultMap("userFields")
    List<User> getIgnore(User user);
    
    
    @Select("SELECT id, login, password, first_name, patronymic, last_name, type " +
            "FROM user JOIN user_ignored ON user.id = user_ignored.user_id " +
            "WHERE user_ignored.ignored_id = #{id}")
    @ResultMap("userFields")
    List<User> getIgnoredBy(User user);
    
    
    @Select("SELECT id, login, password, first_name, patronymic, last_name, type, deleted FROM user")
    @ResultMap("userFields")
    List<User> getAll();
    
    
    @Select({
            "<script>",
            "   SELECT user.id, first_name, patronymic, last_name, login, time_registered,",
            "   (date_add(session.was_active, INTERVAL #{userIdleTimeout} SECOND) > now()) AS isOnline,",
            "   deleted AS isDeleted,",
            "   <if test='selectSuper'>",
            "       (type = 'SUPER') AS isSuper,",
            "   </if>",
            "   avg(rating.value) AS rating",
            "   FROM user",
            "   LEFT JOIN note ON user.id = note.author_id",
            "   LEFT JOIN rating ON note.id = rating.note_id",
            "   LEFT JOIN session ON user.id = session.user_id",
            "   GROUP BY user.id",
            "   <if test='sortByRating == \"asc\"'>",
            "       ORDER BY rating ASC",
            "   </if>",
            "   <if test='sortByRating == \"desc\"'>",
            "       ORDER BY rating DESC",
            "   </if>",
            "   <if test='count == null and from != null'>",
            "       LIMIT 2147483647",
            "       OFFSET #{from}",
            "   </if>",
            "   <if test='count != null'>",
            "       LIMIT #{count}",
            "       <if test='from != null'>",
            "           OFFSET #{from}",
            "       </if>",
            "   </if>",
            "</script>"
    })
    List<UserView> getAllWithRating(
            @Param("sortByRating") String sortByRating,
            @Param("selectSuper") boolean selectSuper,
            @Param("from") Integer from,
            @Param("count") Integer count,
            @Param("userIdleTimeout") int userIdleTimeout
    );
    
    
    @Select({
            "<script>",
            "   WITH t AS (",
            "       SELECT user.id, first_name, patronymic, last_name, login, time_registered,",
            "       (date_add(session.was_active, INTERVAL #{userIdleTimeout} SECOND) > now()) AS isOnline,",
            "       deleted AS isDeleted,",
            "       <if test='selectSuper'>",
            "           (type = 'SUPER') AS isSuper,",
            "       </if>",
            "       avg(rating.value) AS rating",
            "       FROM user",
            "       LEFT JOIN note ON user.id = note.author_id",
            "       LEFT JOIN rating ON note.id = rating.note_id",
            "       LEFT JOIN session ON user.id = session.user_id",
            "       GROUP BY user.id",
            "   ) SELECT * FROM t WHERE rating =",
            "       <choose>",
            "           <when test='ratingType == \"highRating\"'>",
            "               (SELECT max(rating) FROM t)",
            "           </when>",
            "           <when test='ratingType == \"lowRating\"'>",
            "               (SELECT min(rating) FROM t)",
            "           </when>",
            "       </choose>",
            "   <if test='count == null and from != null'>",
            "       LIMIT 2147483647",
            "       OFFSET #{from}",
            "   </if>",
            "   <if test='count != null'>",
            "       LIMIT #{count}",
            "       <if test='from != null'>",
            "           OFFSET #{from}",
            "       </if>",
            "   </if>",
            "</script>"
    })
    List<UserView> getAllByRatingType(
            @Param("ratingType") String ratingType,
            @Param("selectSuper") boolean selectSuper,
            @Param("from") Integer from,
            @Param("count") Integer count,
            @Param("userIdleTimeout") int userIdleTimeout
    );
    
    
    @Select({
            "<script>",
            "   SELECT user.id, first_name, patronymic, last_name, login, time_registered,",
            "   (date_add(session.was_active, INTERVAL #{userIdleTimeout} SECOND) > now()) AS isOnline,",
            "   deleted AS isDeleted,",
            "   <if test='selectSuper'>",
            "       (type = 'SUPER') AS isSuper,",
            "   </if>",
            "   avg(rating.value) AS rating",
            "   FROM user",
            "   LEFT JOIN note ON user.id = note.author_id",
            "   LEFT JOIN rating ON note.id = rating.note_id",
            "   LEFT JOIN session ON user.id = session.user_id",
            "   <if test='selectSuper and userType == \"super\"'>",
            "       WHERE type = 'super'",
            "   </if>",
            "   <if test='userType == \"deleted\"'>",
            "       WHERE deleted = 1",
            "   </if>",
            "   GROUP BY user.id",
            "   <if test='sortByRating == \"asc\"'>",
            "       ORDER BY rating ASC",
            "   </if>",
            "   <if test='sortByRating == \"desc\"'>",
            "       ORDER BY rating DESC",
            "   </if>",
            "   <if test='count == null and from != null'>",
            "       LIMIT 2147483647",
            "       OFFSET #{from}",
            "   </if>",
            "   <if test='count != null'>",
            "       LIMIT #{count}",
            "       <if test='from != null'>",
            "           OFFSET #{from}",
            "       </if>",
            "   </if>",
            "</script>"
    })
    List<UserView> getAllByType(
            @Param("userType") String userType,
            @Param("sortByRating") String sortByRating,
            @Param("selectSuper") boolean selectSuper,
            @Param("from") Integer from,
            @Param("count") Integer count,
            @Param("userIdleTimeout") int userIdleTimeout
    );
    
    
    @Select({
            "<script>",
            "   SELECT user.id, first_name, patronymic, last_name, login, time_registered,",
            "   (date_add(session.was_active, INTERVAL #{userIdleTimeout} SECOND) > now()) AS isOnline,",
            "   deleted AS isDeleted,",
            "   <if test='selectSuper'>",
            "       (type = 'SUPER') AS isSuper,",
            "   </if>",
            "   avg(rating.value) AS rating",
            "   <choose>",
            "       <when test='relation == \"following\"'>",
            "           FROM user_followed",
            "           JOIN user ON user.id = user_followed.followed_id",
            "       </when>",
            "       <when test='relation == \"followers\"'>",
            "           FROM user_followed",
            "           JOIN user ON user.id = user_followed.user_id",
            "       </when>",
            "       <when test='relation == \"ignore\"'>",
            "           FROM user_ignored",
            "           JOIN user ON user.id = user_ignored.ignored_id",
            "       </when>",
            "       <when test='relation == \"ignoredBy\"'>",
            "           FROM user_ignored",
            "           JOIN user ON user.id = user_ignored.user_id",
            "       </when>",
            "   </choose>",
            "   LEFT JOIN note ON user.id = note.author_id",
            "   LEFT JOIN rating ON note.id = rating.note_id",
            "   LEFT JOIN session ON user.id = session.user_id",
            "   <choose>",
            "       <when test='relation == \"following\"'>",
            "           WHERE user_followed.user_id = #{user.id}",
            "       </when>",
            "       <when test='relation == \"followers\"'>",
            "           WHERE user_followed.followed_id = #{user.id}",
            "       </when>",
            "       <when test='relation == \"ignore\"'>",
            "           WHERE user_ignored.user_id = #{user.id}",
            "       </when>",
            "       <when test='relation == \"ignoredBy\"'>",
            "           WHERE user_ignored.ignored_id = #{user.id}",
            "       </when>",
            "   </choose>",
            "   GROUP BY user.id",
            "   <if test='sortByRating == \"asc\"'>",
            "       ORDER BY rating ASC",
            "   </if>",
            "   <if test='sortByRating == \"desc\"'>",
            "       ORDER BY rating DESC",
            "   </if>",
            "   <if test='count == null and from != null'>",
            "       LIMIT 2147483647",
            "       OFFSET #{from}",
            "   </if>",
            "   <if test='count != null'>",
            "       LIMIT #{count}",
            "       <if test='from != null'>",
            "           OFFSET #{from}",
            "       </if>",
            "   </if>",
            "</script>"
    })
    List<UserView> getAllByRelationToUser(
            @Param("user") User user,
            @Param("relation") String relation,
            @Param("sortByRating") String sortByRating,
            @Param("selectSuper") boolean selectSuper,
            @Param("from") Integer from,
            @Param("count") Integer count,
            @Param("userIdleTimeout") int userIdleTimeout
    );
}
