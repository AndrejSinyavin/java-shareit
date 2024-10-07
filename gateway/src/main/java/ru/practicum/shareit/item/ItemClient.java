package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentDtoCreate;
import ru.practicum.shareit.item.dto.ItemDtoCreate;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemClient extends BaseClient {
    static String API_PREFIX = "/items";
    static String LOG_MESSAGE = "Validated: ok  Forwarded to server";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public Object add(ItemDtoCreate item, long ownerId) {
        log.info(LOG_MESSAGE);
        return post("", ownerId, item);
    }

    public Object update(ItemDtoUpdate item, long ownerId, long itemId) {
        log.info(LOG_MESSAGE);
        return patch("/" + itemId, ownerId, item);
    }

    public Object get(long itemId) {
        log.info(LOG_MESSAGE);
        return get("/" + itemId);
    }

    public ResponseEntity<Object> list(long ownerId) {
        log.info(LOG_MESSAGE);
        return get("", ownerId);
    }

    public ResponseEntity<Object> search(String text) {
        log.info(LOG_MESSAGE);
        return get("/search?text=" + text);
    }

    public ResponseEntity<Object> addComment(CommentDtoCreate comment, long ownerId, long itemId) {
        log.info(LOG_MESSAGE);
        return post("/" + itemId + "/comment", ownerId, comment);
    }

}
