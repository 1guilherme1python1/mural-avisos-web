package com.muralcomunitario.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "comunicados")
public class Comunicado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O titulo e obrigatorio.")
    @Size(max = 120, message = "O titulo deve ter no maximo 120 caracteres.")
    @Column(nullable = false, length = 120)
    private String titulo;

    @NotBlank(message = "A descricao e obrigatoria.")
    @Size(max = 1000, message = "A descricao deve ter no maximo 1000 caracteres.")
    @Column(nullable = false, length = 1000)
    private String descricao;

    @NotBlank(message = "A categoria e obrigatoria.")
    @Size(max = 50, message = "A categoria deve ter no maximo 50 caracteres.")
    @Column(nullable = false, length = 50)
    private String categoria;

    @NotBlank(message = "A localizacao e obrigatoria.")
    @Size(max = 150, message = "A localizacao deve ter no maximo 150 caracteres.")
    @Column(nullable = false, length = 150)
    private String localizacao;

    @NotBlank(message = "O nome do responsavel e obrigatorio.")
    @Size(max = 100, message = "O nome do responsavel deve ter no maximo 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nomeResponsavel;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Size(max = 20, message = "O status deve ter no maximo 20 caracteres.")
    @Column(nullable = false, length = 20)
    private String status;

    @PrePersist
    public void prePersist() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
        if (status == null || status.isBlank()) {
            status = "Ativo";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
