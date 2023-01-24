package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validationmarkers.OnCreate;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final SmartValidator validator;

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId,
                              BindingResult errors) {
        validator.validate(itemDto, errors, OnCreate.class);
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long itemId) {
        return itemService.updateItem(itemDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItem(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getUserItems(userId);
    }

    @GetMapping(params = {"from", "size"})
    public List<ItemDto> getUserItemsWithPagination(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @RequestParam int from,
                                                    @RequestParam int size) {
        return itemService.getUserItems(userId, getPageable(from, size));
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByName(@RequestParam String text) {
        return itemService.getItemsByName(text);
    }

    @GetMapping(path = "/search", params = {"from", "size", "text"})
    public List<ItemDto> getItemsByNameWithPagination(@RequestParam String text,
                                                      @RequestParam int from,
                                                      @RequestParam int size) {
        return itemService.getItemsByName(text, getPageable(from, size));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestBody CommentDto commentDto) {
        return CommentMapper.toCommentDto(itemService.addComment(itemId, userId, commentDto));
    }

    private Pageable getPageable(int from, int size) {
        if (from < 0) {
            throw new BadRequestException("Pagination parameter from should not be negative but was " + from);
        } else if (size <= 0) {
            throw new BadRequestException("Pagination parameter size should be positive but was " + size);
        }
        return PageRequest.of(from / size, size, Sort.by("id").ascending());
    }

}
