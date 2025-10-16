package com.example.demo.shell;

import com.example.demo.account.dto.Account;
import com.example.demo.account.service.AuthenticationService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class MyCommands {

    private final AuthenticationService authenticationService;

    public MyCommands(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @ShellMethod(key = "login", value = "Login with your ID and password")
    public String login(long id, String password) {
        Account account = authenticationService.login(id, password);
        if (account != null) {
            return account.toString();
        } else {
            return "id or password not correct";
        }
    }

    @ShellMethod(key = "logout", value = "Logout from the current session")
    public String logout() {
        authenticationService.logout();
        return "good bye";
    }

    @ShellMethod(key = "current-user", value = "Show current user")
    public String currentUser() {
        Account account = authenticationService.getCurrentAccount();
        if (account != null) {
            return account.toString();
        } else {
            return "No user is currently logged in.";
        }
    }
}