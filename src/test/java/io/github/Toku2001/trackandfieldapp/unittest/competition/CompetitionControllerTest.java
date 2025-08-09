package io.github.Toku2001.trackandfieldapp.unittest.competition;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.github.Toku2001.trackandfieldapp.dto.competition.CompetitionResponse;
import io.github.Toku2001.trackandfieldapp.dto.competition.RegisterCompetitionRequest;
import io.github.Toku2001.trackandfieldapp.dto.user.UserDetailsForToken;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.repository.CompetitionMapper;
import io.github.Toku2001.trackandfieldapp.service.competition.ChangeCompetitionService;
import io.github.Toku2001.trackandfieldapp.service.competition.CompetitionService;
import io.github.Toku2001.trackandfieldapp.service.competition.DeleteCompetitionService;
import io.github.Toku2001.trackandfieldapp.service.competition.RegisterCompetitionService;
import io.github.Toku2001.trackandfieldapp.service.competition.impl.RegisterCompetitionServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc  
@WithMockUser(username = "testuser", roles = {"USER"})
@Transactional
@MapperScan("io.github.Toku2001.trackandfieldapp.repository") 
public class CompetitionControllerTest {
	
	@Autowired
    MockMvc mockMvc;

    @MockBean
    RegisterCompetitionService registerCompetitionService;
    
    @MockBean
    ChangeCompetitionService changeCompetitionService;

    @MockBean
    DeleteCompetitionService deleteCompetitionService;

    @MockBean
    CompetitionService competitionService;
    
	@Mock
	private CompetitionMapper competitionMapper;

	@InjectMocks
	private RegisterCompetitionServiceImpl registerCompetitionServiceImpl;

    String baseUrl = "/api/register-competition";

    private String createJson(String name, String place, String time, String comments) {
        return String.format("""
            {
                "competitionName": %s,
                "competitionPlace": %s,
                "competitionTime": %s,
                "competitionComments": %s
            }
            """,
            name != null ? "\"" + name + "\"" : null,
            place != null ? "\"" + place + "\"" : null,
            time != null ? "\"" + time + "\"" : null,
            comments != null ? "\"" + comments + "\"" : null
        );
    }
    
  @BeforeEach
  void setUp() {
      UserDetailsForToken userDetails = new UserDetailsForToken(
          1L,
          "testuser",
          List.of(new SimpleGrantedAuthority("ROLE_USER"))
      );

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authentication);
  }

    @Test
    void competitionName_isBlank_shouldReturn400() throws Exception {
    	when(registerCompetitionService.registerCompetition(any())).thenReturn(1);
        String json = createJson("", "競技場", "2025-07-27", "コメント");
        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("競技会名は必須です"));
    }

    @Test
    void competitionName_isTooLong_shouldReturn400() throws Exception {
    	when(registerCompetitionService.registerCompetition(any())).thenReturn(1);
        String json = createJson("あ".repeat(33), "競技場", "2025-07-27", "コメント");
        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("競技会名は32文字以内で記入してください"));
    }

    @Test
    void competitionPlace_isBlank_shouldReturn400() throws Exception {
    	when(registerCompetitionService.registerCompetition(any())).thenReturn(1);
        String json = createJson("大会名", "", "2025-07-27", "コメント");
        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("競技会開催場所は必須です"));
    }

    @Test
    void competitionPlace_isTooLong_shouldReturn400() throws Exception {
    	when(registerCompetitionService.registerCompetition(any())).thenReturn(1);
        String json = createJson("大会名", "い".repeat(33), "2025-07-27", "コメント");
        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("競技会開催場所は32文字以内で記入してください"));
    }

    @Test
    void competitionTime_isNull_shouldReturn400() throws Exception {
    	when(registerCompetitionService.registerCompetition(any())).thenReturn(1);
        String json = createJson("大会名", "競技場", null, "コメント");
        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("競技会時間は必須です"));
    }

    @Test
    void competitionComments_isBlank_shouldReturn400() throws Exception {
    	when(registerCompetitionService.registerCompetition(any())).thenReturn(1);
        String json = createJson("大会名", "競技場", "2025-07-27", "");
        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("競技会コメントは必須です"));
    }

    @Test
    void competitionComments_isTooLong_shouldReturn400() throws Exception {
    	when(registerCompetitionService.registerCompetition(any())).thenReturn(1);
        String json = createJson("大会名", "競技場", "2025-07-27", "う".repeat(256));
        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("競技会コメントは255文字以内で記入してください"));
    }

    @Test
    void validRequest_shouldReturn200() throws Exception {
    	when(registerCompetitionService.registerCompetition(any())).thenReturn(1);
        String json = createJson("大会名", "競技場", "2025-07-27", "コメント");
        when(registerCompetitionService.registerCompetition(any())).thenReturn(1);

        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isOk())
            .andExpect(content().string("1"));
    }

    @Test
    void registerCompetition_Succeeds_Service() throws Exception {
 
        String competitionName = "大会";
        String competitionPlace = "競技場";
        LocalDate competitionTime = LocalDate.of(2025, 07, 27);
        String competitionComments = "世界新記録";
        
        RegisterCompetitionRequest registerCompetitionRequest = new RegisterCompetitionRequest(competitionName, competitionPlace, competitionTime, competitionComments);

        when(competitionMapper.registerCompetition(anyLong(), any(), any(), any(LocalDate.class), any()))
                .thenReturn(1);

        int result = registerCompetitionService.registerCompetition(registerCompetitionRequest);
        assertEquals(1, result);
    }

    @Test
    void registerCompetition_Conflict() throws Exception {
        RegisterCompetitionRequest request = new RegisterCompetitionRequest();
        request.setCompetitionTime(LocalDate.of(2025, 7, 27));
        request.setCompetitionPlace("競技場");
        request.setCompetitionComments("コメント");
        request.setCompetitionName("試合");

        when(registerCompetitionService.registerCompetition(any(RegisterCompetitionRequest.class)))
            .thenThrow(new IllegalStateException("この日付の競技会情報は既に登録されています。"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc.perform(post("/api/register-competition")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error").value("この日付の競技会情報は既に登録されています。"));
    }
    
    //PUT
    @Test
    void changeCompetition_validInput_shouldReturn200() throws Exception {
        when(changeCompetitionService.changeCompetition(any())).thenReturn(1);

        String json = """
            {
                "competitionName": "大会",
                "competitionPlace": "競技場",
                "competitionTime": "2025-07-28",
                "competitionComments": "コメント"
            }
            """;

        mockMvc.perform(put("/api/change-competition")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(content().string("1"));
    }
    
    @Test
    void changeCompetition_invalid_shouldReturn400() throws Exception {
        when(changeCompetitionService.changeCompetition(any())).thenReturn(0); // 失敗

        String json = """
            {
                "competitionName": "大会",
                "competitionPlace": "競技場",
                "competitionTime": "2025-07-28",
                "competitionComments": "コメント"
            }
            """;

        mockMvc.perform(put("/api/change-competition")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest())
            .andExpect(status().reason("Invalid credentials"));
    }
    
    //DELETE
    @Test
    void deleteCompetition_validInput_shouldReturn200() throws Exception {
        when(deleteCompetitionService.deleteCompetition(LocalDate.of(2025, 7, 27))).thenReturn(1);

        mockMvc.perform(delete("/api/delete-competition")
                .param("competitionDate", "2025-07-27")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("1"));
    }

    @Test
    void deleteCompetition_shouldReturn401_whenServiceReturns0() throws Exception {
        // サービスが0を返すようにモック
        when(deleteCompetitionService.deleteCompetition(any(LocalDate.class)))
            .thenReturn(0);

        mockMvc.perform(delete("/api/delete-competition")
                .param("competitionDate", "2025-07-28"))
            .andExpect(status().isUnauthorized()) // 401
            .andExpect(status().reason("Invalid credentials"));
    }
    
    //GET
    @Test
    void getCompetitionByDate_valid_shouldReturn200() throws Exception {
        CompetitionResponse response = new CompetitionResponse(1, "大会", "競技場", LocalDate.of(2025, 7, 28), "コメント");
        when(competitionService.getCompetitionByDate(any())).thenReturn(response);

        mockMvc.perform(get("/api/get-competition")
                .param("competitionDate", "2025-07-28"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.competitionId").value(1))
            .andExpect(jsonPath("$.competitionName").value("大会"))
            .andExpect(jsonPath("$.competitionPlace").value("競技場"))
            .andExpect(jsonPath("$.competitionTime").value("2025-07-28"))
            .andExpect(jsonPath("$.competitionComments").value("コメント"));
    }
    
    @Test
    void getCompetitionByDate_notFound_shouldReturn500() throws Exception {
        when(competitionService.getCompetitionByDate(any())).thenReturn(null);

        mockMvc.perform(get("/api/get-competition")
                .param("competitionDate", "2025-07-28"))
	        .andExpect(status().isInternalServerError())
	        .andExpect(jsonPath("$.error").value("コントローラにデータが届いていません。"));
    }
    
    //次の大会情報を取得
    @Test
    void getNextCompetition_valid_shouldReturn200() throws Exception {
        CompetitionResponse response = new CompetitionResponse(1, "大会", "競技場", LocalDate.of(2025, 7, 28), "コメント");
        when(competitionService.getNextCompetition()).thenReturn(response);

        mockMvc.perform(get("/api/get-competition-upcoming"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.competitionName").value("大会"))
            .andExpect(jsonPath("$.competitionId").value(1))
            .andExpect(jsonPath("$.competitionPlace").value("競技場"))
            .andExpect(jsonPath("$.competitionTime").value("2025-07-28"))
            .andExpect(jsonPath("$.competitionComments").value("コメント"));
    }
    
    @Test
    void getNextCompetition_notFound_shouldReturn500() throws Exception {
        when(competitionService.getNextCompetition()).thenReturn(null);

        mockMvc.perform(get("/api/get-competition-upcoming"))
	        .andExpect(status().isInternalServerError())
	        .andExpect(jsonPath("$.error").value("コントローラにデータが届いていません。"));
    }
}