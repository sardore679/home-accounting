package home_accounting.controller;


import home_accounting.dto.request.TransactionRequestDTO;
import home_accounting.dto.response.TransactionResponseDTO;
import home_accounting.entity.enums.TransactionType;
import home_accounting.security.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAll(Authentication auth) {
        return ResponseEntity.ok(transactionService.getAllByUser(auth.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getById(@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(transactionService.getById(id, auth.getName()));
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> create(@Valid @RequestBody TransactionRequestDTO request, Authentication auth) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionService.create(request, auth.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> update(@PathVariable Long id, @Valid @RequestBody TransactionRequestDTO request, Authentication auth) {
        return ResponseEntity.ok(transactionService.update(id, request, auth.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        transactionService.delete(id, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter/type")
    public ResponseEntity<List<TransactionResponseDTO>> filterByType(
            @RequestParam TransactionType type,
            Authentication auth) {
        return ResponseEntity.ok(transactionService.filterByType(auth.getName(), type));
    }


    @GetMapping("/filter/categories")
    public ResponseEntity<List<TransactionResponseDTO>> filterByCategories(
            @RequestParam List<Long> categoryIds,
            Authentication auth) {
        return ResponseEntity.ok(transactionService.filterByCategories(auth.getName(), categoryIds));
    }

    @GetMapping("/filter/date")
    public ResponseEntity<List<TransactionResponseDTO>> filterByDateRange(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to,
            Authentication auth) {
        return ResponseEntity.ok(transactionService.filterByDateRange(auth.getName(), from, to));
    }

}
