package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Builder
@Getter
@Setter
@Entity
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "available", nullable = false)
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}
