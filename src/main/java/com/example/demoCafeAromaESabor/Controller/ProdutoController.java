package com.example.demoCafeAromaESabor.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.demoCafeAromaESabor.repository.ProdutoRepository;
import com.example.demoCafeAromaESabor.model.Produto;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    // LISTAGEM
    @GetMapping("/produto")
    public String listagem(Model model) {
        model.addAttribute("produtos", produtoRepository.findAll());
        return "produto/listagem";
    }

    // FORMULÁRIO DE INSERÇÃO
    @GetMapping("/produto/form-inserir")
    public String formInserir(Model model) {
        model.addAttribute("produto", new Produto());
        return "produto/form-inserir";
    }

    // INSERIR (POST) - form posts to /produto
    @PostMapping("/produto")
    public String inserir(@ModelAttribute Produto produto, org.springframework.validation.BindingResult bindingResult, Model model, RedirectAttributes ra) {
        // if there were binding/validation errors, return to the form with errors displayed
        if (bindingResult.hasErrors()) {
            model.addAttribute("produto", produto);
            model.addAttribute("erro", "Erro ao processar o formulário. Verifique os campos.");
            return "produto/form-inserir";
        }
        try {
            if (produto.getDataCadastro() == null) {
                produto.setDataCadastro(LocalDateTime.now());
            }
            // garantir quantidade em estoque inicial
            if (produto.getQuantidadeEstoque() == null) {
                produto.setQuantidadeEstoque(0);
            }
            // garantir quantidade mínima
            if (produto.getQuantidadeMinima() == null) {
                produto.setQuantidadeMinima(0);
            }
            produtoRepository.save(produto);
            ra.addFlashAttribute("msg", "Produto salvo com sucesso");
            return "redirect:/produto";
        } catch (Exception ex) {
            // log the exception (if logging is configured) and show a friendly message
            ra.addFlashAttribute("erro", "Erro ao salvar produto: " + ex.getMessage());
            return "redirect:/produto/form-inserir";
        }
    }

    // FORMULÁRIO DE ALTERAÇÃO (aceita id como request param)
    @GetMapping("/produto/form-alterar")
    public String formAlterar(@RequestParam Long id, Model model, RedirectAttributes ra) {
        Optional<Produto> op = produtoRepository.findById(id);
        if (op.isEmpty()) {
            ra.addFlashAttribute("erro", "Produto não encontrado");
            return "redirect:/produto";
        }
        model.addAttribute("produto", op.get());
        return "produto/form-alterar";
    }

    // ALTERAR (POST)
    @PostMapping("/produto/alterar")
    public String alterar(@ModelAttribute Produto produto, RedirectAttributes ra) {
        // preservar campos que não vem do formulário (ex: quantidadeEstoque, dataCadastro)
        if (produto.getId() != null) {
            Optional<Produto> op = produtoRepository.findById(produto.getId());
            if (op.isPresent()) {
                Produto existente = op.get();
                // atualizar apenas campos do formulário
                existente.setNome(produto.getNome());
                existente.setDescricao(produto.getDescricao());
                existente.setPreco(produto.getPreco());
                existente.setCategoria(produto.getCategoria());
                // manter dataCadastro e quantidadeEstoque
                produtoRepository.save(existente);
                ra.addFlashAttribute("msg", "Produto alterado com sucesso");
                return "redirect:/produto";
            }
        }
        // se não existir id, salva como novo
        if (produto.getQuantidadeEstoque() == null) produto.setQuantidadeEstoque(0);
        if (produto.getDataCadastro() == null) produto.setDataCadastro(LocalDateTime.now());
        produtoRepository.save(produto);
        ra.addFlashAttribute("msg", "Produto alterado com sucesso");
        return "redirect:/produto";
    }

    // EXCLUIR (template chama /produto/deletar?id=...)
    @GetMapping("/produto/deletar")
    public String excluir(@RequestParam Long id, RedirectAttributes ra) {
        produtoRepository.deleteById(id);
        ra.addFlashAttribute("msg", "Produto excluído com sucesso");
        return "redirect:/produto";
    }
}
