VisitServiceMockitoTest

        package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.repositories.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VisitServiceMockitoTest {

    @Mock
    private VisitRepository visitRepository;

    @InjectMocks
    private VisitServiceImpl visitService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateVisit() {
        Visit visit = new Visit();
        visit.setPetId(1L);
        visit.setVisitDate(new java.sql.Date(new Date().getTime())); // Cambiado para evitar el ClassCastException
        visit.setDescription("Visita de prueba");

        when(visitRepository.save(any(Visit.class))).thenReturn(visit);

        Visit savedVisit = visitService.create(visit);
        assertNotNull(savedVisit);
        assertEquals("Visita de prueba", savedVisit.getDescription());
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        Visit visit = new Visit();
        visit.setId(id);
        visit.setDescription("Consulta médica");

        when(visitRepository.findById(id)).thenReturn(Optional.of(visit));

        Visit foundVisit = visitService.findById(id);
        assertNotNull(foundVisit);
        assertEquals("Consulta médica", foundVisit.getDescription());
    }

    @Test
    public void testUpdateVisit() {
        Visit visit = new Visit();
        visit.setId(1L);
        visit.setDescription("Control");

        when(visitRepository.save(any(Visit.class))).thenReturn(visit);

        Visit updatedVisit = visitService.update(visit);
        assertNotNull(updatedVisit);
        assertEquals("Control", updatedVisit.getDescription());
    }

    @Test
    public void testDeleteVisit() {
        Long id = 1L;
        Visit visit = new Visit();
        visit.setId(id);

        // Simulando que el Visit existe antes de la eliminación
        when(visitRepository.findById(id)).thenReturn(Optional.of(visit));
        doNothing().when(visitRepository).deleteById(id);

        visitService.delete(id);

        verify(visitRepository, times(1)).deleteById(id);
    }
}