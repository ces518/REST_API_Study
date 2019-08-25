package me.june.restapi.configs;

import me.june.restapi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * SecurityConfig.java
 * SecurityConfig
 * ==============================================
 *
 * @author PJY
 * @history 작성일            작성자     변경내용
 * @history 2019-08-25         PJY      최초작성
 * ==============================================
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /* UserDetailsService */
    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public TokenStore tokenStore () {
        return new InMemoryTokenStore();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService)
                .passwordEncoder(passwordEncoder);
    }

    /*
        정적인 리소스들은 필터적용 무시
        Filter Chain 을 타지않고 Filter 이전에 걸러버림
    */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/doc/index.html");
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /* http 로 거르면 filter로 들어온 상태
        Security Filter Chain 을 탄다.
    * */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .anonymous()
                .and()
            .formLogin()
                .and()
            .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/api/**").anonymous()
                .anyRequest().authenticated();
    }
}
