package tn.esprit.autoloc.payment.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.payment.entity.PaymentMethod;
import tn.esprit.autoloc.payment.repository.PaymentMethodRepository;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodRepository repository;

    public PaymentMethodController(PaymentMethodRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<PaymentMethod> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public PaymentMethod findById(@PathVariable Long id) {
        return getMethod(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentMethod create(@Valid @RequestBody PaymentMethod method) {
        method.setId(null);
        return repository.save(method);
    }

    @PutMapping("/{id}")
    public PaymentMethod update(@PathVariable Long id, @Valid @RequestBody PaymentMethod input) {
        PaymentMethod method = getMethod(id);
        method.setName(input.getName());
        method.setType(input.getType());
        method.setActive(input.isActive());
        return repository.save(method);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repository.delete(getMethod(id));
    }

    private PaymentMethod getMethod(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mode de paiement introuvable"));
    }
}
