package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {
    Item createItem(Item item);

    Item updateItem(Item toItem);

    Optional<Item> getItem(long id);

    List<ItemDto> getUserItems(long userId);

    List<ItemDto> getItemsByName(String text);
}
