package com.example.demo.price.aop;

import com.example.demo.account.dto.Account;
import com.example.demo.account.service.AuthenticationService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PriceAop {

    private static final Logger logger = LoggerFactory.getLogger(PriceAop.class);

    private final AuthenticationService authenticationService;

    public PriceAop(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Pointcut("execution(* com.example.demo.shell.MyCommands.*(..)) " +
            "&& !execution(* com.example.demo.shell.MyCommands.login(..)) " +
            "&& !execution(* com.example.demo.shell.MyCommands.logout(..))")
    public void priceRequests() {}

    @Before("priceRequests()")
    public void logAndValidateRequest(JoinPoint joinPoint) {
        Account currentAccount = authenticationService.getCurrentAccount();
        if (currentAccount == null) {
            throw new IllegalStateException("로그인하지 않은 사용자는 이 기능을 사용할 수 없습니다.");
        }

        String username = currentAccount.getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info("요청: {}, 사용자: {}, 인자: {}", methodName, username, args);
    }
}