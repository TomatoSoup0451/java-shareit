package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplUnitTest {

    @InjectMocks
    ItemRequestServiceImpl itemRequestService;


    @Test
    public void shouldTrowExceptionWhenDescriptionEmpty() {
        ItemRequestDto itemRequestDto = getInvalidItemRequestDto();
        final BadRequestException e = assertThrows(BadRequestException.class,
                () -> itemRequestService.addRequest(1, itemRequestDto));
        assertThat(e.getMessage(), equalTo("Message in request should not be empty"));
    }

    private ItemRequestDto getInvalidItemRequestDto() {
        return ItemRequestDto.builder().description("").build();
    }

}