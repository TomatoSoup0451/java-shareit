package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.validationmarkers.OnCreate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Getter
public class ItemDto {

    @NotBlank(message = "name should not be empty", groups = OnCreate.class)
    private final String name;
    @NotBlank(message = "description should not be empty", groups = OnCreate.class)
    private final String description;
    @NotNull(message = "status should not be empty", groups = OnCreate.class)
    private final Boolean available;
    private final long owner;
    private final long request;
    @Setter
    private long id;

}
