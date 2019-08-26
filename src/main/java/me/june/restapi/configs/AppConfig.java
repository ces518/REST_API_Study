package me.june.restapi.configs;

import me.june.restapi.accounts.Account;
import me.june.restapi.accounts.AccountRole;
import me.june.restapi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

/**
 * AppConfig.java
 * AppConfig
 * ==============================================
 *
 * @author PJY
 * @history 작성일            작성자     변경내용
 * @history 2019-08-25         PJY      최초작성
 * ==============================================
 */
@Configuration
public class AppConfig {

    /**
     * 스프링 시큐리티 최신버전에 들어간 패스워드인코더
     * 인코딩 패스워드 문자열 앞에 어떤 방식으로 인코딩이 된건지 prefix 를 적용한다.
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder () {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner () {
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Set roles = new HashSet();
                roles.add(AccountRole.ADMIN);
                roles.add(AccountRole.USER);
                Account account = Account.builder()
                        .email("pupupee9@gmail.com")
                        .password("june")
                        .roles(roles).build();
                accountService.saveAccount(account);
            }
        };
    }
}
