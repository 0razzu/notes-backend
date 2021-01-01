package net.thumbtack.school.notes.database.mapper;


import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;

import java.time.LocalDateTime;


public interface NoteRevisionMapper {
    @Insert("INSERT INTO note_revision (body, note_id) VALUES (#{revision.body}, #{note.id})")
    @Options(useGeneratedKeys = true, keyProperty = "revision.id")
    Integer insert(@Param("revision") NoteRevision revision, @Param("note") Note note);
}
