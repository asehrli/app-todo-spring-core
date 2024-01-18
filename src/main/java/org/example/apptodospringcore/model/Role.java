package org.example.apptodospringcore.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.apptodospringcore.model.enums.Permission;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    Integer id;
    String name;
    List<Permission> permissions;
}
