package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.database.mapper.*;
import net.thumbtack.school.notes.database.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;


public class DaoImplBase {
    protected SqlSession getSession() {
        return MyBatisUtil.getSqlSessionFactory().openSession();
    }
    
    
    protected CommentMapper getCommentMapper(SqlSession session) {
        return session.getMapper(CommentMapper.class);
    }
    
    
    protected NoteMapper getNoteMapper(SqlSession session) {
        return session.getMapper(NoteMapper.class);
    }
    
    
    protected NoteRevisionMapper getNoteRevisionMapper(SqlSession session) {
        return session.getMapper(NoteRevisionMapper.class);
    }
    
    
    protected RatingMapper getRatingMapper(SqlSession session) {
        return session.getMapper(RatingMapper.class);
    }
    
    
    protected SectionMapper getSectionMapper(SqlSession session) {
        return session.getMapper(SectionMapper.class);
    }
    
    
    protected SessionMapper getSessionMapper(SqlSession session) {
        return session.getMapper(SessionMapper.class);
    }
    
    
    protected UserMapper getUserMapper(SqlSession session) {
        return session.getMapper(UserMapper.class);
    }
}
