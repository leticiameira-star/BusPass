package com.onbius.busspass.controller;

import com.onbius.busspass.entity.Ponto;
import com.onbius.busspass.service.PontoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pontos")
@CrossOrigin(origins = "*")
public class PontoController {

    private final PontoService service;

    public PontoController(PontoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Ponto> listar() {
        return service.listar();
    }
}