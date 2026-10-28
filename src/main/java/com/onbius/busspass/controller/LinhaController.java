package com.onbius.busspass.controller;

import com.onbius.busspass.entity.Linha;
import com.onbius.busspass.service.LinhaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/linhas")
@CrossOrigin(origins = "*")
public class LinhaController {

    private final LinhaService service;

    public LinhaController(LinhaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Linha> listar() {
        return service.listar();
    }
}