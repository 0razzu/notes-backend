package net.thumbtack.school.notes.database.mapper;


import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;


public interface NoteRevisionMapper {
    @Insert("INSERT INTO note_revision (body, note_id) VALUES (#{body}, #{note.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(NoteRevision revision);
    
    
    @Select("SELECT id, body, created, note_id FROM note_revision")
    @Results(id = "noteRevisionFields", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "note", column = "note_id", javaType = Note.class,
                    one = @One(
                            select = "net.thumbtack.school.notes.database.mapper.NoteMapper.get",
                            fetchType = FetchType.LAZY
                    )
//            ),
//            @Result(property = "comments", column = "id", javaType = List.class,
//                    many = @Many(
//                            select = "net.thumbtack.school.notes.database.mapper.CommentMapper.getByRevision",
//                            fetchType = FetchType.LAZY
//                    )
            )
    })
    List<NoteRevision> getByNote(Note note);
}
