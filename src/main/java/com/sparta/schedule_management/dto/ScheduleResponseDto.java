package com.sparta.schedule_management.dto;

import com.sparta.schedule_management.entity.Schedule;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleResponseDto {

    private Long id;
    private String manager;
    private String todo;
    private String password;
    private LocalDateTime date;


    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.manager = schedule.getManager();
        this.todo = schedule.getTodo();
        this.password = schedule.getPassword();
        this.date = schedule.getDate();
    }

    public ScheduleResponseDto(Long id, String manager, String todo, String password, LocalDateTime date) {
        this.id = id;
        this.manager = manager;
        this.todo = todo;
        this.password = password;
        this.date = date;
    }

}
