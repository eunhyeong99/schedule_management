package com.sparta.schedule_management.dto;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleRequestDto {

    private String manager;
    private String todo;
    private String password;
    private LocalDateTime date;

}
