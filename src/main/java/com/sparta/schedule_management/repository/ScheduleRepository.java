package com.sparta.schedule_management.repository;


import com.sparta.schedule_management.dto.ScheduleRequestDto;
import com.sparta.schedule_management.dto.ScheduleResponseDto;
import com.sparta.schedule_management.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    public Schedule save(Schedule schedule) {
        // 기본 키를 반환받기 위한 객체
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO schedule (manager, todo, password, date) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, schedule.getManager());
                    preparedStatement.setString(2, schedule.getTodo());
                    preparedStatement.setString(3, schedule.getPassword());

                    if (schedule.getDate() != null) {
                        preparedStatement.setTimestamp(4, Timestamp.valueOf(schedule.getDate()));
                    } else {
                        preparedStatement.setNull(4, java.sql.Types.TIMESTAMP);
                    }

                    return preparedStatement;
                },
                keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            schedule.setId(key.longValue());
        }

        return schedule;
    }

    public List<ScheduleResponseDto> findAll() {
        String sql = "SELECT * FROM schedule";

        return jdbcTemplate.query(sql, new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 결과를 ScheduleResponseDto 타입으로 변환
                Long id = rs.getLong("id");
                String username = rs.getString("manager");
                String contents = rs.getString("todo");
                String password = rs.getString("password");

                Timestamp timestamp = rs.getTimestamp("date");
                LocalDateTime date = timestamp != null ? timestamp.toLocalDateTime() : null;

                return new ScheduleResponseDto(id, username, contents, password, date);
            }
        });
    }

    public List<Schedule> findByDateAndManager(LocalDate date, String manager) {
        // Base SQL query
        String sql = "SELECT * FROM schedule WHERE " +
                "(? IS NULL OR DATE(date) = ?) AND " +
                "(? IS NULL OR manager LIKE ?) " +
                "ORDER BY date DESC";

        return jdbcTemplate.query(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            // Set parameters
            if (date != null) {
                preparedStatement.setDate(1, java.sql.Date.valueOf(date));
                preparedStatement.setDate(2, java.sql.Date.valueOf(date));
            } else {
                preparedStatement.setNull(1, Types.DATE);
                preparedStatement.setNull(2, Types.DATE);
            }

            if (manager != null && !manager.isEmpty()) {
                preparedStatement.setString(3, "%" + manager + "%");
            } else {
                preparedStatement.setNull(3, Types.VARCHAR);
            }

            preparedStatement.setString(4, "%" + manager + "%");

            return preparedStatement;
        }, new ScheduleRowMapper());
    }

    public void update(Long id, ScheduleRequestDto requestDto) {

        validatePassword(id, requestDto.getPassword());

        String sql = "UPDATE schedule SET manager = ?, todo = ? WHERE id = ?";
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            preparedStatement.setString(1, requestDto.getManager());
            preparedStatement.setString(2, requestDto.getTodo());

            return preparedStatement;
        });
    }



    public void delete(Long id,ScheduleRequestDto requestDto) {
        validatePassword(id, requestDto.getPassword());

        String sql = "DELETE FROM schedule WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Schedule findById(Long id) {
        String sql = "SELECT * FROM schedule WHERE id = ?";

        return jdbcTemplate.query(sql, (resultSet) -> {
            if (resultSet.next()) {
                Schedule schedule = new Schedule();
                schedule.setId(resultSet.getLong("id"));
                schedule.setManager(resultSet.getString("manager"));
                schedule.setTodo(resultSet.getString("todo"));
                schedule.setPassword(resultSet.getString("password"));

                Timestamp timestamp = resultSet.getTimestamp("date");
                if (timestamp != null) {
                    schedule.setDate(timestamp.toLocalDateTime());
                }

                return schedule;
            } else {
                return null;
            }
        }, id);
    }

    private void validatePassword(Long id, String providedPassword) {
        String sql = "SELECT password FROM schedule WHERE id = ?";
        String actualPassword = jdbcTemplate.queryForObject(sql, String.class, id);

        if (actualPassword == null || !actualPassword.equals(providedPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }


    private static class ScheduleRowMapper implements RowMapper<Schedule> {
        @Override
        public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
            Schedule schedule = new Schedule();
            schedule.setId(rs.getLong("id"));
            schedule.setManager(rs.getString("manager"));
            schedule.setTodo(rs.getString("todo"));
            schedule.setPassword(rs.getString("password"));

            // Convert SQL Timestamp to LocalDateTime
            LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();
            schedule.setDate(date);

            return schedule;
        }
    }

}
