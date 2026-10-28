package com.onbius.busspass.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cartao")
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId;

    private String numero;

    @Column(name = "saldo_centavos")
    private Integer saldoCentavos = 0;

    public Cartao() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public Integer getSaldoCentavos() { return saldoCentavos; }
    public void setSaldoCentavos(Integer saldoCentavos) { this.saldoCentavos = saldoCentavos; }
}
