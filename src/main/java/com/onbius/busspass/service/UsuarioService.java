package com.onbius.busspass.service;

import com.onbius.busspass.entity.Usuario;
import com.onbius.busspass.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    public List<Usuario> listar() {
        return repo.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Usuario cadastrar(Usuario u) {
        if (u.getNome() == null || u.getNome().length() < 3) {
            throw new RuntimeException("Nome deve ter no mínimo 3 letras.");
        }
        if (u.getEmail() == null || !u.getEmail().contains("@")) {
            throw new RuntimeException("E-mail inválido.");
        }
        if (u.getCpf() == null || u.getCpf().length() != 11) {
            throw new RuntimeException("CPF deve ter 11 dígitos.");
        }
        if (u.getSenha() == null || u.getSenha().length() < 6) {
            throw new RuntimeException("Senha deve ter no mínimo 6 caracteres.");
        }

        if (repo.findByEmail(u.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        return repo.save(u);
    }

    public Usuario login(String email, String senha) {
        Usuario u = repo.findByEmail(email).orElse(null);
        if (u == null) return null;
        if (!u.getSenha().equals(senha)) return null;
        return u;
    }
}
