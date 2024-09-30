package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDtoCreate;
import ru.practicum.shareit.user.dto.UserDtoUpdate;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserClient extends BaseClient {
    static String API_PREFIX = "/users";
    static String LOG_MESSAGE = "Validated: ok  Forwarded to server";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public Object add(UserDtoCreate user) {
        log.info(LOG_MESSAGE);
        return post("", user);
    }

    public Object update(UserDtoUpdate user, long userId) {
        log.info(LOG_MESSAGE);
        return patch("/" + userId, user);
    }

    public Object get(long userId) {
        log.info(LOG_MESSAGE);
        return get("/" + userId);
    }

    public Object delete(long userId) {
        log.info(LOG_MESSAGE);
        return delete("/" + userId);
    }

}
