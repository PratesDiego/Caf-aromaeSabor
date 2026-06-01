package com.example.demoCafeAromaESabor.repository;

import com.example.demoCafeAromaESabor.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    // Buscar produto por nome (case-insensitive)
    List<Produto> findByNomeContainingIgnoreCase(String nome);
    
    // Buscar produtos por categoria
    List<Produto> findByCategoria(String categoria);
    
    // Buscar produtos com estoque disponível
    List<Produto> findByQuantidadeEstoqueGreaterThanEqual(Integer quantidade);
}

