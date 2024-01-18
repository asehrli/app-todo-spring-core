package org.example.apptodospringcore.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Todo {
    UUID id;
    String title;
    String description;
    Timestamp createdAt;
    User createdBy;
    Timestamp expiresAt;
    boolean deleted;
}
