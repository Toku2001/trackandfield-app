package io.github.Toku2001.trackandfieldapp.service.jump;

import java.time.LocalDate;
import java.util.List;

import io.github.Toku2001.trackandfieldapp.dto.jump.JumpResponse;
import io.github.Toku2001.trackandfieldapp.dto.jump.PersonalBestResponse;

public interface JumpService {
    List<JumpResponse> getJumpRecords(LocalDate date);
    
    List<PersonalBestResponse> getPersonalBest();
}