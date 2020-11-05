package net.thumbtack.school.notes.database.mapper;


import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;


public interface SessionMapper {
    @Insert("INSERT INTO session (user_id, token) VALUES (#{user.id}, #{token})")
    Integer insert(User user, String token);
    
    
    @Select("SELECT (id, login, password, first_name, patronymic, last_name, type) " +
            "FROM session JOIN user ON user_id = user.id WHERE token = #{token}")
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
    User getByToken(String token);
    
    
    @Select("SELECT count(*) FROM session WHERE user_id = #{user.id}")
    boolean isOnlineByUser(User user);
    
    
    @Select("SELECT count(*) FROM session WHERE token = #{token}")
    boolean isOnlineByToken(String token);
    
    
    @Select("SELECT (id, login, password, first_name, patronymic, last_name, type) " +
            "FROM session JOIN user ON user_id = user.id")
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
    List<User> getOnline();
    
    
    @Delete("DELETE FROM session WHERE id = #{user.id}")
    void delete(User user);
    
    
    @Delete("DELETE FROM session WHERE token = #{token}")
    void delete(String token);
    
    
    @Delete("DELETE FROM session")
    void deleteAll();
}
