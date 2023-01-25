package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/reset.sql", "/schema.sql", "/import_test_data.sql"})
public class ItemRequestIntegrationTest {

    private final ItemRequestService itemRequestService;

    private final ItemRequestDto newItemRequestDto = ItemRequestDto.builder()
            .description("New request")
            .build();

    @Test
    public void shouldAddNewRequest() {
        ItemRequest itemRequest = itemRequestService.addRequest(2L, newItemRequestDto);
        assertThat(itemRequest.getId(), equalTo(2L));
        assertThat(itemRequest.getDescription(), equalTo("New request"));
        assertThat(itemRequest.getRequestor().getId(), equalTo(2L));
    }

    @Test
    public void shouldGetRequest1() {
        ItemRequestDto itemRequest = itemRequestService.getRequest(2L, 1L);
        assertThat(itemRequest.getId(), equalTo(1L));
        assertThat(itemRequest.getDescription(), equalTo("Description1"));
        assertThat(itemRequest.getRequestor(), equalTo(2L));
    }

    @Test
    public void shouldGetListWithOneRequestForUser2() {
        List<ItemRequestDto> itemRequests = itemRequestService.getUserRequests(2L);
        assertThat(itemRequests.size(), equalTo(1));
        assertThat(itemRequests.get(0).getId(), equalTo(1L));
        assertThat(itemRequests.get(0).getDescription(), equalTo("Description1"));
        assertThat(itemRequests.get(0).getRequestor(), equalTo(2L));
    }

    @Test
    public void shouldGetListWithOneRequestForUser1() {
        List<ItemRequestDto> itemRequests = itemRequestService.getAllRequests(1L);
        assertThat(itemRequests.size(), equalTo(1));
        assertThat(itemRequests.get(0).getId(), equalTo(1L));
        assertThat(itemRequests.get(0).getDescription(), equalTo("Description1"));
        assertThat(itemRequests.get(0).getRequestor(), equalTo(2L));
    }

    @Test
    public void shouldGetListWithOneRequestForUser1WithPagination() {
        List<ItemRequestDto> itemRequests = itemRequestService.getAllRequests(PageRequest.of(0, 1), 1L);
        assertThat(itemRequests.size(), equalTo(1));
        assertThat(itemRequests.get(0).getId(), equalTo(1L));
        assertThat(itemRequests.get(0).getDescription(), equalTo("Description1"));
        assertThat(itemRequests.get(0).getRequestor(), equalTo(2L));
    }

}