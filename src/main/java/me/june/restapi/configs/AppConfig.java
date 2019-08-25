package me.june.restapi.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

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

}
