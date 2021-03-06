package com.parus.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.parus.store"))
				//Danger : the below line of code needs to be enabled
				//.paths(PathSelectors.regex("/api.*"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(metaInfo());
	}

	/**
	 * Return the meta info about the API. This should reflect
	 */
	private ApiInfo metaInfo() {
		return new ApiInfoBuilder()
				.description("Backend API For the Auth/User Service")
				.title("Auth/User API")
				.version("Unreleased [WIP]")
				.build();
	}

}
