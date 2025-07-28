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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.dto.competition.RegisterCompetitionRequest;
import io.github.Toku2001.trackandfieldapp.dto.user.UserDetailsForToken;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.repository.CompetitionMapper;
import io.github.Toku2001.trackandfieldapp.service.competition.impl.RegisterCompetitionServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc  
@WithMockUser(username = "testuser", roles = {"USER"})
@Transactional
@MapperScan("io.github.Toku2001.trackandfieldapp.repository") 
class CompetitionServiceImplTest {

    @InjectMocks
    private RegisterCompetitionServiceImpl service;

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

    private void setAuthentication(Object principal) {
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

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

        int result = service.registerCompetition(request);
        assertEquals(1, result);
    }
    
    @Test
    void registerCompetition_withoutAuthentication_shouldThrowNullPointerException() {
        RegisterCompetitionRequest request = createValidRequest();
        SecurityContextHolder.clearContext();

        assertThrows(NullPointerException.class, () -> service.registerCompetition(request));
    }

    @Test
    void registerCompetition_withInvalidPrincipal_shouldThrowClassCastException() {
        setAuthentication("invalidPrincipal");

        RegisterCompetitionRequest request = createValidRequest();

        assertThrows(ClassCastException.class, () -> service.registerCompetition(request));
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

        Exception ex = assertThrows(DatabaseOperationException.class, () -> service.registerCompetition(request));
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

        assertThrows(RuntimeException.class, () -> service.registerCompetition(request));
    }
}