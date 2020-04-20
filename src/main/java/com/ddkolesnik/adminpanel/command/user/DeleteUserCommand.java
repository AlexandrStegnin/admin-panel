package com.ddkolesnik.adminpanel.command.user;


import com.ddkolesnik.adminpanel.command.Command;
import com.ddkolesnik.adminpanel.model.User;
import com.ddkolesnik.adminpanel.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Alexandr Stegnin
 */

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DeleteUserCommand implements Command {

    User user;
    UserService userService;

    @Override
    public void execute() {
        userService.delete(user);
    }

}
