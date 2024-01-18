package org.example.apptodospringcore.mapper;

import org.example.apptodospringcore.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getObject("id", UUID.class))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .createdAt(rs.getTimestamp("created_at"))
                .enabled(rs.getBoolean("enabled"))
                .build();
    }
}
