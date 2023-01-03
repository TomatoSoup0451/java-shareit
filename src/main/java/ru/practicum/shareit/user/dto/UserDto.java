package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.validationmarkers.OnCreate;
import ru.practicum.shareit.validationmarkers.OnUpdate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
public class UserDto {

    private long id;

    private String name;

    @Email(message = "email should be valid", groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(message = "email should not be blank", groups = OnCreate.class)
    private String email;

}
