package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CreateCommentRequestDto;
import ru.practicum.shareit.item.dto.CreateItemRequestDto;
import ru.practicum.shareit.validationmarkers.OnCreate;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final SmartValidator validator;
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestBody CreateItemRequestDto requestDto,
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             BindingResult errors) {
        validator.validate(requestDto, errors, OnCreate.class);
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
        return itemClient.createItem(userId, requestDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody CreateItemRequestDto requestDto,
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId) {
        return itemClient.updateItem(userId, requestDto, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable long itemId,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getUserItems(userId);
    }

    @GetMapping(params = {"from", "size"})
    public ResponseEntity<Object> getUserItemsWithPagination(@RequestHeader("X-Sharer-User-Id") long userId,
                                                             @PositiveOrZero @RequestParam Integer from,
                                                             @Positive @RequestParam Integer size) {
        return itemClient.getUserItemsWithPagination(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByName(@RequestParam String text,
                                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getItemsByName(text, userId);
    }

    @GetMapping(path = "/search", params = {"from", "size", "text"})
    public ResponseEntity<Object> getItemsByNameWithPagination(@RequestParam String text,
                                                               @PositiveOrZero @RequestParam Integer from,
                                                               @Positive @RequestParam Integer size,
                                                               @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getItemsByNameWithPagination(text, from, size, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable long itemId,
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody CreateCommentRequestDto requestDto) {
        return itemClient.addComment(itemId, userId, requestDto);
    }
}
