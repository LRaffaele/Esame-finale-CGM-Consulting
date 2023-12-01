package it.cgmconsulting.raffaele.repository;

import it.cgmconsulting.raffaele.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    Optional<Language> findByLanguageName(String languageName);

}
