package com.example.demoCafeAromaESabor.repository;

import com.example.demoCafeAromaESabor.model.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {
    
    // Buscar movimentações de um produto específico
    List<MovimentacaoEstoque> findByProdutoId(Long produtoId);
    
    // Buscar movimentações de um usuário específico
    List<MovimentacaoEstoque> findByUsuarioId(Long usuarioId);
    
    // Buscar movimentações por tipo
    List<MovimentacaoEstoque> findByTipoMovimentacao(String tipoMovimentacao);
    
    // Buscar movimentações entre datas
    List<MovimentacaoEstoque> findByDataMovimentacaoBetween(
        LocalDateTime dataInicio, 
        LocalDateTime dataFim
    );
}

