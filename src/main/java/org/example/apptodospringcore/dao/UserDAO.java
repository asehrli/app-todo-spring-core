package org.example.apptodospringcore.dao;

import lombok.RequiredArgsConstructor;
import org.example.apptodospringcore.mapper.UserMapper;
import org.example.apptodospringcore.model.Role;
import org.example.apptodospringcore.model.User;
import org.example.apptodospringcore.model.enums.Permission;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    public User save(User user) {
        String sql = """
                insert into users(name, email, password)
                values(:name, :email, :password)
                returning id, name, email, password, created_at, enabled""";

        Map<String, Object> map = Map.of(
                "name", user.getName(),
                "email", user.getEmail(),
                "password", user.getPassword()
        );

        user = jdbcTemplate.queryForObject(sql, map, userMapper);

        sql = "select id from role where name = 'USER'";
        Integer userRoleId = jdbcTemplate.queryForObject(sql, Map.of(), Integer.class);
        assert userRoleId != null;

        sql = "insert into users_roles values (:userId, :roleId);";
        map = Map.of("userId", user.getId(), "roleId", userRoleId);
        jdbcTemplate.update(sql, map);

        return user;
    }

    public User getByEmail(String email) {
        try {
            String sql = "select * from users where email = :email";
            Map<String, Object> map = Map.of("email", email);
            User user = jdbcTemplate.queryForObject(sql, map, userMapper);
            assert user != null;

            sql = "select * from role r inner join users_roles ur on r.id = ur.role_id and ur.user_id = :userId";
            map = Map.of("userId", user.getId());
            List<Role> roles = jdbcTemplate.query(sql, map, (rs, rowNum) -> Role.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .build());

            user.setRoles(roles);

            roles.forEach(role -> {
                String query = "select permission from roles_permissions where role_id = :roleId";
                List<Permission> permissions = jdbcTemplate.queryForList(query, Map.of("roleId", role.getId()), Permission.class);
                role.setPermissions(permissions);
            });

            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public void saveCode(UUID id, String code) {
        String sql = "insert into code(user_id, code) values(:userId, :code);";
        jdbcTemplate.update(sql, Map.of("userId", id, "code", code));
    }

    public boolean isCodeRight(UUID id, String code) {
        try {
            String sql = """
                    select exists(select * from code c
                                    where c.user_id = :userId
                                    and c.code = :code
                                    and expires_at > now())
                    """;

            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Map.of("userId", id, "code", code), boolean.class));
        } catch (Exception e) {
            return false;
        }
    }

    public void enable(UUID id) {
        String sql = "update users set enabled = true where id = :id";
        var map = Map.of("id", id);
        jdbcTemplate.update(sql, map);
    }

    public List<User> getAll() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, userMapper);
    }

    public void changeEnable(UUID id, boolean enabled) {
        String sql = "update users set enabled = :enabled where id = :id";
        jdbcTemplate.update(sql, Map.of("enabled", enabled, "id", id));
    }

    public User getById(UUID createdById) {
        try {
            String sql = "select * from users where id = :id";
            return jdbcTemplate.queryForObject(sql, Map.of("id", createdById), userMapper);
        } catch (Exception e) {
            return null;
        }
    }
}
