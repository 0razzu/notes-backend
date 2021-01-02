package net.thumbtack.school.notes.service;


import net.thumbtack.school.notes.database.util.Properties;
import net.thumbtack.school.notes.dto.response.GetSettingsResponse;
import org.springframework.stereotype.Service;


@Service
public class DebugService extends ServiceBase {
    protected DebugService(Properties properties) {
        super(properties, null, null, null, null, null, null);
    }
    
    
    public GetSettingsResponse getSettings() {
        return new GetSettingsResponse(
                properties.getMaxNameLength(),
                properties.getMinPasswordLength(),
                properties.getUserIdleTimeout()
        );
    }
}
