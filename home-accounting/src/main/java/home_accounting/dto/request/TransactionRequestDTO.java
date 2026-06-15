package home_accounting.dto.request;

import home_accounting.entity.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionRequestDTO(

        @NotNull(message = "Turi bo'sh bo'lmasligi kerak")
        TransactionType type,

        @NotNull(message = "Kategoriya bo'sh bo'lmasligi kerak")
        Long categoryId,

        @NotNull(message = "Summa bo'sh bo'lmasligi kerak")
        @DecimalMin(value = "0.01", message = "summa 0 dan katta bo'lishi kerak")
        BigDecimal amount,

        String comment,

        LocalDateTime date

) {}
