package ru.practicum.shareit.request.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ItemRequestDaoImpl implements ItemRequestDao {

    private final Map<Long, ItemRequest> requests = new HashMap<>();
    private final long id = 0;

    @Override
    public Optional<ItemRequest> getRequest(long request) {
        return Optional.ofNullable(requests.get(id));
    }
}
