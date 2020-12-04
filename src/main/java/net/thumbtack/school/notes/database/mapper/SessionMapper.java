package net.thumbtack.school.notes.database.mapper;


import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;


public interface SessionMapper {
    @Insert("INSERT INTO session (user_id, token) VALUES (#{user.id}, #{token}) " +
            "ON DUPLICATE KEY UPDATE token = #{token}")
    Integer insert(@Param("user") User user, @Param("token") String token);
    
    
    @Select("SELECT id, login, password, first_name, patronymic, last_name, type " +
            "FROM session JOIN user ON user_id = user.id WHERE token = #{token}")
    @ResultMap("net.thumbtack.school.notes.database.mapper.UserMapper.userFields")
    User getUserByToken(String token);
    
    
    @Select("SELECT count(*) FROM session WHERE user_id = #{id}")
    boolean isOnlineByUser(User user);
    
    
    @Select("SELECT count(*) FROM session WHERE token = #{token}")
    boolean isOnlineByToken(String token);
    
    
    @Select("SELECT id, login, password, first_name, patronymic, last_name, type " +
            "FROM session JOIN user ON user_id = user.id")
    @ResultMap("net.thumbtack.school.notes.database.mapper.UserMapper.userFields")
    List<User> getOnline();
    
    
    @Delete("DELETE FROM session WHERE user_id = #{id}")
    void deleteByUser(User user);
    
    
    @Delete("DELETE FROM session WHERE token = #{token}")
    void deleteByToken(String token);
    
    
    @Delete("DELETE FROM session")
    void deleteAll();
}
