package net.thumbtack.school.notes.controller;


import net.thumbtack.school.notes.dto.response.GetSettingsResponse;
import net.thumbtack.school.notes.service.DebugService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/debug")
public class DebugController {
    private final DebugService debugService;
    
    
    public DebugController(DebugService debugService) {
        this.debugService = debugService;
    }
    
    
    @GetMapping(path = "/settings",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GetSettingsResponse getSettings() {
        return debugService.getSettings();
    }
}
