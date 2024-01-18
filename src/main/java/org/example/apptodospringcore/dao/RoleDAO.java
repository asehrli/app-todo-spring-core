package org.example.apptodospringcore.dao;

import lombok.RequiredArgsConstructor;
import org.example.apptodospringcore.mapper.RoleMapper;
import org.example.apptodospringcore.model.Role;
import org.example.apptodospringcore.model.enums.Permission;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RoleDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RoleMapper roleMapper;

    public List<Role> getAll() {
        String sql = "select * from role";
        List<Role> roles = jdbcTemplate.query(sql, Map.of(), roleMapper);

        roles.forEach(role -> {
            String q = "select permission from roles_permissions where role_id = :roleId";
            List<Permission> permissions = jdbcTemplate.queryForList(q, Map.of("roleId", role.getId()), Permission.class);
            role.setPermissions(permissions);
        });

        return roles;
    }

    public Role save(Role role) {
        String sql = "insert into role(name) values(:name) returning id, name;";
        Role savedRole = jdbcTemplate.queryForObject(sql, Map.of("name", role.getName()), roleMapper);

        StringBuilder sb = new StringBuilder("insert into roles_permissions values ");
        role.getPermissions().forEach(permission -> {
            sb.append("(%s, '%s'),".formatted(savedRole.getId(), permission.name()));
        });

        sb.deleteCharAt(sb.length() - 1);

        jdbcTemplate.update(sb.toString(), Map.of());
        return savedRole;
    }

    public void delete(Integer id) {
        String sql = "delete from roles_permissions where role_id = :roleId";
        jdbcTemplate.update(sql, Map.of("roleId", id));
        sql = "delete from role where id = :id";
        jdbcTemplate.update(sql, Map.of("id", id));
    }
}
