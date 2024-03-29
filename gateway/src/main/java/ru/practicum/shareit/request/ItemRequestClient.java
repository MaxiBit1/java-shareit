package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        return post("", userId, itemRequestDto);
    }

    public ResponseEntity<Object> getItemRequests(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItemRequest(Long userId, Long requestId) {
        return get(UriComponentsBuilder
                .newInstance()
                .path("/{requestId}")
                .buildAndExpand(requestId)
                .toUriString(), userId);
    }

    public ResponseEntity<Object> getItemRequestAll(Long userId, Integer from, Integer size) {
        if (from == null && size == null) {
            return get("/all", userId);
        }
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get(UriComponentsBuilder
                .newInstance()
                .path("/all")
                .query("from={from}")
                .query("size={size}")
                .build()
                .toUriString(), userId, parameters);
    }
}

