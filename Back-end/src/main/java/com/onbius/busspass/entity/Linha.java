package com.onbius.busspass.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "linha")
public class Linha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private String nome;
    private String cor;

    @Column(name = "operadora_id")
    private Long operadoraId;

    public Linha() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public Long getOperadoraId() { return operadoraId; }
    public void setOperadoraId(Long operadoraId) { this.operadoraId = operadoraId; }
}
