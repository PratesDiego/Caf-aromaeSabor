package com.example.demoCafeAromaESabor.repository;

import com.example.demoCafeAromaESabor.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Método personalizado: buscar usuário por username
    Usuario findByUsername(String username);
    
    // Método personalizado: verificar se username existe
    boolean existsByUsername(String username);
    
    // Método personalizado: buscar por CPF
    Usuario findByCpf(String cpf);
}

