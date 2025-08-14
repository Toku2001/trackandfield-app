package io.github.Toku2001.trackandfieldapp.unittest.competition;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.dto.competition.ChangeCompetitionRequest;
import io.github.Toku2001.trackandfieldapp.dto.competition.CompetitionResponse;
import io.github.Toku2001.trackandfieldapp.dto.competition.RegisterCompetitionRequest;
import io.github.Toku2001.trackandfieldapp.dto.user.UserDetailsForToken;
import io.github.Toku2001.trackandfieldapp.entity.Competition_Info;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.repository.CompetitionMapper;
import io.github.Toku2001.trackandfieldapp.service.competition.impl.ChangeCompetitionServiceImpl;
import io.github.Toku2001.trackandfieldapp.service.competition.impl.CompetitionServiceImpl;
import io.github.Toku2001.trackandfieldapp.service.competition.impl.DeleteCompetitionServiceImpl;
import io.github.Toku2001.trackandfieldapp.service.competition.impl.RegisterCompetitionServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc  
@WithMockUser(username = "testuser", roles = {"USER"})
@Transactional
@MapperScan("io.github.Toku2001.trackandfieldapp.repository") 
class CompetitionServiceImplTest {

	@InjectMocks
    private RegisterCompetitionServiceImpl registerService;
	
	@InjectMocks
    private DeleteCompetitionServiceImpl deleteService;
	
	@InjectMocks
    private ChangeCompetitionServiceImpl changeService;
	
	@InjectMocks
    private CompetitionServiceImpl competitionService;

    @Mock
    private CompetitionMapper mapper;

    private RegisterCompetitionRequest createValidRequest() {
        return new RegisterCompetitionRequest(
            "大会名",
            "競技場",
            LocalDate.of(2025, 7, 27),
            "コメント"
        );
    }
    
    private ChangeCompetitionRequest changeValidRequest() {
        return new ChangeCompetitionRequest(
        		1,
            "大会名",
            "競技場",
            LocalDate.of(2025, 7, 27),
            "コメント"
        );
    }

    private void setAuthentication(Object principal) {
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    //競技会情報登録
    @Test
    void registerCompetition_validRequest_shouldReturnRegisterNumber() {
        setAuthentication(new UserDetailsForToken(1L, "testuser", List.of()));

        RegisterCompetitionRequest request = createValidRequest();
        when(mapper.registerCompetition(
            anyLong(),
            anyString(),
            anyString(),
            any(LocalDate.class),
            anyString()))
        .thenReturn(1);

        int result = registerService.registerCompetition(request);
        assertEquals(1, result);
    }
    
    @Test
    void registerCompetition_withoutAuthentication_shouldThrowNullPointerException() {
        RegisterCompetitionRequest request = createValidRequest();
        SecurityContextHolder.clearContext();

        assertThrows(NullPointerException.class, () -> registerService.registerCompetition(request));
    }

    @Test
    void registerCompetition_withInvalidPrincipal_shouldThrowClassCastException() {
        setAuthentication("invalidPrincipal");

        RegisterCompetitionRequest request = createValidRequest();

        assertThrows(ClassCastException.class, () -> registerService.registerCompetition(request));
    }

    @Test
    void registerCompetition_mapperReturnsZero_shouldThrowDatabaseOperationException() {
        setAuthentication(new UserDetailsForToken(1L, "testuser", List.of()));

        RegisterCompetitionRequest request = createValidRequest();
        when(mapper.registerCompetition(
            anyLong(),
            anyString(),
            anyString(),
            any(LocalDate.class),
            anyString()))
        .thenReturn(0);

        Exception ex = assertThrows(DatabaseOperationException.class, () -> registerService.registerCompetition(request));
        assertEquals("練習日誌を登録できませんでした", ex.getMessage());
    }

    @Test
    void registerCompetition_mapperThrowsException_shouldPropagateException() {
        setAuthentication(new UserDetailsForToken(1L, "testuser", List.of()));

        RegisterCompetitionRequest request = createValidRequest();
        when(mapper.registerCompetition(
            anyLong(),
            anyString(),
            anyString(),
            any(LocalDate.class),
            anyString()))
        .thenThrow(new RuntimeException("DBエラー"));

        assertThrows(RuntimeException.class, () -> registerService.registerCompetition(request));
    }

    @Test
    void registerCompetition_whenDataIntegrityViolationExceptionThrown_shouldThrowIllegalStateException() {
        setAuthentication(new UserDetailsForToken(1L, "testuser", List.of()));

        RegisterCompetitionRequest request = createValidRequest();

        // モックで DataIntegrityViolationException をスローさせる
        when(mapper.registerCompetition(
            anyLong(),
            anyString(),
            anyString(),
            any(LocalDate.class),
            anyString()))
        .thenThrow(new DataIntegrityViolationException("duplicate key"));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            registerService.registerCompetition(request);
        });

        assertEquals("この日付の大会は既に登録されています。", ex.getMessage());
    }

    //競技会情報削除
    @Test
    void deleteCompetition_validRequest_shouldReturnRegisterNumber() {
        setAuthentication(new UserDetailsForToken(1L, "testuser", List.of()));

        when(mapper.deleteCompetition(
            anyLong(),
            any(LocalDate.class)))
        .thenReturn(1);

        int deleteResult = deleteService.deleteCompetition(LocalDate.of(2025, 7, 27));
        assertEquals(1, deleteResult);
    }
    
    @Test
    void deleteCompetition_mapperReturnsZero_shouldThrowDatabaseOperationException() {
        setAuthentication(new UserDetailsForToken(1L, "testuser", List.of()));

        when(mapper.deleteCompetition(
            anyLong(),
            any(LocalDate.class)))
        .thenReturn(0);

        Exception ex = assertThrows(DatabaseOperationException.class, () -> deleteService.deleteCompetition(LocalDate.of(2025, 7, 27)));
        assertEquals("正しく競技会情報が削除されませんでした", ex.getMessage());
    }
    
    //競技会情報更新
    @Test
    void updateCompetition_shouldReturnChangeNumber() {
        setAuthentication(new UserDetailsForToken(1L, "testuser", List.of()));

        ChangeCompetitionRequest changeRequest = changeValidRequest();
        when(mapper.changeCompetition(
                anyLong(),
                anyInt(),
                anyString(),
                anyString(),
                any(LocalDate.class),
                anyString()))
            .thenReturn(1);

        int changeResult = changeService.changeCompetition(changeRequest);
        assertEquals(1, changeResult);
    }
    
    @Test
    void updateCompetition_mapperReturnsZero_shouldReturnChangeNumber() {
        setAuthentication(new UserDetailsForToken(1L, "testuser", List.of()));

        ChangeCompetitionRequest changeRequest = changeValidRequest();
        when(mapper.changeCompetition(
                anyLong(),
                anyInt(),
                anyString(),
                anyString(),
                any(LocalDate.class),
                anyString()))
            .thenReturn(0);

        Exception ex = assertThrows(DatabaseOperationException.class, () -> changeService.changeCompetition(changeRequest));
        assertEquals("競技会情報の更新件数が0件です。", ex.getMessage());
    }
    
    //競技会情報取得
    @Test
    void getCompetition_shouldReturnCompetitionInfo() {
        setAuthentication(new UserDetailsForToken(1L, "testuser", List.of()));
        
        // モックが返すレスポンスを定義
        Competition_Info mockResponse = new Competition_Info();
        mockResponse.setCompetition_Id(0);
        mockResponse.setCompetition_Name("大会名");
        mockResponse.setCompetition_Place("場所");
        mockResponse.setCompetition_Time(LocalDate.of(2025, 7, 27));
        mockResponse.setCompetition_Comments("コメント");
        
        CompetitionResponse competitionResponse = new CompetitionResponse();
        competitionResponse.setCompetitionName("大会名");
        competitionResponse.setCompetitionPlace("場所");
        competitionResponse.setCompetitionTime(LocalDate.of(2025, 7, 27));
        competitionResponse.setCompetitionComments("コメント");

        when(mapper.getCompetitionByDate(
                anyLong(),
                any(LocalDate.class)))
            .thenReturn(mockResponse);

        CompetitionResponse getResult = competitionService.getCompetitionByDate(LocalDate.of(2025, 7, 27));
        assertEquals(competitionResponse, getResult);
    }
    
    @Test
    void getCompetition_mapperReturnsNull_shouldReturnCompetitionInfo() {
        setAuthentication(new UserDetailsForToken(1L, "testuser", List.of()));
        
        // モックが返すレスポンスを定義
        Competition_Info mockResponse = new Competition_Info();
        mockResponse.setCompetition_Id(1);
        mockResponse.setCompetition_Name("大会名");
        mockResponse.setCompetition_Place("場所");
        mockResponse.setCompetition_Time(LocalDate.of(2025, 7, 27));
        mockResponse.setCompetition_Comments("コメント");
        
        CompetitionResponse competitionResponse = new CompetitionResponse();
        competitionResponse.setCompetitionName("大会名");
        competitionResponse.setCompetitionPlace("場所");
        competitionResponse.setCompetitionTime(LocalDate.of(2025, 7, 27));
        competitionResponse.setCompetitionComments("コメント");

        when(mapper.getCompetitionByDate(
                anyLong(),
                any(LocalDate.class)))
            .thenReturn(null);

        Exception ex = assertThrows(DatabaseOperationException.class, () -> competitionService.getCompetitionByDate(LocalDate.of(2025, 7, 27)));
        assertEquals("想定のデータが取得できていません。", ex.getMessage());
    }
    
    //次回の競技会情報を取得
    @Test
    void getUpcomingCompetition_shouldReturnCompetitionInfo() {
        setAuthentication(new UserDetailsForToken(1L, "testuser", List.of()));
        
        // モックが返すレスポンスを定義
        Competition_Info mockNextResponse = new Competition_Info();
        mockNextResponse.setCompetition_Id(1);
        mockNextResponse.setCompetition_Name("大会名");
        mockNextResponse.setCompetition_Place("場所");
        mockNextResponse.setCompetition_Time(LocalDate.of(2025, 7, 27));
        mockNextResponse.setCompetition_Comments("コメント");
        
        CompetitionResponse competitionNextResponse = new CompetitionResponse();
        competitionNextResponse.setCompetitionName("大会名");
        competitionNextResponse.setCompetitionPlace("場所");
        competitionNextResponse.setCompetitionTime(LocalDate.of(2025, 7, 27));

       when(mapper.getNextCompetition(1L))
           .thenReturn(mockNextResponse);

        CompetitionResponse getResult = competitionService.getNextCompetition();
        assertEquals(competitionNextResponse, getResult);
    }
    
    @Test
    void getUpcomingCompetition_mapperReturnsNull_shouldReturnCompetitionInfo() {
        setAuthentication(new UserDetailsForToken(1L, "testuser", List.of()));
        
        // モックが返すレスポンスを定義
        Competition_Info mockResponse = new Competition_Info();
        mockResponse.setCompetition_Id(1);
        mockResponse.setCompetition_Name("大会名");
        mockResponse.setCompetition_Place("場所");
        mockResponse.setCompetition_Time(LocalDate.of(2025, 7, 27));
        mockResponse.setCompetition_Comments("コメント");
        
        CompetitionResponse competitionResponse = new CompetitionResponse();
        competitionResponse.setCompetitionName("大会名");
        competitionResponse.setCompetitionPlace("場所");
        competitionResponse.setCompetitionTime(LocalDate.of(2025, 7, 27));
        competitionResponse.setCompetitionComments("コメント");

       when(mapper.getNextCompetition(1L))
       .thenReturn(null);

        Exception ex = assertThrows(DatabaseOperationException.class, () -> competitionService.getNextCompetition());
        assertEquals("想定のデータが取得できていません。", ex.getMessage());
    }
}