package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    private final UserDto newUserDto = UserDto.builder()
            .name("NewName")
            .email("new@email.com")
            .build();

    private final UserDto blankEmailUserDto = UserDto.builder()
            .name("NewName")
            .email("")
            .build();

    private final UserDto invalidEmailUserDto = UserDto.builder()
            .name("NewName")
            .email("dfgd")
            .build();
    private final User newUser = User.builder()
            .name("NewName")
            .email("new@email.com")
            .build();
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService userService;

    @Test
    public void shouldCreateNewUser() throws Exception {
        when(userService.createUser(any(UserDto.class))).thenReturn(newUser);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(newUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("NewName")));
    }

    @Test
    public void shouldReturnListWithOneUsers() throws Exception {
        when(userService.getUsers()).thenReturn(List.of(newUserDto));
        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].name", is("NewName")));
    }

    @Test
    public void shouldReturnUser() throws Exception {
        when(userService.getUser(anyLong())).thenReturn(newUserDto);
        mvc.perform(get("/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("NewName")));
    }

    @Test
    public void shouldReturnUserDtoWhenUpdated() throws Exception {
        when(userService.updateUser(anyLong(), any(UserDto.class))).thenReturn(newUser);
        mvc.perform(patch("/users/{id}", 1)
                        .content(mapper.writeValueAsString(newUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("NewName")));
    }

    @Test
    public void shouldCallDeleteMethodOneTimeWhenDeleted() throws Exception {
        mvc.perform(delete("/users/{id}", 1))
                .andExpect(status().isOk());
        Mockito.verify(userService, Mockito.times(1)).deleteUser(1);
    }

    @Test
    public void shouldThrowBadRequestWhenCreateInvalidUser() throws Exception {
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(blankEmailUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldThrowBadRequestWhenUpdatedInvalid() throws Exception {
        when(userService.updateUser(anyLong(), any(UserDto.class))).thenReturn(newUser);
        mvc.perform(patch("/users/{id}", 1)
                        .content(mapper.writeValueAsString(invalidEmailUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
