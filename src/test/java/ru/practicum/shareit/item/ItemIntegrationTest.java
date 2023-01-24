package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/reset.sql", "/schema.sql", "/import_test_data.sql"})
class ItemIntegrationTest {

    private final ItemService itemService;

    private final ItemDto newItemDto = ItemDto.builder()
            .name("NewName")
            .description("NewDescription")
            .available(true)
            .build();

    private final CommentDto newCommentDto = CommentDto.builder()
            .text("NewComment")
            .authorName("Name")
            .build();


    @Test
    public void shouldAddThirdItem() {
        ItemDto item = itemService.createItem(newItemDto, 1L);
        assertThat(item.getId(), equalTo(3L));
        assertThat(item.getName(), equalTo("NewName"));
        assertThat(item.getDescription(), equalTo("NewDescription"));
    }

    @Test
    public void shouldUpdateFirstItem() {
        ItemDto item = itemService.updateItem(newItemDto, 1L, 1L);
        assertThat(item.getId(), equalTo(1L));
        assertThat(item.getName(), equalTo("NewName"));
        assertThat(item.getDescription(), equalTo("NewDescription"));
    }

    @Test
    public void shouldReturnSecondItem() {
        ItemDto item = itemService.getItem(1L, 2L);
        assertThat(item.getId(), equalTo(2L));
        assertThat(item.getName(), equalTo("Item2"));
        assertThat(item.getDescription(), equalTo("Description2"));
    }

    @Test
    public void shouldReturnListOfTwoItemsForFirstUser() {
        List<ItemDto> items = itemService.getUserItems(1L);
        assertThat(items.size(), equalTo(2));
        assertThat(items.get(1).getId(), equalTo(2L));
        assertThat(items.get(1).getName(), equalTo("Item2"));
        assertThat(items.get(1).getDescription(), equalTo("Description2"));
    }

    @Test
    public void shouldReturnSecondItemWhenSearchTion1() {
        List<ItemDto> items = itemService.getItemsByName("tion1");
        assertThat(items.size(), equalTo(1));
        assertThat(items.get(0).getId(), equalTo(1L));
        assertThat(items.get(0).getName(), equalTo("Item1"));
        assertThat(items.get(0).getDescription(), equalTo("Description1"));
    }

    @Test
    public void shouldAddCommentFromUser2toItem1() {
        Comment comment = itemService.addComment(1, 2, newCommentDto);
        assertThat(comment.getId(), equalTo(2L));
        assertThat(comment.getText(), equalTo("NewComment"));
        assertThat(comment.getItem().getId(), equalTo(1L));
        assertThat(comment.getAuthor().getId(), equalTo(2L));
    }

    @Test
    public void shouldReturnListOfOneItemForUser1WithPagination() {
        List<ItemDto> items = itemService.getUserItems(1,
                PageRequest.of(0, 1));
        assertThat(items.size(), equalTo(1));
        assertThat(items.get(0).getId(), equalTo(1L));
        assertThat(items.get(0).getName(), equalTo("Item1"));
        assertThat(items.get(0).getDescription(), equalTo("Description1"));
    }

    @Test
    public void shouldReturnListOfOneItemWhenSearchWithPagination() {
        List<ItemDto> items = itemService.getItemsByName("tion",
                PageRequest.of(0, 10));
        assertThat(items.size(), equalTo(1));
        assertThat(items.get(0).getId(), equalTo(1L));
        assertThat(items.get(0).getName(), equalTo("Item1"));
        assertThat(items.get(0).getDescription(), equalTo("Description1"));
    }
}