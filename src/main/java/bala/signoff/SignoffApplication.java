package bala.signoff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan( basePackages = {"bala.signoff"})
public class SignoffApplication
{
	public static void main(String[] args) {
		SpringApplication.run(SignoffApplication.class, args);
	}
}
