package it.cgmconsulting.raffaele.repository;

import it.cgmconsulting.raffaele.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
