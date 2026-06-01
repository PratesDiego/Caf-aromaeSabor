package com.example.demoCafeAromaESabor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 150)
    private String nome;
    
    @Column(length = 500)
    private String descricao;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;
    
    @Column(nullable = false)
    private Integer quantidadeEstoque;
    
    @Column(nullable = false)
    private Integer quantidadeMinima;

    @Column(length = 50)
    private String categoria;
    
    @Column(nullable = false)
    private LocalDateTime dataCadastro;
    
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovimentacaoEstoque> movimentacoes = new ArrayList<>();

    // Explicit getters and setters (in case Lombok is not processed by the environment)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public java.math.BigDecimal getPreco() { return preco; }
    public void setPreco(java.math.BigDecimal preco) { this.preco = preco; }

    public Integer getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(Integer quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }

    public Integer getQuantidadeMinima() { return quantidadeMinima; }
    public void setQuantidadeMinima(Integer quantidadeMinima) { this.quantidadeMinima = quantidadeMinima; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public java.time.LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(java.time.LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public List<MovimentacaoEstoque> getMovimentacoes() { return movimentacoes; }
    public void setMovimentacoes(List<MovimentacaoEstoque> movimentacoes) { this.movimentacoes = movimentacoes; }

    // Indica se o estoque está abaixo ou igual ao mínimo configurado
    public boolean isEstoqueBaixo() {
        if (this.quantidadeEstoque == null || this.quantidadeMinima == null) return false;
        return this.quantidadeEstoque <= this.quantidadeMinima;
    }
}

