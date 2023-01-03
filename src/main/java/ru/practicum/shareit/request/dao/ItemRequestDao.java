package ru.practicum.shareit.request.dao;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Optional;

public interface ItemRequestDao {
    Optional<ItemRequest> getRequest(long request);
}

