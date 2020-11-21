package net.thumbtack.school.notes.database.mapper;


import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
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
            "first_name = #{firstName}, patronymic = #{patronymic}, last_name = #{lastName}, type = #{type} " +
            "WHERE id = #{id}")
    void update(User user);
    
    
    @Select("SELECT id, login, password, first_name, patronymic, last_name, type " +
            "FROM user WHERE id = #{id}")
    @Results(id = "userFields", value = {
            @Result(property = "id", column = "id"),
//            @Result(property = "notes", column = "id", javaType = List.class,
//                    many = @Many(
//                            select = "net.thumbtack.school.notes.database.mapper.NoteMapper.getByAuthor",
//                            fetchType = FetchType.LAZY
//                    )
//            ),
            @Result(property = "comments", column = "id", javaType = List.class,
                    many = @Many(
                            select = "net.thumbtack.school.notes.database.mapper.CommentMapper.getByAuthor",
                            fetchType = FetchType.LAZY
                    )
            )//,
//            @Result(property = "ratings", column = "id", javaType = List.class,
//                    many = @Many(
//                            select = "net.thumbtack.school.notes.database.mapper.RatingMapper.getByAuthor",
//                            fetchType = FetchType.LAZY
//                    )
//            ),
//            @Result(property = "sections", column = "id", javaType = List.class,
//                    many = @Many(
//                            select = "net.thumbtack.school.notes.database.mapper.SectionMapper.getByCreator",
//                            fetchType = FetchType.LAZY
//                    )
//            )
    })
    User get(int id);
    
    
    @Select("SELECT id, login, password, first_name, patronymic, last_name, type FROM user")
//    @ResultMap("userFields")
    List<User> getAll();
    
    
    @Select({
            "<script>",
            "   SELECT user.id AS id, first_name, patronymic, last_name, login, time_registered,",
            "   (session.token IS NOT NULL) AS isOnline,",
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
            @Param("count") Integer count
    );
    
    
    @Select({
            "<script>",
            "   SELECT user.id AS id, first_name, patronymic, last_name, login, time_registered,",
            "   (session.token IS NOT NULL) AS isOnline,",
            "   deleted AS isDeleted,",
            "   <if test='selectSuper'>",
            "       (type = 'SUPER') AS isSuper,",
            "   </if>",
            "   avg(rating.value) AS rating",
            "   FROM user",
            "   LEFT JOIN note ON user.id = note.author_id",
            "   LEFT JOIN rating ON note.id = rating.note_id",
            "   LEFT JOIN session ON user.id = session.user_id",
            "   <if test='selectSuper and userType.name() == \"SUPER\" or userType.name() != \"SUPER\"'>",
            "       WHERE type = #{userType}",
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
            @Param("userType") UserType userType,
            @Param("sortByRating") String sortByRating,
            @Param("selectSuper") boolean selectSuper,
            @Param("from") Integer from,
            @Param("count") Integer count
    );
    
    
    @Select({
            "<script>",
            "   SELECT user.id AS id, first_name, patronymic, last_name, login, time_registered,",
            "   (session.token IS NOT NULL) AS isOnline,",
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
            "   FROM user",
            "   LEFT JOIN note ON user.id = note.author_id",
            "   LEFT JOIN rating ON note.id = rating.note_id",
            "   LEFT JOIN session ON user.id = session.user_id",
            "   <choose>",
            "       <when test='relation == \"following\"'>",
            "           WHERE user_followed.user_id = #{user.id}",
            "       </when>",
            "       <when test='relation == \"followers\"'>",
            "           WHERE user_followed.followed_id = #{user.id}\",",
            "       </when>",
            "       <when test='relation == \"ignore\"'>",
            "           WHERE user_ignored.user_id = #{user.id}\",",
            "       </when>",
            "       <when test='relation == \"ignoredBy\"'>",
            "           WHERE user_ignored.ignored_id = #{user.id}\",",
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
            @Param("count") Integer count
    );
    
    
    @Delete("DELETE FROM user WHERE id = #{id}")
    void delete(User user);
    
    
    @Delete("DELETE FROM user")
    void deleteAll();
}
