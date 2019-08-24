package me.june.restapi.accounts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void loadUserByUsername () {
        // given
        final String username = "juneyoung@email.com";
        final String password = "juneyoung";

        Set roles = new HashSet();
        roles.add(AccountRole.ADMIN);
        roles.add(AccountRole.USER);

        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(roles)
                .build();

        this.accountRepository.save(account);

        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);


        assertThat(userDetails.getPassword()).isEqualTo(userDetails.getPassword());
    }
}
