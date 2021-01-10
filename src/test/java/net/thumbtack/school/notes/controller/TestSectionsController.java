package net.thumbtack.school.notes.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import net.thumbtack.school.notes.database.dao.SectionDao;
import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.dto.request.CreateSectionRequest;
import net.thumbtack.school.notes.dto.request.RenameSectionRequest;
import net.thumbtack.school.notes.dto.response.CreateSectionResponse;
import net.thumbtack.school.notes.dto.response.EmptyResponse;
import net.thumbtack.school.notes.dto.response.GetSectionsResponseItem;
import net.thumbtack.school.notes.dto.response.RenameSectionResponse;
import net.thumbtack.school.notes.dto.response.error.ErrorResponse;
import net.thumbtack.school.notes.model.Section;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static net.thumbtack.school.notes.database.util.Properties.JAVA_SESSION_ID;
import static net.thumbtack.school.notes.error.ErrorCode.SECTION_NAME_CONSTRAINT_VIOLATION;
import static net.thumbtack.school.notes.error.ErrorCode.TYPE_MISMATCH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class TestSectionsController extends TestControllerBase {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SectionDao sectionDao;
    @MockBean
    private SessionDao sessionDao;
    
    private static final User bill = new User("bill", "Bill1234", "Bill", null, "Blare", UserType.USER);
    private static final Section admSection = new Section("Adm section", admin);
    private static final Section billSection = new Section("Bill section", bill);
    
    
    @Test
    void testCreate() throws Exception {
        clearInvocations(sectionDao);
        clearInvocations(sessionDao);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(post("/api/sections")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new CreateSectionRequest("Adm section")))
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new CreateSectionResponse(0, "Adm section"),
                        mapper.readValue(response.getContentAsString(), CreateSectionResponse.class))
        );
        
        verify(sectionDao).insert(admSection);
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testCreateConstraints() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/api/sections")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new CreateSectionRequest("Section!")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("SECTION_NAME_CONSTRAINT_VIOLATION", "name",
                        SECTION_NAME_CONSTRAINT_VIOLATION.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testCreateNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/api/sections")
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new CreateSectionRequest("Section")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
    
    
    @Test
    void testRename() throws Exception {
        clearInvocations(sectionDao);
        clearInvocations(sessionDao);
        when(sectionDao.get(0)).thenReturn(billSection);
        when(sessionDao.getUser(anyString())).thenReturn(bill);
        
        MockHttpServletResponse response = mvc.perform(put("/api/sections/0")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new RenameSectionRequest("Bill section 1")))
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new RenameSectionResponse(0, "Bill section 1"),
                        mapper.readValue(response.getContentAsString(), RenameSectionResponse.class))
        );
        
        verify(sectionDao).update(new Section("Bill section 1", bill));
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testRenameConstraints() throws Exception {
        MockHttpServletResponse response = mvc.perform(put("/api/sections/0")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new RenameSectionRequest("$ection")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("SECTION_NAME_CONSTRAINT_VIOLATION", "name",
                        SECTION_NAME_CONSTRAINT_VIOLATION.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testRenameOtherUsersSection() throws Exception {
        when(sectionDao.get(0)).thenReturn(billSection);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(put("/api/sections/0")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new RenameSectionRequest("Bill section 1")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(notPermittedSet, getErrorSet(response));
    }
    
    
    @Test
    void testRenameNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(put("/api/sections/0")
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new RenameSectionRequest("Section")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
    
    
    @Test
    void testDelete() throws Exception {
        clearInvocations(sectionDao);
        clearInvocations(sessionDao);
        when(sectionDao.get(0)).thenReturn(billSection);
        when(sessionDao.getUser(anyString())).thenReturn(bill);
        
        MockHttpServletResponse response = mvc.perform(delete("/api/sections/0").cookie(cookie)
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new EmptyResponse(),
                        mapper.readValue(response.getContentAsString(), EmptyResponse.class))
        );
        
        verify(sectionDao).delete(billSection);
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testDeleteConstraints() throws Exception {
        MockHttpServletResponse response = mvc.perform(delete("/api/sections/m").cookie(cookie)
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("TYPE_MISMATCH", "id", TYPE_MISMATCH.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testDeleteOtherUsersSection() throws Exception {
        when(sectionDao.get(0)).thenReturn(admSection);
        when(sessionDao.getUser(anyString())).thenReturn(bill);
        
        MockHttpServletResponse response = mvc.perform(delete("/api/sections/0").cookie(cookie)
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(notPermittedSet, getErrorSet(response));
    }
    
    
    @Test
    void testDeleteOtherUsersSectionByAdmin() throws Exception {
        when(sectionDao.get(0)).thenReturn(billSection);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(delete("/api/sections/0").cookie(cookie)
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new EmptyResponse(),
                        mapper.readValue(response.getContentAsString(), EmptyResponse.class))
        );
    }
    
    
    @Test
    void testDeleteNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(delete("/api/sections/0")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
    
    
    @Test
    void testGetAll() throws Exception {
        clearInvocations(sectionDao);
        clearInvocations(sessionDao);
        when(sectionDao.getAll()).thenReturn(List.of(admSection, billSection));
        when(sessionDao.getUser(anyString())).thenReturn(bill);
        
        MockHttpServletResponse response = mvc.perform(get("/api/sections").cookie(cookie)
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        List<GetSectionsResponseItem> sectionsResponse = mapper.readValue(response.getContentAsString(),
                new TypeReference<>() {});
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(2, sectionsResponse.size()),
                () -> assertEquals("Adm section", sectionsResponse.get(0).getName()),
                () -> assertEquals("Bill section", sectionsResponse.get(1).getName())
        );
        
        verify(sectionDao).getAll();
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testGetAllNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/api/sections")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
}
