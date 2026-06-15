package home_accounting.dto.response;

import home_accounting.entity.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDTO(

        Long id,
        TransactionType type,
        String categoryName,
        Long categoryId,
        BigDecimal amount,
        String comment,
        LocalDateTime date,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {}
