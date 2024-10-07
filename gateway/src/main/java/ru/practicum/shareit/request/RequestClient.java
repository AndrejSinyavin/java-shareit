package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDtoCreate;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestClient extends BaseClient  {
    static String API_PREFIX = "/requests";
    static String LOG_MESSAGE = "Validated: ok  Forwarded to server";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public Object add(long ownerId, ItemRequestDtoCreate requestBody) {
        log.info(LOG_MESSAGE);
        return post("", ownerId, requestBody);
    }

    public Object get(long ownerId, long requestId) {
        log.info(LOG_MESSAGE);
        return get("/" + requestId, ownerId);
    }

    public ResponseEntity<Object> getAllByOwner(long ownerId) {
        log.info(LOG_MESSAGE);
        return get("", ownerId);
    }

    public ResponseEntity<Object> getAllOtherUsers(long ownerId) {
        log.info(LOG_MESSAGE);
        return get("/all", ownerId);
    }

}
