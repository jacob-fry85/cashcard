package example.cashcard.controller;

import example.cashcard.model.CashCard;
import example.cashcard.repository.CashCardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {
    private final CashCardRepository cashCardRepository;

    public CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<CashCard> findById(@PathVariable Long requestedId, Principal principal) {
        return cashCardRepository.findByIdAndOwner(requestedId, principal.getName())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createCashCard(@RequestBody CashCard cashCard,
                                                   UriComponentsBuilder ucb,
                                                   Principal principal) {
        if (cashCard.id() != null) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "error", "InvalidRequest",
                            "message", "id must be null when creating a new CashCard"
                    ));
        }
        CashCard newCashCard = new CashCard(null, cashCard.amount(), principal.getName());
        CashCard saved = cashCardRepository.save(newCashCard);
        URI location = ucb
                .path("/cashcards/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<CashCard>> findAll(
            @PageableDefault(
                    sort = "amount",
                    direction = Sort.Direction.ASC
            ) Pageable pageable,
            Principal principal) {

        Page<CashCard> page = cashCardRepository.findByOwner(
                principal.getName(),
                pageable
        );

        return ResponseEntity.ok(page.getContent());
    }

    @PutMapping("/{requestedId}")
    public ResponseEntity<Void> updateCashCard(@PathVariable Long requestedId,
                                            @RequestBody CashCard cashCard,
                                            Principal principal) {
        Optional<CashCard> optionalCashCard =
                cashCardRepository.findByIdAndOwner(requestedId, principal.getName());
        if(optionalCashCard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CashCard existingCashCard = optionalCashCard.get();

        CashCard updatedCashCard = new CashCard(
                requestedId,
                cashCard.amount(),
                existingCashCard.owner()
        );

        cashCardRepository.save(updatedCashCard);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{requestedId}")
    public ResponseEntity<Void> deleteCashCard(@PathVariable Long requestedId,
                                               Principal principal) {
        int deleted = cashCardRepository
                .deleteByIdAndOwner(requestedId, principal.getName());

        if (deleted == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
