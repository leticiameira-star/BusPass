package com.onbius.busspass.service;

import com.onbius.busspass.entity.Linha;
import com.onbius.busspass.repository.LinhaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinhaService {

    private final LinhaRepository repo;

    public LinhaService(LinhaRepository repo) {
        this.repo = repo;
    }

    public List<Linha> listar() {
        return repo.findAll();
    }
}