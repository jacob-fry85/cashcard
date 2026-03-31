package example.cashcard.service;

import example.cashcard.model.CashCard;

public interface CashCardService {
    CashCard findById(Long id);
}
