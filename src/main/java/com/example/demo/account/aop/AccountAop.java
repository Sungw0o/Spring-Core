package com.example.demo.account.aop;


import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AccountAop {

    private static final Logger logger = LoggerFactory.getLogger(AccountAop.class);

    @Pointcut("execution(* com.example.demo.account.service.AuthenticationService.login(..))")
    public void loginPointcut() {}

    @Pointcut("execution(* com.example.demo.account.service.AuthenticationService.logout(..))")
    public void logoutPointcut() {}

    @AfterReturning("loginPointcut() && args(id, password)")
    public void logLogin(long id, String password) {
        logger.info("login([{}, {}])", id, password);
    }

    @AfterReturning("logoutPointcut()")
    public void logLogout() {
        logger.info("logout([])");
    }

}