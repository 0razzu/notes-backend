package net.thumbtack.school.notes.database.mapper;


import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.Section;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.view.NoteView;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;


public interface NoteMapper {
    @Insert("INSERT INTO note (subject, author_id, section_id) VALUES (#{subject}, #{author.id}, #{section.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(Note note);
    
    
    @Update("UPDATE note SET section_id = #{section.id} WHERE id = #{id}")
    void update(Note note);
    
    
    @Select("SELECT id, subject, created, author_id, section_id FROM note WHERE id = #{id}")
    @Results(id = "noteFields", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "author", column = "author_id", javaType = User.class,
                    one = @One(
                            select = "net.thumbtack.school.notes.database.mapper.UserMapper.get",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(property = "section", column = "section_id", javaType = Section.class,
                    one = @One(
                            select = "net.thumbtack.school.notes.database.mapper.SectionMapper.get",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(property = "noteRevisions", column = "id", javaType = List.class,
                    many = @Many(
                            select = "net.thumbtack.school.notes.database.mapper.NoteRevisionMapper.getByNote",
                            fetchType = FetchType.LAZY
                    )
//            ),
//            @Result(property = "ratings", column = "id", javaType = List.class,
//                    many = @Many(
//                            select = "net.thumbtack.school.notes.database.mapper.RatingMapper.getByNote",
//                            fetchType = FetchType.LAZY
//                    )
            )
    })
    Note get(int id);
    
    
    @Select("WITH t AS (" +
            "   SELECT note.id AS id, subject, body, section_id, author_id, note.created AS created," +
            "       note_revision.id AS revision_id" +
            "   FROM note" +
            "   LEFT JOIN note_revision ON note.id = note_revision.note_id" +
            "   WHERE note.id = #{id}" +
            ") SELECT * FROM t WHERE revision_id = (SELECT max(revision_id) FROM t)")
    NoteView getView(int id);
    
    
    @Select("SELECT id, subject, created, author_id, section_id FROM note WHERE author_id = #{id}")
    @ResultMap("noteFields")
    List<Note> getByAuthor(User author);
    
    
    @Select("SELECT id, subject, created, author_id, section_id FROM note WHERE section_id = #{id}")
    @ResultMap("noteFields")
    List<Note> getBySection(Section section);
    
    
    @Delete("DELETE FROM note WHERE id = #{id}")
    void delete(Note note);
}
