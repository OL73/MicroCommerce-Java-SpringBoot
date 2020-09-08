package com.ecommerce.microcommerce.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig { // permet de personnaliser la documenation Swagger2
    
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ecommerce.microcommerce")) // permet de filtrer selon des prédicats, on filtre ici sur le package om.ecommerce.microcommerce (on peut éventuellement récupérer la doc sur tout, yc les erreurs avec RequestHandlerSelectors.any()
                .paths(PathSelectors.regex("/produits.*")) // permet de filtrer selon les URI (ici uniquement vers /produits...), par défaut c'est PathSelectors.any() qui permet de documenter toutes les URI
                .build();
    }

}