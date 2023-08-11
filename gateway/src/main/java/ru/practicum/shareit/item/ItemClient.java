package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(long userId, ItemDto itemDto) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> upgradeItem(long itemId, ItemDto itemDto, long userId) {
        return patch(UriComponentsBuilder
                .newInstance()
                .path("/{itemId}")
                .buildAndExpand(itemId)
                .toUriString(), userId, itemDto);
    }

    public ResponseEntity<Object> getItem(long itemId, long userId) {
        return get(UriComponentsBuilder
                .newInstance()
                .path("/{itemId}")
                .buildAndExpand(itemId)
                .toUriString(), userId);
    }

    public ResponseEntity<Object> getItems(long userId, Long from, Long size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get(UriComponentsBuilder
                .newInstance()
                .query("from={from}")
                .query("size={size}")
                .build()
                .toUriString(), userId, parameters);
    }

    public ResponseEntity<Object> getSearchItems(long userId, String text, Long from, Long size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get(UriComponentsBuilder
                .newInstance()
                .path("/search")
                .query("text={text}")
                .query("from={from}")
                .query("size={size}")
                .build()
                .toUriString(), userId, parameters);
    }

    public ResponseEntity<Object> createComment(long itemId, long userId, CommentDto commentDto) {
        return post(UriComponentsBuilder
                .newInstance()
                .path("/{userId}")
                .path("/comment")
                .buildAndExpand(itemId)
                .toUriString(), userId, commentDto);
    }
}
