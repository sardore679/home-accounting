package home_accounting.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record ReportResponseDTO(

        BigDecimal totalIncome,

        BigDecimal totalExpense,

        BigDecimal balance,

        List<CategorySummary> categorySummaries

) {
    public record CategorySummary(
      Long categoryId,
      String categoryName,
      String type,
      BigDecimal totalAmount
    ){}
}
