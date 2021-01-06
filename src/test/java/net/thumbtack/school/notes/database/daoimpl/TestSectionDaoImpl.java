package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.Section;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class TestSectionDaoImpl extends TestDaoImplBase {
    private static final User creator1 = new User("creator1", "pa55word", "Creator", null, "Creator", UserType.SUPER);
    private static final User creator2 = new User("creator2", "pa55word", "Creator", null, "Creator", UserType.USER);
    
    
    public boolean compareSections(Section section1, Section section2) {
        return section1.getId() == section2.getId() &&
                section1.getName().equals(section2.getName()) &&
                section1.getCreator().equals(section2.getCreator());
    }
    
    
    @Test
    void testInsert() throws ServerException {
        userDao.insert(creator1);
        Section section = new Section("section 1", creator1);
        
        sectionDao.insert(section);
        
        assertTrue(compareSections(section, sectionDao.get(section.getId())));
    }
    
    
    @Test
    void testInsertSameName() throws ServerException {
        userDao.insert(creator1);
        userDao.insert(creator2);
        Section section1 = new Section("section 1", creator1);
        Section section2 = new Section("section 1", creator2);
        
        sectionDao.insert(section1);
        
        assertThrows(ServerException.class, () -> sectionDao.insert(section2));
    }
    
    
    @Test
    void testUpdate() throws ServerException {
        userDao.insert(creator1);
        Section section = new Section("section 1", creator1);
        sectionDao.insert(section);
        
        section.setName("section 2");
        sectionDao.update(section);
        
        assertTrue(compareSections(section, sectionDao.get(section.getId())));
    }
    
    
    @Test
    void testUpdateSameName() throws ServerException {
        userDao.insert(creator1);
        userDao.insert(creator2);
        Section section1 = new Section("section 1", creator1);
        Section section2 = new Section("section 2", creator2);
        sectionDao.insert(section1);
        sectionDao.insert(section2);
        
        section2.setName("section 1");
        
        assertThrows(ServerException.class, () -> sectionDao.update(section2));
    }
    
    
    @Test
    void testGetAll() throws ServerException {
        userDao.insert(creator1);
        userDao.insert(creator2);
        Section section1 = new Section("section 1", creator1);
        Section section2 = new Section("section 2", creator2);
        Section section3 = new Section("section 3", creator2);
        sectionDao.insert(section1);
        sectionDao.insert(section2);
        sectionDao.insert(section3);
        
        assertEquals(Set.of(section1, section2, section3), Set.copyOf(sectionDao.getAll()));
    }
    
    
    @Test
    void testGetByCreator() throws ServerException {
        userDao.insert(creator1);
        userDao.insert(creator2);
        Section section1 = new Section("section 1", creator1);
        Section section2 = new Section("section 2", creator2);
        Section section3 = new Section("section 3", creator2);
        sectionDao.insert(section1);
        sectionDao.insert(section2);
        sectionDao.insert(section3);
        
        assertAll(
                () -> assertEquals(List.of(section1), sectionDao.getByCreator(creator1)),
                () -> assertEquals(Set.of(section2, section3), Set.copyOf(sectionDao.getByCreator(creator2)))
        );
    }
    
    
    @Test
    void testDelete() throws ServerException {
        userDao.insert(creator1);
        Section section1 = new Section("section 1", creator1);
        Section section2 = new Section("section 2", creator1);
        sectionDao.insert(section1);
        sectionDao.insert(section2);
        
        sectionDao.delete(section1);
        
        assertAll(
                () -> assertNull(sectionDao.get(section1.getId())),
                () -> assertTrue(compareSections(section2, sectionDao.get(section2.getId())))
        );
    }
}
