package io.github.Toku2001.trackandfieldapp.service.jump.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.dto.jump.JumpResponse;
import io.github.Toku2001.trackandfieldapp.dto.jump.PersonalBestResponse;
import io.github.Toku2001.trackandfieldapp.dto.user.UserDetailsForToken;
import io.github.Toku2001.trackandfieldapp.entity.Jump_Events;
import io.github.Toku2001.trackandfieldapp.repository.JumpMapper;
import io.github.Toku2001.trackandfieldapp.service.jump.JumpService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class JumpServiceImpl implements JumpService {

    private final JumpMapper jumpMapper;

    @Override
    public List<JumpResponse> getJumpRecords(LocalDate date) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetailsForToken userDetails = (UserDetailsForToken) authentication.getPrincipal();
        List<Jump_Events> events = jumpMapper.getJumpRecord(userDetails.getUserId(), date);
        return events.stream().map(e -> new JumpResponse(
        		e.getJump_event_id(),
                e.getEvent_code(),
                e.getJump_distance(),
                e.getApproach_steps(),
                e.getPole_length(),
                e.getPole_stiffness(),
                e.getRecord_type(),
                e.getJump_date().format(DateTimeFormatter.ISO_DATE)
        )).collect(Collectors.toList());
    }
    
    @Override
    public List<PersonalBestResponse> getPersonalBest(){
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetailsForToken userDetails = (UserDetailsForToken) authentication.getPrincipal();
	    List<Jump_Events> personalBest = jumpMapper.getPersonalBest(userDetails.getUserId());
	    return personalBest.stream().map(e -> new PersonalBestResponse(
                e.getEvent_code(),
                e.getJump_distance(),
                e.getJump_date().format(DateTimeFormatter.ISO_DATE)
        )).collect(Collectors.toList());
    }
}