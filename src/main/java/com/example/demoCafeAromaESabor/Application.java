package com.example.demoCafeAromaESabor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.example.demoCafeAromaESabor.model.Usuario;
import com.example.demoCafeAromaESabor.repository.UsuarioRepository;
import java.time.LocalDateTime;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner initializeData(UsuarioRepository usuarioRepository) {
		return args -> {
			// Verifica se já existe usuário admin
			if (usuarioRepository.findByUsername("admin") == null) {
				Usuario usuario = new Usuario();
				usuario.setUsername("admin");
				usuario.setSenha("senha123");
				usuario.setNome("Administrador");
				usuario.setCpf("12345678901");
				usuario.setDataCadastro(LocalDateTime.now());
				usuarioRepository.save(usuario);
				System.out.println("✅ Usuário admin criado com sucesso!");
			} else {
				System.out.println("✅ Usuário admin já existe!");
			}
		};
	}
}
