package tn.esprit.autoloc.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.payment.entity.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
}
