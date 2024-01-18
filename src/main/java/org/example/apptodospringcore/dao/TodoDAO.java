package org.example.apptodospringcore.dao;

import lombok.RequiredArgsConstructor;
import org.example.apptodospringcore.mapper.TodoMapper;
import org.example.apptodospringcore.model.Todo;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TodoDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TodoMapper todoMapper;

    public Todo add(Todo todo) {
        String sql = "insert into todo (title, description, created_by_id) " +
                "values (:title, :description, :createdById) " +
                "returning id, title, description, created_at, created_by_id, expires_at, deleted";

        var map = Map.of(
                "title", todo.getTitle(),
                "description", todo.getDescription(),
                "createdById", todo.getCreatedBy().getId());

        return jdbcTemplate.queryForObject(sql, map, todoMapper);
    }

    public List<Todo> myTodos(UUID userId) {
        try {
            String sql = "select * from todo where created_by_id = :cbi";
            return jdbcTemplate.query(sql, Map.of("cbi", userId), todoMapper);
        }catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void deleteTodo(UUID id) {
        String sql = "delete from todo where id = :id";
        jdbcTemplate.update(sql, Map.of("id", id));
    }

    public void complete(UUID id) {
        String sql = "update todo set deleted = true where id = :id";
        jdbcTemplate.update(sql, Map.of("id", id));
    }
}
