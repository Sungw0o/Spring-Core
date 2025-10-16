package com.example.demo.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class MyCommands {

    @ShellMethod
    public String login(long id, String password) {
        return null;
    }

    @ShellMethod
    public String logout() {
        return null;
    }

    @ShellMethod
    public String currentUser() {
        return null;
    }

    @ShellMethod
    public String city() {
        return null;
    }

    @ShellMethod
    public String sector(String city) {
        return null;
    }

    @ShellMethod
    public String price(String city, String sector) {
        return null;
    }

    @ShellMethod
    public String billTotal(String city, String sector, int usage) {
        return null;
    }


}