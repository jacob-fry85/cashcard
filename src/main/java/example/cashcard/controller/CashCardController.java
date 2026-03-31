package example.cashcard.controller;

import example.cashcard.model.CashCard;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CashCardController {

    @GetMapping("/cashcards/{requestedId}")
    public ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {
        CashCard cashCard = new CashCard(requestedId, .99);
        return ResponseEntity.ok(cashCard);
    }
}
