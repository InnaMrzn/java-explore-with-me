package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.ViewStats;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StatsClient extends BaseClient {
    private static final String API_PREFIX = "";
    private static final DateTimeFormatter dateFormat
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public void save(HttpServletRequest request) {
        EndpointHitDto hitDto = new EndpointHitDto();
        hitDto.setApp("ewm-main");
        hitDto.setIp(request.getRemoteAddr());
        hitDto.setUri(request.getRequestURI());
        hitDto.setTimestamp(dateFormat.format(LocalDateTime.now()));
        post("/hit", hitDto);
    }

    public List<ViewStats> getStats(String start, String end, List<Long> ids, Boolean unique) {

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("start", start);
        parameters.add("end", end);
        parameters.add("unique", unique.toString());

        if (ids != null) {
            for (Long id : ids) {
                parameters.add("uris", "/events/" + id);
            }
        }

        log.info("GET /stats?");
        for (String key : parameters.keySet()) {
            log.info(key + ": " + parameters.get(key));
        }
        List<ViewStats> result = get("/stats", parameters).getBody();
        return (result != null ? result : new ArrayList<>());
    }
}
