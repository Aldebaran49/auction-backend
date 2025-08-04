package ru.andreev.auction.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class RegisterRequestDto {
    @NotBlank(message = "Username should be filled in")
    String username;

    @NotBlank(message = "Password should be filled in")
    @Size(min = 6, message = "Пароль должен содержать не менее 6 символов")
    String password;

    @NotBlank(message = "Email should be filled in")
    @Email(message = "Incorrect email")
    String email;
}
