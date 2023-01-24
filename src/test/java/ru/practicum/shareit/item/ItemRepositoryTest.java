package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/reset.sql", "/schema.sql", "/import_test_data.sql"})
class ItemRepositoryTest {

    private final ItemRepository itemRepository;

    @Test
    public void shouldReturnListOf1ItemWhenSearch() {
        List<Item> items = itemRepository.findByName("eScRi");
        System.out.println(items);
        assertThat(items.size(), equalTo(1));
        assertThat(items.get(0).getId(), equalTo(1L));
        assertThat(items.get(0).getName(), equalTo("Item1"));
        assertThat(items.get(0).getDescription(), equalTo("Description1"));
    }

    @Test
    public void shouldReturnEmptyListWhenSearchWrong() {
        List<Item> items = itemRepository.findByName("xdcfgvhbjnkm");
        assertThat(items.size(), equalTo(0));
    }

    @Test
    public void shouldReturnListOf1ItemWhenSearchWithPagination() {
        List<Item> items = itemRepository.findByName("eScRi", PageRequest.of(0, 1));
        assertThat(items.size(), equalTo(1));
        assertThat(items.get(0).getId(), equalTo(1L));
        assertThat(items.get(0).getName(), equalTo("Item1"));
        assertThat(items.get(0).getDescription(), equalTo("Description1"));
    }

}