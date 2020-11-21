package net.thumbtack.school.notes.database.mapper;


import net.thumbtack.school.notes.model.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.time.LocalDateTime;
import java.util.List;


public interface CommentMapper {
    @Insert("INSERT INTO note_comment (created, body, author_id, note_revision_id)" +
            "VALUES (#{created}, #{body}, #{author.id}, #{revision.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(Comment comment);
    
    
    @Update("UPDATE note_comment SET created = #{created}, body = #{body}, revision_id = #{revision.id} " +
            "WHERE id = #{comment.id}")
    void update(Comment comment);
    
    
    @Select("SELECT (id, created, body, author_id, note_revision_id) " +
            "FROM note_comment WHERE id = #{id}")
    @Results(id = "commentFields", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "author", column = "author_id", javaType = User.class,
                    one = @One(
                            select = "net.thumbtack.school.notes.database.mapper.UserMapper.get",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(property = "noteRevision", column = "note_revision_id", javaType = NoteRevision.class,
                    one = @One(
                            select = "net.thumbtack.school.notes.database.mapper.NoteRevisionMapper.get",
                            fetchType = FetchType.LAZY
                    )
            )
    })
    Comment get(int id);
    
    
    @Select("SELECT id, created, body, author_id, note_revision_id " +
            "FROM note_comment WHERE created = #{created}")
    @ResultMap("commentFields")
    List<Comment> getByCreated(LocalDateTime created);
    
    
    @Select("SELECT id, created, body, author_id, note_revision_id " +
            "FROM note_comment WHERE author_id = #{author.id}")
    @ResultMap("commentFields")
    List<Comment> getByAuthor(User author);
    
    
    @Select("SELECT id, created, body, author_id, note_revision_id " +
            "FROM note_comment WHERE section_id = #{section.id}")
    @ResultMap("commentFields")
    List<Comment> getBySection(Section section);
    
    
    @Select("SELECT id, created, body, author_id, note_revision_id " +
            "FROM note_comment WHERE note_revision_id = #{revision.id}")
    @ResultMap("commentFields")
    List<Comment> getByNoteRevision(NoteRevision revision);
    
    
    @Select("SELECT note_comment.id, note_comment.created, note_comment.body, author_id, note_revision_id " +
            "FROM note_comment " +
            "JOIN note_revision ON note_comment.note_revision_id = note_revision.id " +
            "WHERE note_id = #{note.id}")
    @ResultMap("commentFields")
    List<Comment> getByNote(Note note);
    
    
    @Delete("DELETE FROM note_comment WHERE id = #{id}")
    void delete(Comment comment);
    
    
    @Delete("DELETE FROM note_comment")
    void deleteAll();
}
