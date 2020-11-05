package net.thumbtack.school.notes.database.mapper;


import net.thumbtack.school.notes.model.Type;
import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;


public interface UserMapper {
    @Insert("INSERT INTO user (login, password, first_name, patronymic, last_name, type)" +
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
            @Result(property = "notes", column = "id", javaType = List.class,
                    many = @Many(
                            select = "net.thumbtack.school.notes.database.mappers.NoteMapper.getByAuthor",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(property = "comments", column = "id", javaType = List.class,
                    many = @Many(
                            select = "net.thumbtack.school.notes.database.mappers.CommentMapper.getByAuthor",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(property = "ratings", column = "id", javaType = List.class,
                    many = @Many(
                            select = "net.thumbtack.school.notes.database.mappers.RatingMapper.getByAuthor",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(property = "sections", column = "id", javaType = List.class,
                    many = @Many(
                            select = "net.thumbtack.school.notes.database.mappers.SectionMapper.getByCreator",
                            fetchType = FetchType.LAZY
                    )
            )
    })
    User get(int id);
    
    
    @Select("SELECT id, login, password, first_name, patronymic, last_name, type FROM user")
    @ResultMap("userFields")
    List<User> getAll();
    
    
    @Select({
            "SELECT user.id AS id, login, password, first_name, patronymic, last_name, type,",
            "avg(rating.value) AS rating,",
            "(session.token IS NOT NULL) AS online",
            "FROM user",
            "LEFT JOIN note ON user.id = note.author_id",
            "LEFT JOIN rating ON note.id = rating.note_id",
            "LEFT JOIN session ON user.id = session.user_id",
            "GROUP BY user.id"
    })
    @ResultMap("userFields")
    List<User> getAllWithRating();
    
    
    @SelectProvider(method = "getAllByType", type = net.thumbtack.school.notes.database.provider.UserProvider.class)
    @ResultMap("userMap")
    List<User> getAllByType(
            @Param("sortByRating") String sortByRating,
            @Param("type") Type type,
            @Param("from") Integer from,
            @Param("count") Integer count
    );
    
    
    @SelectProvider(method = "getAllByRelationToUser", type = net.thumbtack.school.notes.database.provider.UserProvider.class)
    @ResultMap("userMap")
    List<User> getAllByRelationToUser(
            @Param("user") User user,
            @Param("relation") String relation,
            @Param("sortByRating") String sortByRating,
            @Param("from") Integer from,
            @Param("count") Integer count
    );
    
    
    @Delete("DELETE FROM user WHERE id = #{user.id}")
    void delete(User user);
    
    
    @Delete("DELETE FROM user")
    void deleteAll();
}
