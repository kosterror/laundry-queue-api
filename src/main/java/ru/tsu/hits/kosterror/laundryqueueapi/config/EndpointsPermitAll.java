package ru.tsu.hits.kosterror.laundryqueueapi.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.api.Endpoint;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties("security.permit-all")
@Slf4j
public class EndpointsPermitAll {

    private List<Endpoint> endpoints;
    private List<AntPathRequestMatcher> requestMatchers = new ArrayList<>();

    @PostConstruct
    private void init() {
        for (Endpoint endpoint : endpoints) {
            requestMatchers.add(new AntPathRequestMatcher(endpoint.getPattern(), endpoint.getMethod()));
        }

        log.info("Белый список ручек: {}", requestMatchers);
    }

}
