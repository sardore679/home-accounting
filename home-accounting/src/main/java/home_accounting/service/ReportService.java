package home_accounting.service;


import home_accounting.dto.response.ReportResponseDTO;
import home_accounting.entity.Transaction;
import home_accounting.entity.User;
import home_accounting.entity.enums.TransactionType;
import home_accounting.repository.TransactionRepository;
import home_accounting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));
    }

    public ReportResponseDTO getReport(
            String username,
            TransactionType type,
            List<Long> categoryIds,
            LocalDateTime from,
            LocalDateTime to
    ) {
        User user = getUser(username);

        List<Transaction> transactions = transactionRepository
                .findByUserIdOrderByDateDesc(user.getId());

        if (type != null) {
            transactions = transactions.stream()
                    .filter(t -> t.getType() == type)
                    .toList();
        }

        if (categoryIds != null && !categoryIds.isEmpty()) {
            transactions = transactions.stream()
                    .filter(t -> categoryIds.contains(t.getCategory().getId()))
                    .toList();
        }

        if (from != null) {
            transactions = transactions.stream()
                    .filter(t -> !t.getDate().isBefore(from))
                    .toList();
        }

        if (to != null) {
            transactions = transactions.stream()
                    .filter(t -> !t.getDate().isAfter(to))
                    .toList();
        }

        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalIncome.subtract(totalExpense);

        final List<Transaction> res = transactions;
        List<ReportResponseDTO.CategorySummary> categorySummaries = transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().getId(),
                        Collectors.reducing(BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add)
                ))
                .entrySet().stream()
                .map(entry -> {
                    Transaction sample = res.stream()
                            .filter(t -> t.getCategory().getId().equals(entry.getKey()))
                            .findFirst()
                            .orElseThrow();

                    return new ReportResponseDTO.CategorySummary(
                            entry.getKey(),
                            sample.getCategory().getName(),
                            sample.getType().name(),
                            entry.getValue()
                    );
                }).toList();

        return new ReportResponseDTO(
                totalIncome,
                totalExpense,
                balance,
                categorySummaries
        );

    }

}
