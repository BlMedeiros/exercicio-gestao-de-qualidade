package org.example.service.falha;

import org.example.model.Equipamento;
import org.example.model.Falha;
import org.example.repository.EquipamentoRepository;
import org.example.repository.FalhaRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class FalhaServiceImpl implements FalhaService{

    @Override
    public Falha registrarNovaFalha(Falha falha) throws SQLException {
        EquipamentoRepository equipamentoRepository = new EquipamentoRepository();
        FalhaRepository falhaRepository = new FalhaRepository();

        Equipamento equipamento = equipamentoRepository.buscarEquipamentoPorId(falha.getEquipamentoId());

        if (equipamento == null) {
            throw new IllegalArgumentException("Equipamento não encontrado!");
        }

        Falha falhaSalva = falhaRepository.registrarNovaFalha(falha);

        if (Objects.equals(falha.getCriticidade(), "CRITICA")) {
            equipamentoRepository.atualizarStatusEquipamento(falha.getEquipamentoId(), "EM_MANUTENCAO");
        }

        return falhaSalva;
    }

    @Override
    public List<Falha> buscarFalhasCriticasAbertas() throws SQLException {
        FalhaRepository falhaRepository = new FalhaRepository();

        return falhaRepository.buscarFalhasCriticasAbertas();
    }
}
