package tn.esprit.autoloc.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
