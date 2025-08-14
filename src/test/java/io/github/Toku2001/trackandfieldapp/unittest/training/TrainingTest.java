package io.github.Toku2001.trackandfieldapp.unittest.training;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import io.github.Toku2001.trackandfieldapp.dto.training.ChangeTrainingRequest;
import io.github.Toku2001.trackandfieldapp.dto.training.RegisterTrainingRequest;
import io.github.Toku2001.trackandfieldapp.dto.training.TrainingResponse;
import io.github.Toku2001.trackandfieldapp.dto.user.UserDetailsForToken;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.repository.PasswordMapper;
import io.github.Toku2001.trackandfieldapp.repository.TrainingMapper;
import io.github.Toku2001.trackandfieldapp.service.training.ChangeTrainingService;
import io.github.Toku2001.trackandfieldapp.service.training.DeleteTrainingService;
import io.github.Toku2001.trackandfieldapp.service.training.RegisterTrainingService;
import io.github.Toku2001.trackandfieldapp.service.training.TrainingService;

@SpringBootTest
@AutoConfigureMockMvc  
@WithMockUser(username = "testuser", roles = {"USER"})
@Transactional
@MapperScan("io.github.Toku2001.trackandfieldapp.repository") 
public class TrainingTest {

	@Autowired
    private MockMvc mockMvc;

	@MockBean
    private TrainingMapper trainingMapper;

    @MockBean
    private PasswordMapper passwordMapper;
    
    @MockBean
    private TrainingService trainingService;
    
    @MockBean
    private DeleteTrainingService deleteTrainingService;

    @MockBean
    private RegisterTrainingService registerTrainingService;

    @MockBean
    private ChangeTrainingService changeTrainingService;

    @Autowired
    private ObjectMapper objectMapper;
    
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
    void registerTraining_Succeeds_WithValidInput() throws Exception {
        String json = """
            {
                "trainingTime": "2025-07-27",
                "trainingPlace": "競技場",
                "trainingComments": "問題なし"
            }
            """;

        when(registerTrainingService.registerTraining(any(RegisterTrainingRequest.class)))
            .thenReturn(1);
        
        mockMvc.perform(post("/api/register-training")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value(1));
    }
    
    @Test
    void registerTraining_Fails_WhenTrainingTimeIsBlank() throws Exception {
        String json = """
        {
            "trainingTime": "",
            "trainingPlace": "陸上競技場",
            "trainingComments": "調整練習"
        }
        """;

        mockMvc.perform(post("/api/register-training")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("練習時間は必須です"));
    }
    
    @Test
    void registerTraining_Fails_WhenTrainingPlaceIsBlank() throws Exception {
        String json = """
        {
            "trainingTime": "2025-07-27",
            "trainingPlace": "",
            "trainingComments": "調整練習"
        }
        """;

        mockMvc.perform(post("/api/register-training")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("練習場所は必須です"));
    }
    
    @Test
    void registerTraining_Fails_WhenTrainingPlaceIsTooLong() throws Exception {
        String tooLongPlace = "あ".repeat(65);
        String json = String.format("""
        {
            "trainingTime": "2025-07-27",
            "trainingPlace": "%s",
            "trainingComments": "長すぎる場所名"
        }
        """, tooLongPlace);

        mockMvc.perform(post("/api/register-training")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("練習場所は64文字以内で記入してください"));
    }
    
    @Test
    void registerTraining_Fails_WhenTrainingCommentsIsTooLong() throws Exception {
        String tooLongComment = "あ".repeat(256);
        String json = String.format("""
        {
            "trainingTime": "2025-07-27",
            "trainingPlace": "陸上競技場",
            "trainingComments": "%s"
        }
        """, tooLongComment);

        mockMvc.perform(post("/api/register-training")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("練習コメントは255文字以内で記入してください"));
    }
    
    void getTraining_Success() throws Exception {
        LocalDate date = LocalDate.of(2025, 7, 27);

        TrainingResponse mockResponse = new TrainingResponse();
        mockResponse.setTrainingTime(date);
        mockResponse.setTrainingPlace("競技場");
        mockResponse.setTrainingComments("問題なし");

        when(trainingService.getTraining(eq(date))).thenReturn(mockResponse);

        mockMvc.perform(get("/api/get-training")
                .param("trainingDate", date.toString())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.trainingPlace").value("競技場"))
            .andExpect(jsonPath("$.trainingComments").value("問題なし"))
            .andExpect(jsonPath("$.trainingTime").value(date.toString()));
    }

    @Test
    void registerTraining_Conflict() throws Exception {
        RegisterTrainingRequest request = new RegisterTrainingRequest();
        request.setTrainingTime(LocalDate.of(2025, 7, 27));
        request.setTrainingPlace("競技場");
        request.setTrainingComments("コメント");

        when(registerTrainingService.registerTraining(any(RegisterTrainingRequest.class)))
            .thenThrow(new IllegalStateException("この日付の練習日誌は既に登録されています。"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc.perform(post("/api/register-training")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error").value("この日付の練習日誌は既に登録されています。"));
    }
    
    @Test
    void getTraining_Failure_WhenDataNotFound() throws Exception {
        LocalDate date = LocalDate.of(2025, 7, 27);

        when(trainingService.getTraining(eq(date)))
            .thenThrow(new DatabaseOperationException("想定のデータが取得できていません。", null));

        mockMvc.perform(get("/api/get-training")
                .param("trainingDate", date.toString())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.error").value("想定のデータが取得できていません。"));
    }
    
    @Test
    void delete_Success() throws Exception {

        when(deleteTrainingService.deleteTraining(LocalDate.of(2025, 7, 27)))
            .thenReturn(1); // 削除成功

        mockMvc.perform(delete("/api/delete-training")
                .param("trainingDate", "2025-07-27")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.deleteDate").value("2025-07-27"))
            .andExpect(jsonPath("$.deleteNumber").value(1));
    }

    @Test
    void deleteTraining_shouldReturn401_whenServiceReturns0() throws Exception {
        // サービスが0を返すようにモック
        when(deleteTrainingService.deleteTraining(any(LocalDate.class)))
            .thenReturn(0);

        mockMvc.perform(delete("/api/delete-training")
                .param("trainingDate", "2025-07-28"))
            .andExpect(status().isUnauthorized()) // 401
            .andExpect(status().reason("Invalid credentials"));
    }
    
    @Test
    void update_Success() throws Exception {
        when(changeTrainingService.changeTraining(any(ChangeTrainingRequest.class)))
    .thenReturn(1);

        // JSON文字列を正しく構築
        String json = """
        {
            "trainingTime": "2025-07-27",
            "trainingPlace": "練習場所を変更",
            "trainingComments": "練習日誌を変更"
            
        }
        """;

        mockMvc.perform(put("/api/change-training")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.trainingTime").value("2025-07-27"))
            .andExpect(jsonPath("$.trainingPlace").value("練習場所を変更"))
            .andExpect(jsonPath("$.trainingComments").value("練習日誌を変更"));
    }

    @Test
    void update_Failer() throws Exception {
    	String json = """
    	{
    	   "trainingTime": "",
    	   "trainingPlace": "練習場所を変更",
    	   "trainingComments": "練習日誌を変更"
    	            
    	}
    	""";

        mockMvc.perform(put("/api/change-training")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("更新したい練習日誌の日付が正しくリクエストされていません"));
    }
}