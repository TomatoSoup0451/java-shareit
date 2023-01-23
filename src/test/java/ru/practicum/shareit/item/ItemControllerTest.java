package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    private final ItemDto newItemDto = ItemDto.builder()
            .name("NewName")
            .description("NewDescription")
            .available(true)
            .build();
    private final CommentDto newCommentDto = CommentDto.builder()
            .text("NewComment")
            .authorName("Name")
            .build();
    private final Comment newComment = Comment.builder()
            .text("NewComment")
            .author(User.builder().name("Name").build())
            .build();
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ItemService itemService;

    @Test
    public void shouldReturnItemWhenCreated() throws Exception {
        when(itemService.createItem(any(ItemDto.class), anyLong())).thenReturn(newItemDto);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(newItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("NewName")));
    }

    @Test
    public void shouldReturnItemWhenUpdate() throws Exception {
        when(itemService.updateItem(any(ItemDto.class), anyLong(), anyLong())).thenReturn(newItemDto);
        mvc.perform(patch("/items/{id}", 1)
                        .content(mapper.writeValueAsString(newItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("NewName")));
    }

    @Test
    public void shouldReturnItemWhenGetById() throws Exception {
        when(itemService.getItem(anyLong(), anyLong())).thenReturn(newItemDto);
        mvc.perform(get("/items/{id}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("NewName")));
    }

    @Test
    public void shouldReturnListOf1ItemForUser() throws Exception {
        when(itemService.getUserItems(anyLong())).thenReturn(List.of(newItemDto));
        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].name", is("NewName")));
    }

    @Test
    public void shouldReturnListOf1ItemForUserWhenPaginated() throws Exception {
        when(itemService.getUserItems(anyLong(), any(Pageable.class))).thenReturn(List.of(newItemDto));
        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("from", "0")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].name", is("NewName")));
    }

    @Test
    public void shouldReturnListOf1ItemWhenSearch() throws Exception {
        when(itemService.getItemsByName(anyString())).thenReturn(List.of(newItemDto));
        mvc.perform(get("/items/search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("text", "query"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].name", is("NewName")));
    }

    @Test
    public void shouldReturnListOf1ItemWhenSearchAndPaginated() throws Exception {
        when(itemService.getItemsByName(anyString(), any(Pageable.class))).thenReturn(List.of(newItemDto));
        mvc.perform(get("/items/search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("from", "0")
                        .param("size", "10")
                        .param("text", "query"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].name", is("NewName")));
    }

    @Test
    public void shouldReturnCommentWhenAdd() throws Exception {
        when(itemService.addComment(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(newComment);
        mvc.perform(post("/items/{itemId}/comment", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(newCommentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is("NewComment")));
    }

}

