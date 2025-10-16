package com.example.demo.account.service;

import com.example.demo.account.dto.Account;
import com.example.demo.common.dataparser.DataParser;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AuthenticationService {

    private final List<Account> accounts;
    @Getter
    private Account currentAccount;

    public AuthenticationService(DataParser dataParser) {
        this.accounts = dataParser.accounts();
    }

    public Account login(Long id, String password) {
        // 계정 목록에서 id와 password가 일치하는 첫 번째 사용자를 찾습니다.
        this.currentAccount = accounts.stream()
                .filter(account -> Objects.equals(account.getId(), id) && Objects.equals(account.getPassword(), password))
                .findFirst()
                .orElse(null);
        return this.currentAccount;
    }

    public void logout() {
        this.currentAccount = null;
    }


}
