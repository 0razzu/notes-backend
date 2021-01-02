package net.thumbtack.school.notes.debug;


import net.thumbtack.school.notes.dto.response.EmptyResponse;
import net.thumbtack.school.notes.error.ServerException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController("debugControllerForTests")
@RequestMapping("/api/debug")
public class DebugController {
    private final DebugService debugService;
    
    
    public DebugController(DebugService debugService) {
        this.debugService = debugService;
    }
    
    
    @PostMapping(path = "/clear",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse register() throws ServerException {
        return debugService.clear();
    }
}
