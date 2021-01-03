package net.thumbtack.school.notes.database.mapper;


import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.Rating;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;


public interface RatingMapper {
    @Insert("INSERT INTO rating (value, note_id, author_id) VALUES (#{rating.value}, #{note.id}, #{rating.author.id}) " +
            "ON DUPLICATE KEY UPDATE value = #{rating.value}")
    @Options(useGeneratedKeys = true, keyProperty = "rating.id")
    Integer insert(@Param("rating") Rating rating, @Param("note") Note note);
}
