package com.example.demoCafeAromaESabor.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.demoCafeAromaESabor.repository.UsuarioRepository;
import com.example.demoCafeAromaESabor.model.Usuario;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String autenticar(@RequestParam String username, 
                            @RequestParam String password,
                            HttpSession session,
                            RedirectAttributes ra) {
        
        // Buscar usuário no banco
        Usuario usuario = usuarioRepository.findByUsername(username);
        
        // Validar credenciais
        if (usuario != null && usuario.getSenha().equals(password)) {
            // Login bem-sucedido - armazenar na sessão
            session.setAttribute("usuarioLogado", usuario);
            session.setAttribute("usuarioId", usuario.getId());
            session.setAttribute("usuarioNome", usuario.getNome());
            
            ra.addFlashAttribute("msg", "Login realizado com sucesso!");
            return "redirect:/home";
        } else {
            // Login falhou
            ra.addFlashAttribute("erro", "Usuário ou senha incorretos");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes ra) {
        session.invalidate();
        ra.addFlashAttribute("msg", "Logout realizado com sucesso");
        return "redirect:/login";
    }
}
