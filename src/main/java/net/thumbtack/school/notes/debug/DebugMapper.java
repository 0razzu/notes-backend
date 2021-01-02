package net.thumbtack.school.notes.debug;


import org.apache.ibatis.annotations.Delete;


public interface DebugMapper {
    @Delete("DELETE FROM note_comment")
    void deleteComments();
    
    
    @Delete("DELETE FROM note")
    void deleteNotes();
    
    
    @Delete("DELETE FROM rating")
    void deleteRatings();
    
    
    @Delete("DELETE FROM section")
    void deleteSections();
    
    
    @Delete("DELETE FROM session")
    void deleteSessions();
    
    
    @Delete("DELETE FROM user")
    void deleteUsers();
}
