package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.IdNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ItemDaoImpl implements ItemDao {

    private final Map<Long, Item> items = new HashMap<>();
    private long id = 0;

    @Override
    public Item createItem(Item item) {
        item.setId(++id);
        items.put(item.getId(), item);
        log.info("Item with id = {} added", item.getId());
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        if (!items.containsKey(item.getId())) {
            throw new IdNotFoundException("Item with id = " + item.getId() + " not found");
        }
        Item result = items.get(item.getId());
        if (result.getOwner().getId() != item.getOwner().getId()) {
            throw new ForbiddenException("Item with id = " + item.getId() +
                    " can't be modified by user with id = " + item.getOwner().getId());
        }
        if (item.getName() != null && !item.getName().isEmpty()) {
            result.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isEmpty()) {
            result.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            result.setAvailable(item.getAvailable());
        }
        log.info("Item with id = {} updated", result.getId());
        return result;
    }

    @Override
    public Optional<Item> getItem(long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> getUserItems(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemsByName(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return items.values().stream()
                .filter(item -> ((item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase())) &&
                        item.getAvailable()))
                .collect(Collectors.toList());
    }
}
