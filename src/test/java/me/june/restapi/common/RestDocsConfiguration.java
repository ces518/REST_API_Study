package me.june.restapi.common;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2019-08-15
 * Time: 20:02
 **/
@TestConfiguration
public class RestDocsConfiguration {

    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer () {
        return new RestDocsMockMvcConfigurationCustomizer() {
            @Override
            public void customize(MockMvcRestDocumentationConfigurer configurer) {
                configurer.operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint());
            }
        };
    }
}
