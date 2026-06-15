package home_accounting.security;


import home_accounting.dto.request.TransactionRequestDTO;
import home_accounting.dto.response.TransactionResponseDTO;
import home_accounting.entity.Category;
import home_accounting.entity.Transaction;
import home_accounting.entity.User;
import home_accounting.entity.enums.TransactionType;
import home_accounting.repository.CategoryRepository;
import home_accounting.repository.TransactionRepository;
import home_accounting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi: " + username));
    }

    private TransactionResponseDTO toDto(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getType(),
                transaction.getCategory().getName(),
                transaction.getCategory().getId(),
                transaction.getAmount(),
                transaction.getComment(),
                transaction.getDate(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }

    public List<TransactionResponseDTO> getAllByUser(String username) {
        User user = getUser(username);

        return transactionRepository.findByUserIdOrderByDateDesc(user.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    public TransactionResponseDTO getById(Long id, String username) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Taranzaksiya topilmadi: " + id));

        if (!transaction.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Sizga ruxsat yo'q");
        }
        return toDto(transaction);
    }

    public TransactionResponseDTO create(TransactionRequestDTO request, String username) {
        User user = getUser(username);

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException("Kategoriya topilmadi: " + request.categoryId()));

        Transaction transaction = Transaction.builder()
                .user(user)
                .category(category)
                .type(request.type())
                .amount(request.amount())
                .comment(request.comment())
                .date(request.date() != null ? request.date() : LocalDateTime.now())
                .build();

        return toDto(transactionRepository.save(transaction));
    }

    public TransactionResponseDTO update(Long id, TransactionRequestDTO request, String username) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tranzaksiya topilmadi: " + id));

        if (!transaction.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Sizga ruxsat yo'q");
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException("Kategoriya topilmadi: " + request.categoryId()));

        transaction.setCategory(category);
        transaction.setType(request.type());
        transaction.setAmount(request.amount());
        transaction.setComment(request.comment());
        if (request.date() != null) {
            transaction.setDate(request.date());
        }
        return toDto(transactionRepository.save(transaction));
    }

    public void delete(Long id, String username) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tranzaksiya topilmadi: " + id));

        if (!transaction.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Sizga ruxsat yo'q");
        }
        transactionRepository.delete(transaction);
    }

    public List<TransactionResponseDTO> filterByType(String username, TransactionType type) {
        User user = getUser(username);
        return transactionRepository.findByUserIdAndTypeOrderByDateDesc(user.getId(), type)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<TransactionResponseDTO> filterByCategories(String username, List<Long> categoryIds) {
        User user = getUser(username);
        return transactionRepository.findByUserIdAndCategoryIdInOrderByDateDesc(user.getId(), categoryIds)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<TransactionResponseDTO> filterByDateRange(String username, LocalDateTime from, LocalDateTime to) {
        User user = getUser(username);
        return transactionRepository.findByUserIdAndDateBetweenOrderByDateDesc(user.getId(), from, to)
                .stream()
                .map(this::toDto)
                .toList();
    }



}
