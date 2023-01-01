package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Builder
@Getter
@Setter
public class User {

    private Long id;

    private String name;

    @Email(message = "email should be valid", groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(message = "email should not be blank", groups = OnCreate.class)
    private String email;
}
