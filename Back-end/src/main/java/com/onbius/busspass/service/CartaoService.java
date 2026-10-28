package com.onbius.busspass.service;

import com.onbius.busspass.entity.Cartao;
import com.onbius.busspass.repository.CartaoRepository;
import com.onbius.busspass.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartaoService {

    private final CartaoRepository repo;
    private final UsuarioService usuarioService;

    public CartaoService(CartaoRepository repo, UsuarioService usuarioService) {
        this.repo = repo;
        this.usuarioService = usuarioService;
    }

    public List<Cartao> listarPorUsuario(Long usuarioId) {
        return repo.findByUsuarioId(usuarioId);
    }

    public Cartao criar(Cartao cartao) {
        if (cartao.getNumero() == null || cartao.getNumero().length() < 6) {
            throw new RuntimeException("Número do cartão muito curto.");
        }
        if (cartao.getUsuarioId() == null) {
            throw new RuntimeException("Cartão precisa estar ligado a um usuário.");
        }
        if (usuarioService.buscarPorId(cartao.getUsuarioId()) == null) {
            throw new RuntimeException("Usuário não encontrado.");
        }
        if (cartao.getSaldoCentavos() == null || cartao.getSaldoCentavos() < 0) {
            cartao.setSaldoCentavos(0);
        }
        return repo.save(cartao);
    }

    public Integer recarregar(Long id, Integer valorCentavos) {
        if (valorCentavos == null || valorCentavos <= 0) {
            throw new RuntimeException("Valor da recarga deve ser positivo.");
        }
        if (valorCentavos > 100000) {
            throw new RuntimeException("Recarga máxima por vez: R$ 1.000,00.");
        }

        Cartao c = repo.findById(id).orElseThrow(() ->
            new RuntimeException("Cartão não encontrado."));

        int novoSaldo = c.getSaldoCentavos() + valorCentavos;
        c.setSaldoCentavos(novoSaldo);
        repo.save(c);
        return novoSaldo;
    }

    public Integer debitar(String numero, Integer valorCentavos) {
        if (valorCentavos == null || valorCentavos <= 0) {
            throw new RuntimeException("Valor do débito deve ser positivo.");
        }

        Cartao c = repo.findByNumero(numero).orElseThrow(() ->
            new RuntimeException("Cartão RFID não cadastrado."));

        if (c.getSaldoCentavos() < valorCentavos) {
            throw new RuntimeException("Saldo insuficiente.");
        }

        int novoSaldo = c.getSaldoCentavos() - valorCentavos;
        c.setSaldoCentavos(novoSaldo);
        repo.save(c);
        return novoSaldo;
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}
