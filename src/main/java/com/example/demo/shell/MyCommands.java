package com.example.demo.shell;

import com.example.demo.account.dto.Account;
import com.example.demo.account.service.AuthenticationService;
import com.example.demo.price.dto.Price; // Price DTO를 import 합니다.
import com.example.demo.price.service.PriceService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;

@ShellComponent
public class MyCommands {

    private final AuthenticationService authenticationService;
    private final PriceService priceService;

    public MyCommands(AuthenticationService authenticationService, PriceService priceService) {
        this.authenticationService = authenticationService;
        this.priceService = priceService;
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

    @ShellMethod(key = "city", value = "Show all available cities.")
    public String city() {
        List<String> cities = priceService.cities();
        return formatListToString(cities);
    }

    @ShellMethod(key = "sector", value = "Show all available sectors for a given city.")
    public String sector(@ShellOption(help = "The name of the city.") String city) {
        List<String> sectors = priceService.sectors(city);
        return formatListToString(sectors);
    }


    @ShellMethod(key = "price", value = "Show price info for a given city and sector.")
    public String price(@ShellOption(help = "The name of the city.") String city,
                        @ShellOption(help = "The name of the sector.") String sector) {
        Price priceInfo = priceService.price(city, sector);
        if (priceInfo != null) {
            return priceInfo.toString();
        } else {
            return "해당하는 요금 정보를 찾을 수 없습니다.";
        }
    }

    @ShellMethod(key = "bill-total", value = "Calculate the total bill for a given city, sector, and usage.")
    public String billTotal(@ShellOption(help = "The name of the city.") String city,
                            @ShellOption(help = "The name of the sector.") String sector,
                            @ShellOption(help = "The usage in cubic meters.") int usage) {
        return priceService.billTotal(city, sector, usage);
    }

    private String formatListToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        return "[" + String.join(", ", list) + "]";
    }
}