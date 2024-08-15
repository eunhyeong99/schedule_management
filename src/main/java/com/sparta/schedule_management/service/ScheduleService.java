package com.sparta.schedule_management.service;


import com.sparta.schedule_management.dto.ScheduleRequestDto;
import com.sparta.schedule_management.dto.ScheduleResponseDto;
import com.sparta.schedule_management.entity.Schedule;
import com.sparta.schedule_management.repository.ScheduleRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {

        Schedule schedule = new Schedule(requestDto);

        Schedule saveSchedule = scheduleRepository.save(schedule);

        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(saveSchedule);

        return scheduleResponseDto;
    }

    public Schedule getSchedule(Long id) {
        return scheduleRepository.findById(id);
    }

    public List<Schedule> getSchedules(LocalDate date, String manager) {
        return scheduleRepository.findByDateAndManager(date, manager);
    }

    public Long updateSchedule(Long id, ScheduleRequestDto requestDto) {

        Schedule schedule = scheduleRepository.findById(id);

        if (schedule != null) {
            if (schedule.getPassword().equals(requestDto.getPassword())) {
                scheduleRepository.update(id, requestDto);
                return id;
            } else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("선택한 스케줄이 존재하지 않습니다.");
        }

    }

    public Long deleteSchedule(Long id, ScheduleRequestDto requestDto) {

        Schedule schedule = scheduleRepository.findById(id);

        if (schedule != null) {
            if (schedule.getPassword().equals(requestDto.getPassword())) {
                scheduleRepository.delete(id,requestDto);
                return id;
            } else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("선택한 스케줄이 존재하지 않습니다.");
        }

    }


}
