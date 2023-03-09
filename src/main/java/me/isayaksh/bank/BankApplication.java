package me.isayaksh.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BankApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(BankApplication.class, args);
//		String[] beanDefinitionNames = context.getBeanDefinitionNames();
//		System.out.println("bean factory에 등록된 bean");
//		for (String beanDefinitionName : beanDefinitionNames) {
//			System.out.println("beanDefinitionName = " + beanDefinitionName);
//		}
	}

}
