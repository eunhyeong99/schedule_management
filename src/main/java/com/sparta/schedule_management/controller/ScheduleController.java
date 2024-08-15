package com.sparta.schedule_management.controller;


import com.sparta.schedule_management.dto.ScheduleRequestDto;
import com.sparta.schedule_management.dto.ScheduleResponseDto;
import com.sparta.schedule_management.entity.Schedule;
import com.sparta.schedule_management.service.ScheduleService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleService scheduleService;


    @PostMapping("/schedules")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.createSchedule(requestDto);
    }

    // 선택한 일정 단건의 정보를 조회
    @GetMapping("/schedules/{id}")
    public Schedule getSchedules(@PathVariable Long id) {
        return scheduleService.getSchedule(id);
    }

    // 전체 일정 조회
    @GetMapping
    public List<Schedule> getSchedules(
            @RequestParam LocalDate date,
            @RequestParam String manager
    ) {
        return scheduleService.getSchedules(date, manager);
    }


    @PutMapping("/schedules/{id}")
    public Long updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.updateSchedule(id, requestDto);
    }

    @DeleteMapping("/schedules/{id}")
    public Long deleteSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.deleteSchedule(id, requestDto);
    }


}
