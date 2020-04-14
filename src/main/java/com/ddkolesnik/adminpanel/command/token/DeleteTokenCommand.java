package com.ddkolesnik.adminpanel.command.token;


import com.ddkolesnik.adminpanel.command.Command;
import com.ddkolesnik.adminpanel.model.AppToken;
import com.ddkolesnik.adminpanel.service.AppTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Alexandr Stegnin
 */

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DeleteTokenCommand implements Command {

    AppTokenService appTokenService;
    AppToken appToken;

    @Override
    public void execute() {
        appTokenService.delete(appToken);
    }
}
