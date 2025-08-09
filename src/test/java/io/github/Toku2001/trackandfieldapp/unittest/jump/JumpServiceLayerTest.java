package io.github.Toku2001.trackandfieldapp.unittest.jump;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import io.github.Toku2001.trackandfieldapp.dto.jump.ChangeJumpRequest;
import io.github.Toku2001.trackandfieldapp.dto.jump.JumpResponse;
import io.github.Toku2001.trackandfieldapp.dto.jump.PersonalBestResponse;
import io.github.Toku2001.trackandfieldapp.dto.jump.RegisterJumpRequest;
import io.github.Toku2001.trackandfieldapp.dto.user.UserDetailsForToken;
import io.github.Toku2001.trackandfieldapp.entity.Jump_Events;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.repository.JumpMapper;
import io.github.Toku2001.trackandfieldapp.service.jump.impl.ChangeJumpServiceImpl;
import io.github.Toku2001.trackandfieldapp.service.jump.impl.DeleteJumpServiceImpl;
import io.github.Toku2001.trackandfieldapp.service.jump.impl.JumpServiceImpl;
import io.github.Toku2001.trackandfieldapp.service.jump.impl.RegisterJumpServiceImpl;

@ExtendWith(MockitoExtension.class)
class JumpServiceLayerTest {

    @Mock
    private JumpMapper jumpMapper;

    @InjectMocks
    private ChangeJumpServiceImpl changeJumpService;

    @InjectMocks
    private DeleteJumpServiceImpl deleteJumpService;

    @InjectMocks
    private JumpServiceImpl jumpService;

    @InjectMocks
    private RegisterJumpServiceImpl registerJumpService;

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
    void changeJump_Success() {
        ChangeJumpRequest req = new ChangeJumpRequest();

        // 戻り値ありメソッドなので、doNothing()ではなくwhen().thenReturn()を使う
        when(jumpMapper.changeJump(eq(1L), any(ChangeJumpRequest.class))).thenReturn(1);

        int result = changeJumpService.changeJump(List.of(req));

        assertEquals(1, result);
        verify(jumpMapper, times(1)).changeJump(eq(1L), any(ChangeJumpRequest.class));
    }

    @Test
    void changeJump_Failure_NoRecordsChanged() {
        // changeJumpRecordNumber < size の場合は 0 を返す
        ChangeJumpRequest req = new ChangeJumpRequest();
        doThrow(new DatabaseOperationException("err", new Exception()))
                .when(jumpMapper).changeJump(eq(1L), any(ChangeJumpRequest.class));

        assertThrows(DatabaseOperationException.class,
                () -> changeJumpService.changeJump(List.of(req)));
    }

    @Test
	void changeJump_notAllUpdatesSuccessful_shouldReturn0() {
		// 2件の更新リクエスト
		ChangeJumpRequest req1 = new ChangeJumpRequest();
		ChangeJumpRequest req2 = new ChangeJumpRequest();
		List<ChangeJumpRequest> requests = List.of(req1, req2);

		// jumpMapper.changeJump が1件目は1件更新成功、2件目は0件（失敗）を返す
		when(jumpMapper.changeJump(anyLong(), any(ChangeJumpRequest.class)))
			.thenReturn(1)
			.thenReturn(0);

		int result = changeJumpService.changeJump(requests);

		// 更新件数がリクエスト件数に満たないため0が返る
		assertEquals(0, result);
	}

    @Test
    void deleteById_Success() {
        when(jumpMapper.deleteById(10L)).thenReturn(1);

        int result = deleteJumpService.deleteById(10L);

        assertEquals(1, result);
        verify(jumpMapper).deleteById(10L);
    }

    @Test
    void deleteAllByDate_Success() {
        LocalDate date = LocalDate.of(2025, 8, 1);
        when(jumpMapper.deleteAllByDate(date)).thenReturn(2);

        int result = deleteJumpService.deleteAllByDate("2025-08-01");

        assertEquals(2, result);
        verify(jumpMapper).deleteAllByDate(date);
    }

    @Test
    void getJumpRecords_Success() {
        Jump_Events event = new Jump_Events();
        event.setJump_event_id(1L);
        event.setEvent_code("PV");
        event.setJump_distance(5.10);
        event.setApproach_steps(16);
        event.setPole_length("4.85");
        event.setPole_stiffness("18.2");
        event.setRecord_type("official");
        event.setJump_date(LocalDate.of(2025, 7, 27));

        when(jumpMapper.getJumpRecord(1L, LocalDate.of(2025, 7, 27)))
                .thenReturn(List.of(event));

        List<JumpResponse> result = jumpService.getJumpRecords(LocalDate.of(2025, 7, 27));

        assertEquals(1, result.size());
        assertEquals("PV", result.get(0).getEvent());
        assertEquals("2025-07-27", result.get(0).getJumpDate());
    }

    @Test
    void getPersonalBest_Success() {
        Jump_Events event = new Jump_Events();
        event.setEvent_code("PV");
        event.setJump_distance(5.30);
        event.setJump_date(LocalDate.of(2025, 7, 27));

        when(jumpMapper.getPersonalBest(1L)).thenReturn(List.of(event));

        List<PersonalBestResponse> result = jumpService.getPersonalBest();

        assertEquals(1, result.size());
        assertEquals("PV", result.get(0).getEvent());
    }

    @Test
    void registerJump_Success() {
        RegisterJumpRequest req = new RegisterJumpRequest();
        
        when(jumpMapper.registerJump(eq(1L), any(RegisterJumpRequest.class))).thenReturn(1);

        registerJumpService.registerJump(List.of(req));

        verify(jumpMapper, times(1)).registerJump(eq(1L), any(RegisterJumpRequest.class));
    }
}