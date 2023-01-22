package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/reset.sql", "/schema.sql", "/import_test_data.sql"})
class UserIntegrationTest {

    private final UserService userService;

    private final UserDto newUserDto = UserDto.builder()
            .name("NewName")
            .email("new@email.com")
            .build();

    @Test
    public void shouldReturnListOfTwoUsers() {
        List<UserDto> users = userService.getUsers();
        assertThat(users.size(), equalTo(2));
        assertThat(users.get(0).getId(), equalTo(1L));
        assertThat(users.get(0).getName(), equalTo("User1"));
        assertThat(users.get(0).getEmail(), equalTo("email1@email.com"));
    }

    @Test
    public void shouldReturnSecondUser() {
        UserDto user = userService.getUser(2L);
        assertThat(user.getId(), equalTo(2L));
        assertThat(user.getName(), equalTo("User2"));
        assertThat(user.getEmail(), equalTo("email2@email.com"));
    }

    @Test
    public void shouldUpdateSecondUser() {
        User user = userService.updateUser(2, newUserDto);
        assertThat(user.getId(), equalTo(2L));
        assertThat(user.getName(), equalTo("NewName"));
        assertThat(user.getEmail(), equalTo("new@email.com"));
    }

    @Test
    public void shouldReturnOneUserAfterDeletion() {
        userService.deleteUser(2L);
        List<UserDto> users = userService.getUsers();
        assertThat(users.size(), equalTo(1));
        assertThat(users.get(0).getId(), equalTo(1L));
        assertThat(users.get(0).getName(), equalTo("User1"));
        assertThat(users.get(0).getEmail(), equalTo("email1@email.com"));
    }

    @Test
    public void shouldAddNewUser() {
        User user = userService.createUser(newUserDto);
        assertThat(user.getId(), equalTo(3L));
        assertThat(user.getName(), equalTo("NewName"));
        assertThat(user.getEmail(), equalTo("new@email.com"));
    }

}