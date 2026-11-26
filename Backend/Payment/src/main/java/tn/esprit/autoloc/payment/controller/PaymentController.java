package tn.esprit.autoloc.payment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
import tn.esprit.autoloc.payment.entity.Payment;
import tn.esprit.autoloc.payment.entity.PaymentMethod;
import tn.esprit.autoloc.payment.entity.PaymentStatus;
import tn.esprit.autoloc.payment.repository.PaymentMethodRepository;
import tn.esprit.autoloc.payment.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentRepository payments;
    private final PaymentMethodRepository methods;

    public PaymentController(PaymentRepository payments, PaymentMethodRepository methods) {
        this.payments = payments;
        this.methods = methods;
    }

    @GetMapping
    public List<Payment> findAll() {
        return payments.findAll();
    }

    @GetMapping("/{id}")
    public Payment findById(@PathVariable Long id) {
        return getPayment(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Payment create(@Valid @RequestBody PaymentRequest request) {
        Payment payment = new Payment();
        apply(payment, request);
        return payments.save(payment);
    }

    @PutMapping("/{id}")
    public Payment update(@PathVariable Long id, @Valid @RequestBody PaymentRequest request) {
        Payment payment = getPayment(id);
        apply(payment, request);
        return payments.save(payment);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        payments.delete(getPayment(id));
    }

    private void apply(Payment payment, PaymentRequest request) {
        PaymentMethod method = methods.findById(request.paymentMethodId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mode de paiement introuvable"));
        if (!method.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le mode de paiement est désactivé");
        }
        payment.setAmount(request.amount());
        payment.setCurrency(request.currency());
        payment.setStatus(request.status() == null ? PaymentStatus.PENDING : request.status());
        payment.setTransactionReference(request.transactionReference());
        payment.setPaymentMethod(method);
    }

    private Payment getPayment(Long id) {
        return payments.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paiement introuvable"));
    }

    public record PaymentRequest(
            @NotNull @DecimalMin("0.01") BigDecimal amount,
            @NotBlank @Pattern(regexp = "[A-Z]{3}") String currency,
            PaymentStatus status,
            @Size(max = 100) String transactionReference,
            @NotNull Long paymentMethodId) {
    }
}
