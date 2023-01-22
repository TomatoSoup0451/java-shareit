package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.IdNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ItemServiceUnitTest {

    private final ItemDto newItemDto = ItemDto.builder()
            .name("NewName")
            .description("NewDescription")
            .available(true)
            .build();
    @Mock
    ItemRepository mockItemRepository;
    @Mock
    UserRepository mockUserRepository;
    @Mock
    ItemRequestRepository mockItemRequestRepository;
    @Mock
    BookingRepository mockBookingRepository;
    @Mock
    CommentRepository mockCommentRepository;
    @InjectMocks
    ItemServiceImpl itemService;

    @Test
    public void shouldTrowForbiddenExceptionWhenAccessedByWrongUser() {
        Mockito
                .when(mockItemRepository.findById(any()))
                .thenReturn(Optional.of(Item.builder()
                        .owner(User.builder().id(1).build())
                        .build()));
        final ForbiddenException e = assertThrows(ForbiddenException.class,
                () -> itemService.updateItem(newItemDto, 99, 1));
        assertThat(e.getMessage(), equalTo("Item with id = 1 can't be modified by user with id = 99"));
    }

    @Test
    public void shouldTrowNotFoundExceptionWhenNoUser() {
        Mockito
                .when(mockItemRepository.findById(any()))
                .thenReturn(Optional.empty());
        final IdNotFoundException e = assertThrows(IdNotFoundException.class,
                () -> itemService.updateItem(newItemDto, 1, 99));
        assertThat(e.getMessage(), equalTo("Item with id = 99 not found"));
    }

    @Test
    public void shouldReturnEmptyListWhenSearchWithEmptyString() {
        List<ItemDto> items = itemService.getItemsByName("");
        assertThat(items.size(), equalTo(0));
    }

    @Test
    public void shouldReturnEmptyListWhenSearchWithEmptyStringAndPagination() {
        List<ItemDto> items = itemService.getItemsByName("", 0, 1);
        assertThat(items.size(), equalTo(0));
    }

    @Test
    public void shouldTrowBadRequestExceptionWhenPaginationFromNegative() {
        final BadRequestException e = assertThrows(BadRequestException.class,
                () -> itemService.getItemsByName("", -1, 10));
        assertThat(e.getMessage(), equalTo("Pagination parameter from should not be negative but was -1"));
    }

    @Test
    public void shouldTrowBadRequestExceptionWhenPaginationSizeNegative() {
        final BadRequestException e = assertThrows(BadRequestException.class,
                () -> itemService.getItemsByName("", 0, -1));
        assertThat(e.getMessage(), equalTo("Pagination parameter size should be positive but was -1"));
    }
}
