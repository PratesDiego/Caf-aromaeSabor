package com.example.demoCafeAromaESabor.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.demoCafeAromaESabor.repository.MovimentacaoEstoqueRepository;
import com.example.demoCafeAromaESabor.repository.ProdutoRepository;
import com.example.demoCafeAromaESabor.repository.UsuarioRepository;
import com.example.demoCafeAromaESabor.model.MovimentacaoEstoque;
import com.example.demoCafeAromaESabor.model.Produto;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class EstoqueController {

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    // LISTAGEM DE MOVIMENTAÇÕES
    @GetMapping("/estoque")
    public String movimentacao(Model model) {
        model.addAttribute("movimentacoes", movimentacaoRepository.findAll());
        model.addAttribute("produtos", produtoRepository.findAll());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "estoque/movimentacao";
    }

    // FORMULÁRIO DE INSERÇÃO DE MOVIMENTAÇÃO
    @GetMapping("/estoque/form-inserir")
    public String formInserir(Model model) {
        model.addAttribute("movimentacao", new MovimentacaoEstoque());
        model.addAttribute("produtos", produtoRepository.findAll());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "estoque/form-inserir";
    }

    // INSERIR MOVIMENTAÇÃO (POST) - endpoint esperado pelo template: /estoque/movimentar
    @PostMapping("/estoque/movimentar")
    public String movimentar(@RequestParam Long produtoId,
                             @RequestParam Integer quantidade,
                             @RequestParam String tipo,
                             RedirectAttributes ra) {

        // Validações básicas
        if (quantidade == null || quantidade <= 0) {
            ra.addFlashAttribute("erro", "Quantidade deve ser maior que 0");
            return "redirect:/estoque";
        }

        if (tipo == null || (!"ENTRADA".equals(tipo) && !"SAIDA".equals(tipo))) {
            ra.addFlashAttribute("erro", "Tipo de movimentação inválido (deve ser ENTRADA ou SAIDA)");
            return "redirect:/estoque";
        }

        Optional<Produto> op = produtoRepository.findById(produtoId);
        if (op.isEmpty()) {
            ra.addFlashAttribute("erro", "Produto não encontrado");
            return "redirect:/estoque";
        }

        Produto produto = op.get();
        String tipoUpper = tipo.toUpperCase();

        // Atualizar estoque do produto
        if ("ENTRADA".equals(tipoUpper)) {
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidade);
        } else if ("SAIDA".equals(tipoUpper)) {
            int novoEstoque = produto.getQuantidadeEstoque() - quantidade;
            if (novoEstoque < 0) {
                ra.addFlashAttribute("erro", "Estoque insuficiente para esta saída. Estoque atual: " + produto.getQuantidadeEstoque());
                return "redirect:/estoque";
            }
            produto.setQuantidadeEstoque(novoEstoque);
        }

        produtoRepository.save(produto);

        // Registrar movimentação
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setTipoMovimentacao(tipoUpper);
        movimentacao.setDataMovimentacao(LocalDateTime.now());
        // usuario não fornecido pelo formulário; ficará nulo
        movimentacaoRepository.save(movimentacao);

        ra.addFlashAttribute("msg", "Movimentação registrada com sucesso!");
        return "redirect:/estoque";
    }

    // EXCLUIR MOVIMENTAÇÃO
    @GetMapping("/estoque/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        movimentacaoRepository.deleteById(id);
        ra.addFlashAttribute("msg", "Movimentação excluída");
        return "redirect:/estoque";
    }
}
