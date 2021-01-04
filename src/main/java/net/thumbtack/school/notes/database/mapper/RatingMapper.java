package net.thumbtack.school.notes.database.mapper;


import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.Rating;
import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;


public interface RatingMapper {
    @Insert("INSERT INTO rating (value, note_id, author_id) VALUES (#{rating.value}, #{note.id}, #{rating.author.id}) " +
            "ON DUPLICATE KEY UPDATE value = #{rating.value}")
    @Options(useGeneratedKeys = true, keyProperty = "rating.id")
    Integer insert(@Param("rating") Rating rating, @Param("note") Note note);
    
    
    @Select("SELECT id, value, author_id FROM rating")
    @Results(id = "ratingFields", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "author", column = "author_id", javaType = User.class,
                    one = @One(
                            select = "net.thumbtack.school.notes.database.mapper.UserMapper.get",
                            fetchType = FetchType.LAZY
                    )
            )
    })
    Rating get(int id);
}
