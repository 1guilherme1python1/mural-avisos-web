package com.muralcomunitario.backend.repository;

import com.muralcomunitario.backend.model.Comunicado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComunicadoRepository extends JpaRepository<Comunicado, Long> {
    List<Comunicado> findAllByOrderByDataCriacaoDesc();

    List<Comunicado> findByCategoriaIgnoreCaseOrderByDataCriacaoDesc(String categoria);

    List<Comunicado> findByStatusIgnoreCaseOrderByDataCriacaoDesc(String status);
}
