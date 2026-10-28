package com.onbius.busspass.service;

import com.onbius.busspass.entity.Ponto;
import com.onbius.busspass.repository.PontoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PontoService {

    private final PontoRepository repo;

    public PontoService(PontoRepository repo) {
        this.repo = repo;
    }

    public List<Ponto> listar() {
        return repo.findAll();
    }
}