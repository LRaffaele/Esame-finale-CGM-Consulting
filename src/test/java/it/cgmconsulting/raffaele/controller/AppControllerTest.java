package it.cgmconsulting.raffaele.controller;

import it.cgmconsulting.raffaele.service.FilmService;
import it.cgmconsulting.raffaele.service.InventoryService;
import it.cgmconsulting.raffaele.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;
@RequiredArgsConstructor
class AppControllerTest {

    final AppController appController;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
        System.out.println("sono in tearDown");
    }

    @Test
    void testFindAllFilmsRentedByOneCustomer() {

    }

    @Test
    void test3() {
        System.out.println("test nuovo");
    }
}