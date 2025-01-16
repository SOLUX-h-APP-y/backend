package com.happy.biling.controller;

import com.happy.biling.domain.service.ProfileService;
import com.happy.biling.dto.profile.CheerRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cheers")
public class CheerController {
    private final ProfileService profileService;

    // 응원 추가
    @PostMapping
    public ResponseEntity<Map<String, Object>> addCheer(@RequestBody CheerRequestDto requestDto) {
        profileService.addCheer(requestDto);

        // 총 응원 횟수 반환
        long totalCheers = profileService.getTotalCheersByReceiverId(requestDto.getReceiverId());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("totalCheers", totalCheers);

        return ResponseEntity.ok(response);
    }
}

