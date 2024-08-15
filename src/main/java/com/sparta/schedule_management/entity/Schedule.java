package com.sparta.schedule_management.entity;


import com.sparta.schedule_management.dto.ScheduleRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "schedule")
public class Schedule {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String manager;

    @Column(nullable = false, length = 200)
    private String todo;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime date;


    public Schedule(ScheduleRequestDto requestDto) {
        this.manager = requestDto.getManager();
        this.todo = requestDto.getTodo();
        this.password = requestDto.getPassword();
        this.date = requestDto.getDate();
    }

}
