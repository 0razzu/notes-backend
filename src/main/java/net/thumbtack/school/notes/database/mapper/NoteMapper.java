package net.thumbtack.school.notes.database.mapper;


import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.Section;
import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;


public interface NoteMapper {
    @Insert("INSERT INTO note (subject, author_id, section_id) VALUES (#{subject}, #{author.id}, #{section.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(Note note);
}
