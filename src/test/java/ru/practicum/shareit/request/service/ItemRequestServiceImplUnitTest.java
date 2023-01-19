package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplUnitTest {

    @Mock
    ItemRepository mockItemRepository;

    @Mock
    UserRepository mockUserRepository;

    @Mock
    ItemRequestRepository mockItemRequestRepository;


    ItemRequestServiceImpl itemRequestService = new ItemRequestServiceImpl(mockItemRequestRepository,
            mockUserRepository, mockItemRepository);


    @Test
    public void shouldTrowExceptionWhenDescriptionEmpty() {
        ItemRequestDto itemRequestDto = getInvalidItemRequestDto();
        final BadRequestException e = assertThrows(BadRequestException.class,
                () -> itemRequestService.addRequest(1, itemRequestDto));
        assertThat(e.getMessage(), equalTo("Message in request should not be empty"));
    }


    private ItemRequestDto getValidItemRequestDto(){
        return ItemRequestDto.builder().description("Description").build();
    }

    private ItemRequestDto getInvalidItemRequestDto(){
        return ItemRequestDto.builder().description("").build();
    }

}