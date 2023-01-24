package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerIdOrderByIdAsc(long ownerId);

    @Query(" select i from Item i " +
            "where i.available = true and upper(i.name) like upper(concat('%', :text, '%')) " +
            "or  i.available = true and upper(i.description) like upper(concat('%', :text, '%')) ")
    List<Item> findByName(String text);

    @Query(" select i from Item i " +
            "where i.available = true and upper(i.name) like upper(concat('%', :text, '%')) " +
            "or  i.available = true and upper(i.description) like upper(concat('%', :text, '%')) " +
            "order by i.id asc ")
    List<Item> findByName(String text, Pageable pageable);

    List<Item> findAllByRequest(ItemRequest itemRequest);

    List<Item> findByOwnerId(long ownerId, Pageable pageable);
}
