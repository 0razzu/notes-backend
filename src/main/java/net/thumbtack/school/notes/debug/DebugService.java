package net.thumbtack.school.notes.debug;


import net.thumbtack.school.notes.dto.response.EmptyResponse;
import net.thumbtack.school.notes.error.ServerException;
import org.springframework.stereotype.Service;


@Service("debugServiceForTests")
public class DebugService {
    private final DebugDao debugDao;
    
    
    public DebugService(DebugDao debugDao) {
        this.debugDao = debugDao;
    }
    
    
    public EmptyResponse clear() throws ServerException {
        debugDao.clear();
        
        return new EmptyResponse();
    }
}
