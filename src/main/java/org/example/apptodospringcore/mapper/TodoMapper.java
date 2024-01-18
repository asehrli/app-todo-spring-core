package org.example.apptodospringcore.mapper;

import lombok.RequiredArgsConstructor;
import org.example.apptodospringcore.dao.UserDAO;
import org.example.apptodospringcore.model.Todo;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TodoMapper implements RowMapper<Todo> {
    private final UserDAO userDAO;
    @Override
    public Todo mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Todo.builder()
                .id(rs.getObject("id", UUID.class))
                .title(rs.getString("title"))
                .description(rs.getString("description"))
                .createdAt(rs.getTimestamp("created_at"))
                .expiresAt(rs.getTimestamp("expires_at"))
                .deleted(rs.getBoolean("deleted"))
                .createdBy(userDAO.getById(rs.getObject("created_by_id", UUID.class)))
                .build();
    }
}
