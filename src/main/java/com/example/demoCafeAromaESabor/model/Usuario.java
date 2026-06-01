package com.example.demoCafeAromaESabor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String username;
    
    @Column(nullable = false, length = 255)
    private String senha;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(nullable = false, length = 15, unique = true)
    private String cpf;
    
    @Column(nullable = false)
    private LocalDateTime dataCadastro;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovimentacaoEstoque> movimentacoes = new ArrayList<>();
}

