package example.cashcard.controller;

import example.cashcard.model.CashCard;
import example.cashcard.repository.CashCardRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {
    private final CashCardRepository cashCardRepository;

    private CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {
        return cashCardRepository.findById(requestedId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createCashCard(@RequestBody CashCard cashCard,
                                                   UriComponentsBuilder ucb) {
        if (cashCard.id() != null) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "error", "InvalidRequest",
                            "message", "id must be null when creating a new CashCard"
                    ));
        }

        CashCard saved = cashCardRepository.save(cashCard);
        URI location = ucb
                .path("/cashcards/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping
    private ResponseEntity<Iterable<CashCard>> findAll() {
        return ResponseEntity.ok(cashCardRepository.findAll());
    }

}
