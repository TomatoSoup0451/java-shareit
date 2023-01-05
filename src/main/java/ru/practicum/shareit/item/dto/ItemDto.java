package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.validationmarkers.OnCreate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Value
public class ItemDto {
    @NotBlank(message = "name should not be empty", groups = OnCreate.class)
    String name;
    @NotBlank(message = "description should not be empty", groups = OnCreate.class)
    String description;
    @NotNull(message = "status should not be empty", groups = OnCreate.class)
    Boolean available;
    long owner;
    long request;
    long id;
}
