package net.thumbtack.school.notes.database.mapper;


import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.annotations.*;


public interface SessionMapper {
    @Insert("INSERT INTO session (user_id, token) VALUES (#{user.id}, #{token}) " +
            "ON DUPLICATE KEY UPDATE token = #{token}, was_active = now()")
    Integer insert(@Param("user") User user, @Param("token") String token);
    
    
    @Update("UPDATE session SET was_active = now() WHERE token = #{token}")
    void update(@Param("token") String token);
    
    
    @Select("SELECT id, login, password, first_name, patronymic, last_name, type " +
            "FROM session JOIN user ON user_id = user.id WHERE token = #{token} " +
            "AND (date_add(session.was_active, INTERVAL #{userIdleTimeout} SECOND) > now())")
    @ResultMap("net.thumbtack.school.notes.database.mapper.UserMapper.userFields")
    User getUserByToken(@Param("token") String token, @Param("userIdleTimeout") int userIdleTimeout);
    
    
    @Delete("DELETE FROM session WHERE user_id = #{id}")
    void deleteByUser(User user);
    
    
    @Delete("DELETE FROM session WHERE token = #{token}")
    void deleteByToken(String token);
    
    
    @Delete("DELETE FROM session WHERE date_add(session.was_active, INTERVAL #{userIdleTimeout} SECOND) < now()")
    void deleteOutdated(int userIdleTimeout);
    
    
    @Delete("DELETE FROM session")
    void deleteAll();
}
