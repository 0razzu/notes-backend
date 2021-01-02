package net.thumbtack.school.notes.database.mapper;


import net.thumbtack.school.notes.model.Section;
import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;


public interface SectionMapper {
    @Insert("INSERT INTO section (name, creator_id) VALUES (#{name}, #{creator.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(Section section);
    
    
    @Update("UPDATE section SET name = #{name}, creator_id = #{creator.id} WHERE id = #{id}")
    void update(Section section);
    
    
    @Select("SELECT id, name, creator_id FROM section WHERE id = #{id}")
    @Results(id = "sectionFields", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "creator", column = "creator_id", javaType = User.class,
                    one = @One(
                            select = "net.thumbtack.school.notes.database.mapper.UserMapper.get",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(property = "notes", column = "id", javaType = List.class,
                    many = @Many(
                            select = "net.thumbtack.school.notes.database.mapper.NoteMapper.getBySection",
                            fetchType = FetchType.LAZY
                    )
            )
    })
    Section get(int id);
    
    
    @Select("SELECT id, name FROM section WHERE creator_id = #{creator.id}")
    @ResultMap("sectionFields")
    List<Section> getByCreator(User creator);
    
    
    @Select("SELECT id, name, creator_id FROM section")
    @ResultMap("sectionFields")
    List<Section> getAll();
    
    
    @Delete("DELETE FROM section WHERE id = #{id}")
    void delete(Section section);
}
