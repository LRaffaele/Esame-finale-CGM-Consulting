package it.cgmconsulting.raffaele.repository;

import it.cgmconsulting.raffaele.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
