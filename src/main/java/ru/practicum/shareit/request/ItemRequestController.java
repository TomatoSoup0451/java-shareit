package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequest addRequest(@RequestHeader("X-Sharer-User-Id") long requestorId,
                                  @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.addRequest(requestorId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getUserRequests(@RequestHeader("X-Sharer-User-Id") long requestorId) {
        return itemRequestService.getUserRequests(requestorId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@RequestHeader("X-Sharer-User-Id") long requestorId, @PathVariable long requestId) {
        return itemRequestService.getRequest(requestorId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequestsWithoutPagination(@RequestHeader("X-Sharer-User-Id") long requestorId) {
        return itemRequestService.getAllRequests(requestorId);
    }

    @GetMapping(value = "/all", params = {"from", "size"})
    public List<ItemRequestDto> getAllRequests(@RequestParam int from, @RequestParam final int size,
                                               @RequestHeader("X-Sharer-User-Id") long requestorId) {
        return itemRequestService.getAllRequests(from, size, requestorId);
    }
}
