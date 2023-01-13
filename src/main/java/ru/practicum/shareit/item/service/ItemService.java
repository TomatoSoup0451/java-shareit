package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, long userId);

    ItemDto updateItem(ItemDto itemDto, long userId, long itemId);

    ItemDto getItem(long userId, long id);

    List<ItemDto> getUserItems(long userId);

    List<ItemDto> getItemsByName(String text);

    Comment addComment(long itemId, long userId, CommentDto commentDto);

    List<ItemDto> getUserItems(long userId, int from, int size);

    List<ItemDto> getItemsByName(String text, int from, int size);
}

