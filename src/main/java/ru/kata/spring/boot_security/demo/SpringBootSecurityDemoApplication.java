package ru.kata.spring.boot_security.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.service.UserService;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {
	private final UserService userService;

	public SpringBootSecurityDemoApplication(UserService userService) {
		this.userService = userService;
	}

	@PostConstruct
	@Transactional
	public void init() {
		initRole("ROLE_ADMIN");
		initRole("ROLE_USER");
	}

	private void initRole(String roleName) {
		if (userService.findRoleByName(roleName) == null) {
			Role role = new Role();
			role.setName(roleName);
			userService.saveRole(role);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
	}

}
