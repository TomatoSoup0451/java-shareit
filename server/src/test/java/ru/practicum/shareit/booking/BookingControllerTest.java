package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    private final BookingDto newBookingDto = BookingDto.builder()
            .item(1L)
            .status("WAITING")
            .booker(1L)
            .start(LocalDateTime.now().plusHours(1))
            .end(LocalDateTime.now().plusHours(2))
            .build();
    private final Booking newBooking = Booking.builder()
            .item(Item.builder().id(1L).build())
            .status(Status.WAITING)
            .booker(User.builder().id(1L).build())
            .start(LocalDateTime.now().plusHours(1))
            .end(LocalDateTime.now().plusHours(2))
            .build();
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ItemService itemService;
    @MockBean
    private BookingService bookingService;

    @Test
    public void shouldReturnBookingWhenCreated() throws Exception {
        when(bookingService.createBooking(any(BookingDto.class), anyLong())).thenReturn(newBooking);
        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("WAITING")));
    }

    @Test
    public void shouldReturnBookingWhenUpdated() throws Exception {
        when(bookingService.updateStatus(anyLong(), anyLong(), anyBoolean())).thenReturn(newBooking);
        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newBookingDto))
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("WAITING")));
    }

    @Test
    public void shoudReturnBookingWhenGetById() throws Exception {
        when(bookingService.findBooking(anyLong(), anyLong())).thenReturn(newBooking);
        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("WAITING")));
    }

    @Test
    public void shouldReturnListOf1BookingWhenGetUserBookings() throws Exception {
        when(bookingService.getUserBookings(any(BookingState.class), anyLong())).thenReturn(List.of(newBooking));
        mvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].status", is("WAITING")));
    }

    @Test
    public void shouldReturnListOf1BookingWhenGetUserBookingsAndPaginated() throws Exception {
        when(bookingService.getUserBookings(any(BookingState.class), anyLong(), any(Pageable.class)))
                .thenReturn(List.of(newBooking));
        mvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].status", is("WAITING")));
    }


    @Test
    public void shouldReturnListOf1BookingWhenGetOwnerBookings() throws Exception {
        when(bookingService.getOwnerItemsBookings(any(BookingState.class), anyLong())).thenReturn(List.of(newBooking));
        mvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].status", is("WAITING")));
    }

    @Test
    public void shouldReturnListOf1BookingWhenGetOwnerBookingsWithPagination() throws Exception {
        when(bookingService.getOwnerItemsBookings(any(BookingState.class), anyLong(), any(Pageable.class)))
                .thenReturn(List.of(newBooking));
        mvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].status", is("WAITING")));
    }
}

