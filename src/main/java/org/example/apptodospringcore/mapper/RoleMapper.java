package org.example.apptodospringcore.mapper;

import org.example.apptodospringcore.model.Role;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RoleMapper implements RowMapper<Role> {
    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Role.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}
