package home_accounting.dto.request;

import home_accounting.entity.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequestDTO(

        @NotNull(message = "Kategoriya nomi bo'sh bo'lmasligi kerak")
        @Size(min = 2, max = 100, message = "Kategoriya nomi 2-100 belgi orasida bo'lishi kerak")
        String name,

        @NotNull(message = "Kategoriya bo'sh bo'lmasligi kerak")
        TransactionType type

) {}
