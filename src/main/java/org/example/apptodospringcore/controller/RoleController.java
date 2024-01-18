package org.example.apptodospringcore.controller;

import lombok.RequiredArgsConstructor;
import org.example.apptodospringcore.dao.RoleDAO;
import org.example.apptodospringcore.model.Role;
import org.example.apptodospringcore.model.enums.Permission;
import org.example.apptodospringcore.payload.AddRoleDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleDAO roleDAO;

    @PreAuthorize("hasAuthority('GET_ROLE')")
    @GetMapping
    public String rolePage(Model model){
        model.addAttribute("roles", roleDAO.getAll());
        model.addAttribute("permissions", Permission.values());
        return "role";
    }

    @PreAuthorize("hasAuthority('ADD_ROLE')")
    @PostMapping
    public String addRole(@ModelAttribute AddRoleDTO addRoleDTO) {
        Role role = new Role();
        role.setName(addRoleDTO.name());

        List<Permission> permissions = new ArrayList<>();
        for (String permissionName : addRoleDTO.permissions()) {
            permissions.add(Permission.valueOf(permissionName));
        }

        role.setPermissions(permissions);

        roleDAO.save(role);
        return "redirect:/role";
    }


    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    @PostMapping("/delete")
    public String deleteRole(@RequestParam(name = "id") Integer id) {
        roleDAO.delete(id);
        return "redirect:/role";
    }
}
