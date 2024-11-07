package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.domain.VisitTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for VisitController.
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VisitControllerTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testFindAllVisits() throws Exception {
		int ID_FIRST_RECORD = 1;

		this.mockMvc.perform(get("/visits"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
	}

	@Test
	public void testFindVisitOK() throws Exception {
		int VISIT_ID = 1;
		int PET_ID = 1;
		String VISIT_DATE = "2024-11-07";
		String DESCRIPTION = "Routine checkup";

		mockMvc.perform(get("/visits/" + VISIT_ID))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(VISIT_ID)))
				.andExpect(jsonPath("$.petId", is(PET_ID)))
				.andExpect(jsonPath("$.visitDate", is(VISIT_DATE)))
				.andExpect(jsonPath("$.description", is(DESCRIPTION)));
	}

	@Test
	public void testFindVisitKO() throws Exception {
		mockMvc.perform(get("/visits/9999"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testCreateVisit() throws Exception {
		int PET_ID = 1;
		String VISIT_DATE = "2024-11-07";
		String DESCRIPTION = "Routine visit";

		VisitTO newVisitTO = new VisitTO();
		newVisitTO.setPetId(PET_ID);
		newVisitTO.setVisitDate(VISIT_DATE);
		newVisitTO.setDescription(DESCRIPTION);

		mockMvc.perform(post("/visits")
						.content(om.writeValueAsString(newVisitTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.petId", is(PET_ID)))
				.andExpect(jsonPath("$.visitDate", is(VISIT_DATE)))
				.andExpect(jsonPath("$.description", is(DESCRIPTION)));
	}

	@Test
	public void testDeleteVisit() throws Exception {
		int PET_ID = 1;
		String VISIT_DATE = "2024-11-07";
		String DESCRIPTION = "Temporary visit";

		VisitTO newVisitTO = new VisitTO();
		newVisitTO.setPetId(PET_ID);
		newVisitTO.setVisitDate(VISIT_DATE);
		newVisitTO.setDescription(DESCRIPTION);

		// CREATE
		ResultActions mvcActions = mockMvc.perform(post("/visits")
						.content(om.writeValueAsString(newVisitTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		// DELETE
		mockMvc.perform(delete("/visits/" + id))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteVisitKO() throws Exception {
		mockMvc.perform(delete("/visits/9999"))
				.andExpect(status().isNotFound());
	}
}
