package me.june.restapi.configs;


import me.june.restapi.accounts.Account;
import me.june.restapi.accounts.AccountRole;
import me.june.restapi.accounts.AccountService;
import me.june.restapi.common.BaseControllerTest;
import me.june.restapi.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OAuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AppProperties appProperties;

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken () throws Exception {

        final String USER_NAME = appProperties.getUserUsername();
        final String PASSWORD = appProperties.getUserPassword();
        // httpBasic 메서드를 사용하여 basicOauth 헤더를 만듬
        final String CLIENT_ID = appProperties.getClientId();
        final String CLIENT_SECRET = appProperties.getClientSecret();

        /*
        // given
        Set roles = new HashSet();
        roles.add(AccountRole.ADMIN);
        roles.add(AccountRole.USER);
        Account account = Account.builder()
                .email(USER_NAME)
                .password(PASSWORD)
                .roles(roles)
                .build();
        accountService.saveAccount(account);
         */

        this.mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(CLIENT_ID, CLIENT_SECRET)) // httpBasic 사용시 test dependency 필요
                    .param("username", USER_NAME)
                    .param("password", PASSWORD)
                    .param("grant_type", "password")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists());
    }

}
