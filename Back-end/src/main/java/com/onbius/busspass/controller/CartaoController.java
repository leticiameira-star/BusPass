package com.onbius.busspass.controller;

import com.onbius.busspass.entity.Cartao;
import com.onbius.busspass.service.CartaoService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cartoes")
@CrossOrigin(origins = "*")
public class CartaoController {

    private final CartaoService service;

    public CartaoController(CartaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Cartao> listar(@RequestParam Long usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }

    @PostMapping
    public Cartao criar(@RequestBody Cartao cartao) {
        return service.criar(cartao);
    }

    @PostMapping("/pagar")
    public Map<String, Object> pagar(@RequestBody Map<String, Object> dados) {
        String numero = (String) dados.get("numero");
        Integer valor = (Integer) dados.get("valorCentavos");

        Integer novoSaldo = service.debitar(numero, valor);

        Map<String, Object> resp = new HashMap<>();
        resp.put("sucesso", true);
        resp.put("novoSaldoCentavos", novoSaldo);
        resp.put("saldoFormatado", String.format("R$ %.2f", novoSaldo / 100.0));
        return resp;
    }

    @PostMapping("/{id}/recarga")
    public Map<String, Object> recarregar(@PathVariable Long id,
                                          @RequestBody Map<String, Integer> dados) {
        Integer novoSaldo = service.recarregar(id, dados.get("valorCentavos"));
        Map<String, Object> resp = new HashMap<>();
        resp.put("novoSaldoCentavos", novoSaldo);
        return resp;
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}