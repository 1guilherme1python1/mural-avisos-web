package com.muralcomunitario.backend.service;

import com.muralcomunitario.backend.exception.ComunicadoNotFoundException;
import com.muralcomunitario.backend.model.Comunicado;
import com.muralcomunitario.backend.repository.ComunicadoRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Set;

@Service
public class ComunicadoService {

    private static final Set<String> CATEGORIAS_VALIDAS = Set.of(
            "Falta de agua",
            "Seguranca",
            "Infraestrutura",
            "Iluminacao publica",
            "Coleta de lixo",
            "Outros"
    );

    private static final Set<String> STATUS_VALIDOS = Set.of(
            "Ativo",
            "Resolvido"
    );

    private final ComunicadoRepository comunicadoRepository;

    public ComunicadoService(ComunicadoRepository comunicadoRepository) {
        this.comunicadoRepository = comunicadoRepository;
    }

    public List<Comunicado> listarTodos() {
        return comunicadoRepository.findAllByOrderByDataCriacaoDesc();
    }

    public Comunicado buscarPorId(Long id) {
        return comunicadoRepository.findById(id)
                .orElseThrow(() -> new ComunicadoNotFoundException(id));
    }

    public Comunicado criar(Comunicado comunicado) {
        if (comunicado.getStatus() == null || comunicado.getStatus().isBlank()) {
            comunicado.setStatus("Ativo");
        }

        validarCategoria(comunicado.getCategoria());
        validarStatus(comunicado.getStatus());
        return comunicadoRepository.save(comunicado);
    }

    public Comunicado atualizar(Long id, Comunicado comunicadoAtualizado) {
        Comunicado comunicado = buscarPorId(id);
        validarCategoria(comunicadoAtualizado.getCategoria());
        validarStatus(comunicadoAtualizado.getStatus());

        comunicado.setTitulo(comunicadoAtualizado.getTitulo());
        comunicado.setDescricao(comunicadoAtualizado.getDescricao());
        comunicado.setCategoria(comunicadoAtualizado.getCategoria());
        comunicado.setLocalizacao(comunicadoAtualizado.getLocalizacao());
        comunicado.setNomeResponsavel(comunicadoAtualizado.getNomeResponsavel());
        comunicado.setStatus(comunicadoAtualizado.getStatus());

        return comunicadoRepository.save(comunicado);
    }

    public void remover(Long id) {
        Comunicado comunicado = buscarPorId(id);
        comunicadoRepository.delete(comunicado);
    }

    public List<Comunicado> listarPorCategoria(String categoria) {
        validarCategoria(categoria);
        return comunicadoRepository.findAllByOrderByDataCriacaoDesc().stream()
                .filter(comunicado -> normalizar(comunicado.getCategoria()).equals(normalizar(categoria)))
                .toList();
    }

    public List<Comunicado> listarPorStatus(String status) {
        validarStatus(status);
        return comunicadoRepository.findAllByOrderByDataCriacaoDesc().stream()
                .filter(comunicado -> normalizar(comunicado.getStatus()).equals(normalizar(status)))
                .toList();
    }

    public Comunicado resolver(Long id) {
        Comunicado comunicado = buscarPorId(id);
        comunicado.setStatus("Resolvido");
        return comunicadoRepository.save(comunicado);
    }

    private void validarCategoria(String categoria) {
        if (categoria == null || CATEGORIAS_VALIDAS.stream().noneMatch(item -> normalizar(item).equals(normalizar(categoria)))) {
            throw new IllegalArgumentException("Categoria invalida.");
        }
    }

    private void validarStatus(String status) {
        String statusNormalizado = (status == null || status.isBlank()) ? "Ativo" : status;
        if (STATUS_VALIDOS.stream().noneMatch(item -> normalizar(item).equals(normalizar(statusNormalizado)))) {
            throw new IllegalArgumentException("Status invalido.");
        }
    }

    private String normalizar(String valor) {
        return Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }
}
