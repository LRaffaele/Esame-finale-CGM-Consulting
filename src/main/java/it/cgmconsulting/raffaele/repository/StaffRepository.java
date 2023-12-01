package it.cgmconsulting.raffaele.repository;

import it.cgmconsulting.raffaele.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Long> {
}
