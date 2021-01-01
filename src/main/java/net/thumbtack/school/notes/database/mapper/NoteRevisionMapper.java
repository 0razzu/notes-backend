package net.thumbtack.school.notes.database.mapper;


import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


public interface NoteRevisionMapper {
    @Insert("INSERT INTO note_revision (body, note_id) VALUES (#{body}, #{note.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(NoteRevision revision);
}
