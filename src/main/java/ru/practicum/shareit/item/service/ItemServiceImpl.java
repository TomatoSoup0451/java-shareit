package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IdNotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestDao;
import ru.practicum.shareit.user.dao.UserDao;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemDao;
    private final UserDao userDao;
    private final ItemRequestDao itemRequestDao;

    @Override
    public Item createItem(ItemDto itemDto, long userId) {
        return itemDao.createItem(ItemMapper.toItem(itemDto,
                userDao.getUser(userId)
                        .orElseThrow(() -> new IdNotFoundException("User with id = " + userId + " not found")),
                itemRequestDao.getRequest(itemDto.getRequest()).orElse(null)));
    }

    @Override
    public Item updateItem(ItemDto itemDto, long userId, long itemId) {
        return itemDao.updateItem(ItemMapper.toUpdatedItem(itemDto,
                userDao.getUser(userId)
                        .orElseThrow(() -> new IdNotFoundException("User with id = " + userId + " not found")),
                itemId));
    }

    @Override
    public ItemDto getItem(long id) {
        return ItemMapper.toItemDto(itemDao.getItem(id)
                .orElseThrow(() -> new IdNotFoundException("Item with id = " + id + " not found")));
    }

    @Override
    public List<ItemDto> getUserItems(long userId) {
        userDao.getUser(userId)
                .orElseThrow(() -> new IdNotFoundException("User with id = " + userId + " not found"));
        return itemDao.getUserItems(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsByName(String text) {
        return itemDao.getItemsByName(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
