package com.muralcomunitario.backend.config;

import com.muralcomunitario.backend.model.Comunicado;
import com.muralcomunitario.backend.repository.ComunicadoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadInitialData(ComunicadoRepository comunicadoRepository) {
        return args -> {
            if (comunicadoRepository.count() > 0) {
                return;
            }

            comunicadoRepository.saveAll(List.of(
                    criarComunicado(
                            "Abastecimento com interrupcao programada no Bairro Bosque",
                            "Moradores relataram aviso de interrupcao no fornecimento de agua entre 8h e 14h para manutencao preventiva na rede de distribuicao.",
                            "Falta de agua",
                            "Bairro Bosque, proximidades da Rua Quintino Bocaiuva, Rio Branco/AC",
                            "Associacao de Moradores do Bosque",
                            LocalDateTime.now().minusDays(2),
                            "Ativo"
                    ),
                    criarComunicado(
                            "Buraco ampliado na Avenida Ceara exige sinalizacao",
                            "Um buraco na pista principal tem causado desvios bruscos de veiculos e risco para motociclistas, especialmente no horario de pico.",
                            "Infraestrutura",
                            "Avenida Ceara, sentido centro-bairro, proximo ao Terminal Urbano, Rio Branco/AC",
                            "Carlos Mendes",
                            LocalDateTime.now().minusDays(1).minusHours(4),
                            "Ativo"
                    ),
                    criarComunicado(
                            "Poste com lampada queimada na Estacao Experimental",
                            "A iluminacao publica esta apagada em um trecho de grande circulacao noturna, reduzindo a visibilidade para pedestres.",
                            "Iluminacao publica",
                            "Rua Isaura Parente, trecho da Estacao Experimental, Rio Branco/AC",
                            "Maria do Socorro Lima",
                            LocalDateTime.now().minusDays(3),
                            "Ativo"
                    ),
                    criarComunicado(
                            "Alerta de seguranca na Rua Rio de Janeiro",
                            "Moradores pedem atencao redobrada apos relatos de abordagens suspeitas durante a noite nas proximidades de comercios locais.",
                            "Seguranca",
                            "Rua Rio de Janeiro, bairro Preventorio, Rio Branco/AC",
                            "Conselho Comunitario do Preventorio",
                            LocalDateTime.now().minusHours(18),
                            "Ativo"
                    ),
                    criarComunicado(
                            "Coleta de lixo atrasada no Conjunto Tucuma",
                            "A coleta nao passou no dia previsto e alguns pontos ja apresentam acumulacao de residuos nas calcadas.",
                            "Coleta de lixo",
                            "Conjunto Tucuma, quadras proximas a Via Verde, Rio Branco/AC",
                            "Joana Ferreira",
                            LocalDateTime.now().minusDays(1),
                            "Resolvido"
                    )
            ));
        };
    }

    private Comunicado criarComunicado(
            String titulo,
            String descricao,
            String categoria,
            String localizacao,
            String nomeResponsavel,
            LocalDateTime dataCriacao,
            String status
    ) {
        Comunicado comunicado = new Comunicado();
        comunicado.setTitulo(titulo);
        comunicado.setDescricao(descricao);
        comunicado.setCategoria(categoria);
        comunicado.setLocalizacao(localizacao);
        comunicado.setNomeResponsavel(nomeResponsavel);
        comunicado.setDataCriacao(dataCriacao);
        comunicado.setStatus(status);
        return comunicado;
    }
}
