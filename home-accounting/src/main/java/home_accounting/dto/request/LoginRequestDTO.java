package home_accounting.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

        @NotBlank(message = "Username bo'sh bo'lmasligi kerak")
        String username,

        @NotBlank(message = "Parol bo'sh bo'lmasligi kerak")
        String password

) {}
