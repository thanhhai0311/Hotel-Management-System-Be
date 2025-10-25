package com.javaweb.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		final String securitySchemeName = "bearerAuth";

		return new OpenAPI()
				.info(new Info().title("Hotel Management System API")
						.description("Tài liệu API cho hệ thống quản lý khách sạn - JavaWeb").version("1.0.0")
						.contact(new Contact().name("JavaWeb Team").email("support@javaweb.com")
								.url("https://javaweb.com"))
						.license(new License().name("Apache 2.0").url("https://springdoc.org")))
				// 🔒 Cấu hình bảo mật JWT cho Swagger
				.addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
				.components(new Components().addSecuritySchemes(securitySchemeName,
						new SecurityScheme().name(securitySchemeName).type(SecurityScheme.Type.HTTP).scheme("bearer")
								.bearerFormat("JWT").description("Nhập token JWT theo định dạng: Bearer {token}")));
	}
}
