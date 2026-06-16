package com.muralcomunitario.backend.controller;

import com.muralcomunitario.backend.model.Comunicado;
import com.muralcomunitario.backend.service.ComunicadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comunicados")
public class ComunicadoController {

    private final ComunicadoService comunicadoService;

    public ComunicadoController(ComunicadoService comunicadoService) {
        this.comunicadoService = comunicadoService;
    }

    @GetMapping
    public List<Comunicado> listarTodos() {
        return comunicadoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Comunicado buscarPorId(@PathVariable Long id) {
        return comunicadoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comunicado criar(@Valid @RequestBody Comunicado comunicado) {
        return comunicadoService.criar(comunicado);
    }

    @PutMapping("/{id}")
    public Comunicado atualizar(@PathVariable Long id, @Valid @RequestBody Comunicado comunicado) {
        return comunicadoService.atualizar(id, comunicado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        comunicadoService.remover(id);
    }

    @GetMapping("/categoria/{categoria}")
    public List<Comunicado> listarPorCategoria(@PathVariable String categoria) {
        return comunicadoService.listarPorCategoria(categoria);
    }

    @GetMapping("/status/{status}")
    public List<Comunicado> listarPorStatus(@PathVariable String status) {
        return comunicadoService.listarPorStatus(status);
    }

    @PostMapping("/{id}/resolver")
    public Comunicado resolver(@PathVariable Long id) {
        return comunicadoService.resolver(id);
    }
}
