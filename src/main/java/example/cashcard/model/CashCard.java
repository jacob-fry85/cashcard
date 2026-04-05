package example.cashcard.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("CASH_CARD")
public record CashCard(
        @Id Long id,
        Double amount,
        String owner
) {}
