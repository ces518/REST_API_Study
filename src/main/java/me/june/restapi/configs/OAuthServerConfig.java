package me.june.restapi.configs;

import me.june.restapi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2019-08-26
 * Time: 21:20
 **/
@Configuration
@EnableAuthorizationServer
public class OAuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AccountService accountService;

    @Autowired
    TokenStore tokenStore;

    @Autowired
    AppProperties appProperties;

    // 패스워드 인코더를 사용하여 클라이언트 시크릿을 확인한다.
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(passwordEncoder);
    }

    /**
     * inMemory 로 관리
     * clientId는 myApp
     * grant-type은 password와 refresh_token 방식 지원
     * scopes 은 정의하기 나름 read, write
     * client-secret은 passwordEncoder로 encoding
     * accessToken 유효시간 10분설정
     * refreshToken 유효시간 1시간
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(appProperties.getClientId())
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read", "write")
                .secret(this.passwordEncoder.encode(appProperties.getClientSecret()))
                .accessTokenValiditySeconds(10 * 60)
                .refreshTokenValiditySeconds(6 * 10 * 60)
        ;
    }

    /**
     * authenticationManager : 사용자정보를 가지고있음
     *  - 유저정보를 확인해야 토큰을 발급받을수 있음
     * accountService : userDetailsService
     * tokenStore : 토큰을 저장할 tokenStore
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(accountService)
                .tokenStore(tokenStore);
    }
}
