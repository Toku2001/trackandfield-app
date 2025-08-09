package io.github.Toku2001.trackandfieldapp.unittest.training;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import io.github.Toku2001.trackandfieldapp.dto.competition.ChangeCompetitionRequest;
import io.github.Toku2001.trackandfieldapp.dto.training.ChangeTrainingRequest;
import io.github.Toku2001.trackandfieldapp.dto.training.RegisterTrainingRequest;
import io.github.Toku2001.trackandfieldapp.dto.training.TrainingRequest;
import io.github.Toku2001.trackandfieldapp.dto.training.TrainingResponse;
import io.github.Toku2001.trackandfieldapp.dto.user.UserDetailsForToken;
import io.github.Toku2001.trackandfieldapp.entity.Training_Info;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.repository.TrainingMapper;
import io.github.Toku2001.trackandfieldapp.service.training.impl.ChangeTrainingServiceImpl;
import io.github.Toku2001.trackandfieldapp.service.training.impl.DeleteTrainingServiceImpl;
import io.github.Toku2001.trackandfieldapp.service.training.impl.RegisterTrainingServiceImpl;
import io.github.Toku2001.trackandfieldapp.service.training.impl.TrainingServiceImpl;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc  
@WithMockUser(username = "testuser", roles = {"USER"})
@Transactional
@MapperScan("io.github.Toku2001.trackandfieldapp.repository") 
public class TrainingServiceTraining {

    @InjectMocks
    private DeleteTrainingServiceImpl deleteTrainingService;
	
	@InjectMocks
    private TrainingServiceImpl trainingService;
	
	@InjectMocks
    private ChangeTrainingServiceImpl changeTrainingServiceImpl;
	
	@InjectMocks
    private RegisterTrainingServiceImpl registerTrainingServiceImpl;

    @Mock
    private TrainingMapper trainingMapper;

    private ChangeTrainingRequest changeTrainingRequest(){
        ChangeTrainingRequest changeTrainingRequest = new ChangeTrainingRequest();
        changeTrainingRequest.setTrainingId(1);
        changeTrainingRequest.setTrainingComments("跳躍練習");
        changeTrainingRequest.setTrainingPlace("学校");
        changeTrainingRequest.setTrainingTime(LocalDate.of(2025, 07, 27));
        return changeTrainingRequest;
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

    // --- DeleteTrainingServiceImpl ---

    @Test
    void deleteTraining_Success() {
        LocalDate date = LocalDate.of(2025, 7, 27);
        when(trainingMapper.deleteTraining(1L, date)).thenReturn(1);

        int deletedCount = deleteTrainingService.deleteTraining(date);

        assertEquals(1, deletedCount);
        verify(trainingMapper).deleteTraining(1L, date);
    }

    @Test
    void deleteTraining_Failure_ThrowsException() {
        LocalDate date = LocalDate.of(2025, 7, 27);
        when(trainingMapper.deleteTraining(1L, date)).thenReturn(0);

        DatabaseOperationException thrown = assertThrows(DatabaseOperationException.class, () -> {
            deleteTrainingService.deleteTraining(date);
        });

        assertTrue(thrown.getMessage().contains("正しく練習日誌が削除されませんでした"));
        verify(trainingMapper).deleteTraining(1L, date);
    }

    // --- TrainingServiceImpl ---

    @Test
    void getTraining_Success() {
        LocalDate date = LocalDate.of(2025, 7, 27);

        Training_Info trainingInfo = new Training_Info();
        trainingInfo.setTraining_Id(123);
        trainingInfo.setTraining_Time(LocalDate.of(2025, 7, 27));
        trainingInfo.setTraining_Place("競技場");
        trainingInfo.setTraining_Comments("良い練習でした");

        when(trainingMapper.getTrainingByDate(1L, date)).thenReturn(trainingInfo);

        TrainingResponse response = trainingService.getTraining(date);

        assertEquals(123L, response.getTrainingId());
        assertEquals(LocalDate.of(2025, 7, 27), response.getTrainingTime());
        assertEquals("競技場", response.getTrainingPlace());
        assertEquals("良い練習でした", response.getTrainingComments());
    }

    @Test
    void getTraining_Failure_ThrowsException() {
        LocalDate date = LocalDate.of(2025, 7, 27);

        when(trainingMapper.getTrainingByDate(1L, date)).thenReturn(null);

        DatabaseOperationException thrown = assertThrows(DatabaseOperationException.class, () -> {
            trainingService.getTraining(date);
        });

        assertTrue(thrown.getMessage().contains("想定のデータが取得できていません。"));
    }

    @Test
    void registerTraining_Success() {
        RegisterTrainingRequest request = new RegisterTrainingRequest();
        request.setTrainingTime(LocalDate.of(2025, 7, 27));
        request.setTrainingPlace("競技場");
        request.setTrainingComments("コメント");

        when(trainingMapper.registerTraining(
            eq(1L),
            eq(request.getTrainingTime()),
            eq(request.getTrainingPlace()),
            eq(request.getTrainingComments())))
            .thenReturn(1);

        int result = registerTrainingServiceImpl.registerTraining(request);

        assertEquals(1, result);
        verify(trainingMapper).registerTraining(
            eq(1L),
            eq(request.getTrainingTime()),
            eq(request.getTrainingPlace()),
            eq(request.getTrainingComments())
        );
    }

    @Test
    void registerTraining_DataIntegrityViolationException() {
        RegisterTrainingRequest request = new RegisterTrainingRequest();
        request.setTrainingTime(LocalDate.of(2025, 7, 27));
        request.setTrainingPlace("競技場");
        request.setTrainingComments("コメント");

        // DataIntegrityViolationExceptionをスローする設定
        when(trainingMapper.registerTraining(
            anyLong(),
            any(LocalDate.class),
            anyString(),
            anyString()))
            .thenThrow(new org.springframework.dao.DataIntegrityViolationException("duplicate key"));

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            registerTrainingServiceImpl.registerTraining(request);
        });

        assertEquals("この日付の練習日誌は既に登録されています。", thrown.getMessage());
        verify(trainingMapper).registerTraining(
            eq(1L),
            eq(request.getTrainingTime()),
            eq(request.getTrainingPlace()),
            eq(request.getTrainingComments())
        );
    }

    @Test
    void updateTraining_shouldReturnChangeNumber() {

        when(trainingMapper.changeTraining(
                anyLong(),
                anyInt(),
                any(LocalDate.class),
                anyString(),
                anyString()))
            .thenReturn(1);

        ChangeTrainingRequest changeRequest = changeTrainingRequest();
        int changeResult = changeTrainingServiceImpl.changeTraining(changeRequest);
        assertEquals(1, changeResult);
    }

    @Test
    void updateTraining_ZeroChangeNumber() {

        when(trainingMapper.changeTraining(
                anyLong(),
                anyInt(),
                any(LocalDate.class),
                anyString(),
                anyString()))
            .thenReturn(0);

        ChangeTrainingRequest changeRequest = changeTrainingRequest();
        Exception ex = assertThrows(DatabaseOperationException.class, () -> changeTrainingServiceImpl.changeTraining(changeRequest));
        assertEquals("練習日誌の更新件数が0件です。", ex.getMessage());
    }
}