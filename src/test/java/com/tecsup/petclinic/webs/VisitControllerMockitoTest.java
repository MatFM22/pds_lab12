package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.domain.VisitTO;
import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.services.VisitService;
import com.tecsup.petclinic.mapper.VisitMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VisitController.class)
@AutoConfigureMockMvc(addFilters = false)
public class VisitControllerMockitoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VisitService visitService;

    @MockBean
    private VisitMapper visitMapper;  // Agregar el mock para VisitMapper

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetVisitById() throws Exception {
        Visit visit = new Visit();
        visit.setId(1L);
        visit.setPetId(1L);
        visit.setVisitDate(Date.valueOf("2024-11-07"));
        visit.setDescription("Consulta de prueba");

        VisitTO visitTO = new VisitTO(1, 1L, "2024-11-07", "Consulta de prueba");

        when(visitService.findById(1L)).thenReturn(visit);
        when(visitMapper.toVisitTO(visit)).thenReturn(visitTO);

        mockMvc.perform(get("/visits/1"))  // Actualizado a /visits/1
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Consulta de prueba")));
    }

    @Test
    public void testCreateVisit() throws Exception {
        VisitTO visitTO = new VisitTO(1, 1L, "2024-11-07", "Nueva visita");
        Visit visit = new Visit();
        visit.setId(1L);
        visit.setPetId(1L);
        visit.setVisitDate(Date.valueOf("2024-11-07"));
        visit.setDescription("Nueva visita");

        when(visitMapper.toVisit(any(VisitTO.class))).thenReturn(visit);
        when(visitService.create(any(Visit.class))).thenReturn(visit);
        when(visitMapper.toVisitTO(visit)).thenReturn(visitTO);

        mockMvc.perform(post("/visits")  // Actualizado a /visits
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(visitTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.description", is("Nueva visita")));
    }

    @Test
    public void testUpdateVisit() throws Exception {
        Visit visit = new Visit();
        visit.setId(1L);
        visit.setPetId(1L);
        visit.setVisitDate(Date.valueOf("2024-11-07"));
        visit.setDescription("Consulta actualizada");

        VisitTO visitTO = new VisitTO(2, 1L, "2024-11-07", "Consulta actualizada");

        // Configuración del comportamiento de los mocks
        when(visitService.findById(1L)).thenReturn(visit);  // Aseguramos que findById devuelva un Visit
        when(visitService.update(any(Visit.class))).thenReturn(visit);  // Configuramos update para devolver el Visit actualizado
        when(visitMapper.toVisitTO(visit)).thenReturn(visitTO);

        // Ejecutar la solicitud PUT y verificar
        mockMvc.perform(put("/visits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(visitTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Consulta actualizada")));
    }


    @Test
    public void testDeleteVisit() throws Exception {
        doNothing().when(visitService).delete(1L);

        mockMvc.perform(delete("/visits/1"))  // Actualizado a /visits/1
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted Visit ID: 1"));
    }
}




