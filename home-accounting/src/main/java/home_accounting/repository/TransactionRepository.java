package home_accounting.repository;

import home_accounting.entity.Transaction;
import home_accounting.entity.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserIdOrderByDateDesc(Long id);

    List<Transaction> findByUserIdAndTypeOrderByDateDesc(Long userId, TransactionType type);

    List<Transaction> findByUserIdAndCategoryIdInOrderByDateDesc(Long userId, List<Long> categoryIds);

    List<Transaction> findByUserIdAndDateBetweenOrderByDateDesc(Long userId, LocalDateTime from, LocalDateTime to);
}
