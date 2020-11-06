package net.thumbtack.school.notes.database.mapper;


import net.thumbtack.school.notes.model.Comment;
import net.thumbtack.school.notes.model.NoteRevision;
import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.time.LocalDateTime;


public interface CommentMapper {
    @Insert("INSERT INTO comment (created, body, author_id, note_revision_id)" +
            "VALUES (#{created}, #{body}, #{author.id}, #{revision.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(Comment comment);
    
    
    @Update("UPDATE comment SET created = #{created}, body = #{body}, revision_id = #{revision.id} " +
            "WHERE id = #{comment.id}")
    void update(Comment comment);
    
    
    @Select("SELECT (id, created, body, author_id, note_revision_id) " +
            "FROM comment WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "author", column = "author_id", javaType = User.class,
                    one = @One(
                            select = "net.thumbtack.school.notes.database.mappers.UserMapper.get",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(property = "revision", column = "note_revision_id", javaType = NoteRevision.class,
                    one = @One(
                            select = "net.thumbtack.school.notes.database.mappers.RevisionMapper.get",
                            fetchType = FetchType.LAZY
                    )
            )
    })
    Comment get(int id);
    
    
    @Select("SELECT (id, created, body, author_id, note_revision_id) " +
            "FROM comment WHERE created = #{created}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "author", column = "author_id", javaType = User.class,
                    one = @One(
                            select = "net.thumbtack.school.notes.database.mappers.UserMapper.get",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(property = "revision", column = "note_revision_id", javaType = NoteRevision.class,
                    one = @One(
                            select = "net.thumbtack.school.notes.database.mappers.RevisionMapper.get",
                            fetchType = FetchType.LAZY
                    )
            )
    })
    Comment getByCreated(LocalDateTime created);
    
    
    @Select("SELECT (id, created, body, author_id, note_revision_id) " +
            "FROM comment WHERE author_id = #{author.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "author", column = "author_id", javaType = User.class,
                    one = @One(
                            select = "net.thumbtack.school.notes.database.mappers.UserMapper.get",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(property = "revision", column = "note_revision_id", javaType = NoteRevision.class,
                    one = @One(
                            select = "net.thumbtack.school.notes.database.mappers.RevisionMapper.get",
                            fetchType = FetchType.LAZY
                    )
            )
    })
    Comment getByAuthor(User author);
    
    
    // TODO: getByRevision / getByNote...
}
