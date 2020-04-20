package com.ddkolesnik.adminpanel.command.role;

import com.ddkolesnik.adminpanel.command.Command;
import com.ddkolesnik.adminpanel.model.Role;
import com.ddkolesnik.adminpanel.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Alexandr Stegnin
 */

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UpdateRoleCommand implements Command {

    Role role;
    RoleService roleService;

    @Override
    public void execute() {
        roleService.update(role);
    }
}
