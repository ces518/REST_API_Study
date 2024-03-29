package me.june.restapi.accounts;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.useDefaultDateFormatsOnly;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

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

        this.accountService.saveAccount(account);

        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);


        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
    }

    @Rule
    public ExpectedException expectedException;

    @Test
    public void findByUsernameFail () {

        final String username = "random@gmail.com";

        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));

        accountService.loadUserByUsername(username);
    }
}
