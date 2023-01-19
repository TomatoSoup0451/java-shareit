package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql", "/import_test_data.sql"})
class IntegrationTest {

    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    private final UserDto firstUserDto = UserDto.builder()
            .name("NewName")
            .email("new@email.com")
            .build();


    @Test
    public void shouldAddNewUser() {
        User user = userService.createUser(firstUserDto);
        assertThat(user.getId(), equalTo(3L));
        assertThat(user.getName(), equalTo("NewName"));
        assertThat(user.getEmail(), equalTo("new@email.com"));
    }
}