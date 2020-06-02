package com.jt.tours.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;

import static springfox.documentation.builders.PathSelectors.any;

/**
 * Swagger configuration for this application.
 *
 * Created by Jason Tao on 6/1/2020
 */
@Configuration
@EnableSwagger2WebMvc
@Import(SpringDataRestConfiguration.class)
public class SwaggerConfiguration {

    private static final String API_VERSION = "1.0";

    /**
     * Configures the swagger setting.
     *
     * @return The docket as the primary interface for swagger.
     */
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jt.tours"))
                .paths(any()).build()
                .apiInfo(apiInfo());
    }

    /**
     * The api information about this application.
     *
     * @return the ApiInfo object
     */
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Tour Package APIs",
                "API for the Tour Package Travel Service", API_VERSION, null,
                new Contact("Jason Tao", null, "jastaoathk@gmail.com"),
                null, null, new ArrayList<>());
    }

}
