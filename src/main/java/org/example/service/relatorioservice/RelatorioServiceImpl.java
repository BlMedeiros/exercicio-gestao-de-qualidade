package org.example.service.relatorioservice;

import org.example.dto.EquipamentoContagemFalhasDTO;
import org.example.dto.FalhaDetalhadaDTO;
import org.example.dto.RelatorioParadaDTO;
import org.example.model.Equipamento;
import org.example.repository.RelatorioRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RelatorioServiceImpl implements RelatorioService{
    @Override
    public List<RelatorioParadaDTO> gerarRelatorioTempoParada() throws SQLException {

        RelatorioRepository relatorioRepository = new RelatorioRepository();

        return relatorioRepository.gerarRelatorioTempoParada();
    }

    @Override
    public List<Equipamento> buscarEquipamentosSemFalhasPorPeriodo(LocalDate dataInicio, LocalDate datafim) throws SQLException {

        RelatorioRepository relatorioRepository = new RelatorioRepository();

        return relatorioRepository.buscarEquipamentosSemFalhasPorPeriodo(dataInicio,datafim);
    }

    @Override
    public Optional<FalhaDetalhadaDTO> buscarDetalhesCompletosFalha(long falhaId) throws SQLException {

        RelatorioRepository relatorioRepository = new RelatorioRepository();

        if (falhaId <= 0) {
            throw new RuntimeException("ID da falha inválido: " + falhaId);
        }

        return relatorioRepository.buscarDetalhesCompletosFalha(falhaId);
    }

    @Override
    public List<EquipamentoContagemFalhasDTO> gerarRelatorioManutencaoPreventiva(int contagemMinimaFalhas) throws SQLException {
        RelatorioRepository relatorioRepository = new RelatorioRepository();

        return relatorioRepository.gerarRelatorioManutencaoPreventiva(contagemMinimaFalhas);
    }
}
