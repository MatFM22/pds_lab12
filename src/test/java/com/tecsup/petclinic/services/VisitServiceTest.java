VisitServiceTest

        package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Visit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VisitServiceTest {

    @Autowired
    private VisitService visitService;

    private Visit savedVisit;

    @BeforeEach
    public void setUp() {
        Visit visit = new Visit();
        visit.setPetId(1L);
        visit.setVisitDate(Date.valueOf("2024-11-07"));
        visit.setDescription("Visita de prueba");
        savedVisit = visitService.create(visit); // Crear y guardar un Visit para usar en las pruebas
    }

    @Test
    public void testCreateVisit() {
        assertNotNull(savedVisit);
        assertNotNull(savedVisit.getId());
        assertEquals("Visita de prueba", savedVisit.getDescription());
    }

    @Test
    public void testFindById() {
        Visit foundVisit = visitService.findById(savedVisit.getId());
        assertNotNull(foundVisit);
        assertEquals(savedVisit.getId(), foundVisit.getId());
    }

    @Test
    public void testUpdateVisit() {
        savedVisit.setDescription("Consulta actualizada");
        Visit updatedVisit = visitService.update(savedVisit);
        assertNotNull(updatedVisit);
        assertEquals("Consulta actualizada", updatedVisit.getDescription());
    }

    @Test
    public void testDeleteVisit() {
        visitService.delete(savedVisit.getId());
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> visitService.findById(savedVisit.getId()));
        assertEquals("Visit not found...!", thrown.getMessage());
    }
}