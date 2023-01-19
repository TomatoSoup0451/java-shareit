package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    ObjectMapper mapper;

    private ItemRequestDto firstItemRequestDto = ItemRequestDto.builder().description("Description1").build();
    private ItemRequestDto secondItemRequestDto = ItemRequestDto.builder().description("Description2").build();
    private ItemRequest itemRequest = ItemRequest.builder().description("Description1").build();

    @Test
    void shouldAddNewRequest() throws Exception {
        when(itemRequestService.addRequest(anyLong(), any())).thenReturn(itemRequest);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(firstItemRequestDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Description1")));
    }

    @Test
    void shouldGetRequest() throws Exception {
        when(itemRequestService.getRequest(anyLong(), anyLong())).thenReturn(firstItemRequestDto);
        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Description1")));
    }

    @Test
    void shouldGetListOfRequestsWithTwoElements() throws Exception {
        when(itemRequestService.getAllRequests(anyLong())).thenReturn(List.of(firstItemRequestDto,
                secondItemRequestDto));
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].description", is("Description1")));
    }

    @Test
    void shouldGetListOfRequestsWithOneElement() throws Exception {
        when(itemRequestService.getAllRequests(anyInt(), anyInt(), anyLong())).thenReturn(List.of(firstItemRequestDto));
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].description", is("Description1")));

    }

    @Test
    void shouldReturnListOfRequestsWithTwoElementsForOneUser() throws Exception {
        when(itemRequestService.getUserRequests(anyLong())).thenReturn((List.of(firstItemRequestDto,
                secondItemRequestDto)));
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].description", is("Description1")));
    }
}