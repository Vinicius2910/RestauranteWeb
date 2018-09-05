package br.com.sistemaTCC;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.LocaleResolver;
@SpringBootApplication
public class SistemaTccApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaTccApplication.class, args);
	}
	
	@Bean
	public LocaleResolver localeResolver() {
		return new FixedLocaleResolver(new Locale("pt", "BR"));
	}
	
}
