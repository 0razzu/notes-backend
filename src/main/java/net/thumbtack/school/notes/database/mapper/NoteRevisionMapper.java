package net.thumbtack.school.notes.database.mapper;


import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;


public interface NoteRevisionMapper {
    @Insert("INSERT INTO note_revision (body, created, note_id) VALUES (#{body}, #{created}, #{note.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(NoteRevision revision);
    
    
    @Select("SELECT id, body, created, note_id FROM note_revision WHERE id = #{id}")
    @Results(id = "noteRevisionFields", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "note", column = "note_id", javaType = Note.class,
                    one = @One(
                            select = "net.thumbtack.school.notes.database.mapper.NoteMapper.get",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(property = "comments", column = "id", javaType = List.class,
                    many = @Many(
                            select = "net.thumbtack.school.notes.database.mapper.CommentMapper.getByNoteRevision",
                            fetchType = FetchType.LAZY
                    )
            )
    })
    NoteRevision get(int id);
    
    
    @Select("SELECT id, body, created, note_id FROM note_revision WHERE note_id = #{id}")
    @ResultMap("noteRevisionFields")
    List<NoteRevision> getByNote(Note note);
    
    
    @Select("WITH t AS (" +
            "   SELECT note_revision.id, body, note_revision.created, note_id" +
            "   FROM note" +
            "   LEFT JOIN note_revision ON note.id = note_revision.note_id" +
            "   WHERE note.id = #{id}" +
            ") SELECT * FROM t WHERE id = (SELECT max(id) FROM t)")
    @ResultMap("noteRevisionFields")
    NoteRevision getMostRecent(Note note);
}
