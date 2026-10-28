package com.onbius.busspass.controller;

import com.onbius.busspass.entity.Usuario;
import com.onbius.busspass.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioService service;

    public AuthController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Usuario dados) {
        Usuario salvo = service.cadastrar(dados);

        Map<String, Object> resp = new HashMap<>();
        resp.put("id", salvo.getId());
        resp.put("nome", salvo.getNome());
        resp.put("email", salvo.getEmail());
        resp.put("cpf", salvo.getCpf());
        return resp;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> dados) {
        Usuario u = service.login(dados.get("email"), dados.get("senha"));
        if (u == null) {
            throw new RuntimeException("E-mail ou senha inválidos.");
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("id", u.getId());
        resp.put("nome", u.getNome());
        resp.put("email", u.getEmail());
        resp.put("cpf", u.getCpf());
        return resp;
    }
}