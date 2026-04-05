package example.cashcard.repository;

import example.cashcard.model.CashCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashCardRepository
        extends CrudRepository<CashCard, Long>,
        PagingAndSortingRepository<CashCard, Long> {
}
