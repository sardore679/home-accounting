package home_accounting.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(

        @NotBlank(message = "User bo'sh bo'lmasligi kerak")
        @Size(min = 3, max = 50, message = "Username 4 - 50 belgi orasidan bolishi kerak")
        String username,

        @NotBlank(message = "Email bo'sh bo'lmasligi kerak")
        @Email(message = "Email formati noto'g'ri")
        String email,

        @NotBlank(message = "Parol bo'sh bo'lmasligi kerak")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%&~]).{8,}$",
                message = "Parol kamida 8 ta belgi, katta kichik harflar, sonlardan va bitta maxsus belgidan iborat bo'lishi kerak"

        )
        String password

) {}
