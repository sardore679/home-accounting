package home_accounting.dto.response;

import home_accounting.entity.enums.TransactionType;

import java.time.LocalDateTime;

public record CategoryResponseDTO(

        Long id,
        String name,
        TransactionType type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {}
