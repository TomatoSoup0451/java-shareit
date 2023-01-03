package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(ItemDto itemDto, long userId);

    Item updateItem(ItemDto itemDto, long userId, long itemId);

    ItemDto getItem(long id);

    List<ItemDto> getUserItems(long userId);

    List<ItemDto> getItemsByName(String text);
}

