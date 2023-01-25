package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") long requestorId,
                                             @RequestBody CreateRequestDto itemRequestDto) {
        return itemRequestClient.addRequest(requestorId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader("X-Sharer-User-Id") long requestorId) {
        return itemRequestClient.getUserRequests(requestorId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader("X-Sharer-User-Id") long requestorId, @PathVariable long requestId) {
        return itemRequestClient.getRequest(requestorId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestsWithoutPagination(@RequestHeader("X-Sharer-User-Id") long requestorId) {
        return itemRequestClient.getAllRequests(requestorId);
    }

    @GetMapping(value = "/all", params = {"from", "size"})
    public ResponseEntity<Object> getAllRequests(@RequestParam @PositiveOrZero int from,
                                                 @RequestParam @Positive final int size,
                                                 @RequestHeader("X-Sharer-User-Id") long requestorId) {
        return itemRequestClient.getAllRequests(requestorId, from, size);
    }
}
