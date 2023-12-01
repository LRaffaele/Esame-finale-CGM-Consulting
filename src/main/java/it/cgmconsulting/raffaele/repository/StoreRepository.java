package it.cgmconsulting.raffaele.repository;

import it.cgmconsulting.raffaele.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

    boolean existsByStoreName(String storeName);

}
