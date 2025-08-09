package io.github.Toku2001.trackandfieldapp.unittest.jump;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import io.github.Toku2001.trackandfieldapp.dto.competition.CompetitionResponse;
import io.github.Toku2001.trackandfieldapp.dto.jump.PersonalBestResponse;
import io.github.Toku2001.trackandfieldapp.service.competition.CompetitionService;
import io.github.Toku2001.trackandfieldapp.service.jump.ChangeJumpService;
import io.github.Toku2001.trackandfieldapp.service.jump.DeleteJumpService;
import io.github.Toku2001.trackandfieldapp.service.jump.JumpService;
import io.github.Toku2001.trackandfieldapp.service.jump.RegisterJumpService;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testuser", roles = {"USER"})
public class JumpTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RegisterJumpService registerJumpService;

	@MockBean
	private DeleteJumpService deleteJumpService;

	@MockBean
	private ChangeJumpService changeJumpService;

	@MockBean
	private CompetitionService competitionService;

	@MockBean
	private JumpService jumpService;

	// --- /register-jump-records ---
	@Test
	void registerJumpRecords_Success() throws Exception {
		String json = """
		[
			{
				"eventCode": "PV",
				"jumpDistance": 5.10,
				"approachSteps": 16,
				"poleLength": 4.85,
				"poleStiffness": 18.2,
				"recordType": "official",
				"jumpDate": "2025-07-27"
			}
		]
		""";
		Mockito.doNothing().when(registerJumpService).registerJump(anyList());

		mockMvc.perform(post("/api/register-jump-records")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.result").value(1));
	}

	@Test
	void registerJumpRecords_EmptyList() throws Exception {
		String json = "[]";
		Mockito.doNothing().when(registerJumpService).registerJump(anyList());

		mockMvc.perform(post("/api/register-jump-records")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.result").value(1));
	}

	// --- /delete-jump-record ---
	@Test
	void deleteJumpRecord_Success() throws Exception {
		when(deleteJumpService.deleteById(eq(1L))).thenReturn(1);

		mockMvc.perform(delete("/api/delete-jump-record")
				.param("jumpEventId", "1"))
			.andExpect(status().isNoContent());
	}

	@Test
	void deleteJumpRecord_NotFound() throws Exception {
		when(deleteJumpService.deleteById(eq(999L))).thenReturn(0);

		mockMvc.perform(delete("/api/delete-jump-record")
				.param("jumpEventId", "999"))
			.andExpect(status().isNotFound());
	}

	// --- /delete-jump-records-by-date ---
	@Test
	void deleteJumpRecordsByDate_Success() throws Exception {
		when(deleteJumpService.deleteAllByDate(eq("2025-07-27"))).thenReturn(2);

		mockMvc.perform(delete("/api/delete-jump-records-by-date")
				.param("jumpDate", "2025-07-27"))
			.andExpect(status().isNoContent());
	}

	@Test
	void deleteJumpRecordsByDate_NotFound() throws Exception {
		when(deleteJumpService.deleteAllByDate(eq("2025-07-27"))).thenReturn(0);

		mockMvc.perform(delete("/api/delete-jump-records-by-date")
				.param("jumpDate", "2025-07-27"))
			.andExpect(status().isNotFound());
	}

	// --- /change-jump-records ---
	@Test
	void changeJumpRecords_Success() throws Exception {
		String json = """
		[
			{
				"jumpEventId": 1,
				"eventCode": "PV",
				"jumpDistance": 5.20,
				"approachSteps": 16,
				"poleLength": 4.85,
				"poleStiffness": 18.2,
				"recordType": "official",
				"jumpDate": "2025-07-27"
			}
		]
		""";
		when(changeJumpService.changeJump(anyList())).thenReturn(1);

		mockMvc.perform(put("/api/change-jump-records")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isOk())
			.andExpect(content().string("1"));
	}

	@Test
	void changeJumpRecords_Failure() throws Exception {
		String json = """
		[
			{
				"jumpEventId": 1,
				"eventCode": "PV",
				"jumpDistance": 5.20,
				"approachSteps": 16,
				"poleLength": 4.85,
				"poleStiffness": 18.2,
				"recordType": "official",
				"jumpDate": "2025-07-27"
			}
		]
		""";
		when(changeJumpService.changeJump(anyList())).thenReturn(0);

		mockMvc.perform(put("/api/change-jump-records")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isBadRequest());
	}

	// --- /get-competition-upcoming ---
	@Test
	void getCompetitionUpcoming_Success() throws Exception {
		CompetitionResponse response = new CompetitionResponse();
		response.setCompetitionTime(LocalDate.of(2025, 8, 1));
		response.setCompetitionName("Next Meet");
		response.setCompetitionPlace("競技場");
		when(competitionService.getNextCompetition()).thenReturn(response);

		mockMvc.perform(get("/api/get-competition-upcoming"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.competitionName").value("Next Meet"))
			.andExpect(jsonPath("$.competitionPlace").value("競技場"))
			.andExpect(jsonPath("$.competitionTime").value("2025-08-01"));
	}

	@Test
	void getCompetitionUpcoming_Failure() throws Exception {
		when(competitionService.getNextCompetition()).thenReturn(null);

		mockMvc.perform(get("/api/get-competition-upcoming"))
			.andExpect(status().isInternalServerError());
	}

	// --- /get-personal-best ---
	@Test
	void getPersonalBest_Success() throws Exception {
		PersonalBestResponse pb = new PersonalBestResponse("PV", 5.30, "2025-07-27");
		when(jumpService.getPersonalBest()).thenReturn(List.of(pb));

		mockMvc.perform(get("/api/get-personal-best"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].event").value("PV"))
			.andExpect(jsonPath("$[0].distance").value(5.30))
			.andExpect(jsonPath("$[0].jumpDate").value("2025-07-27"));
	}

	@Test
	void getPersonalBest_Failure() throws Exception {
		when(jumpService.getPersonalBest()).thenReturn(null);

		mockMvc.perform(get("/api/get-personal-best"))
			.andExpect(status().isInternalServerError());
	}
}