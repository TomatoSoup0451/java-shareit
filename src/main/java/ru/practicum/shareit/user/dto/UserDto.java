package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.validationmarkers.OnCreate;
import ru.practicum.shareit.validationmarkers.OnUpdate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Value
public class UserDto {

    long id;
    String name;

    @Email(message = "email should be valid", groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(message = "email should not be blank", groups = OnCreate.class)
    String email;

}
