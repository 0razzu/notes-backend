package net.thumbtack.school.notes.database.mapper;


import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;


public interface UserMapper {
    @Insert("INSERT INTO user (login, password, first_name, patronymic, last_name, role)" +
            "VALUES (#{login}, #{password}, #{firstName}, #{patronymic}, #{lastName}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(User user);
    
    
    @Update("UPDATE user SET password = #{password}, " +
            "first_name = #{firstName}, patronymic = #{patronymic}, last_name = #{lastName}, role = #{role} " +
            "WHERE id = #{id}")
    void update(User user);
    
    
    @Select("SELECT (id, login, password, first_name AS firstName, patronymic, last_name AS lastName, role) " +
            "FROM user WHERE id = #{id}")
    @Results({
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
    
    
    @Select("SELECT (id, login, password, first_name AS firstName, patronymic, last_name AS lastName, role) " +
            "FROM user")
    @Results({
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
    List<User> getAll();
    
    
    @Delete("DELETE FROM user WHERE id = #{user.id}")
    void delete(User user);
    
    
    @Delete("DELETE FROM user")
    void deleteAll();
}
