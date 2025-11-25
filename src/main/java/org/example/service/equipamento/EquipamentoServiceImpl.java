package org.example.service.equipamento;

import org.example.model.Equipamento;
import org.example.repository.EquipamentoRepository;

import java.sql.SQLException;

public class EquipamentoServiceImpl implements EquipamentoService{
    @Override
    public Equipamento criarEquipamento(Equipamento equipamento) throws SQLException {
        EquipamentoRepository equipamentoRepository = new EquipamentoRepository();

        return equipamentoRepository.criarEquipamento(equipamento);
    }

    @Override
    public Equipamento buscarEquipamentoPorId(Long id) throws SQLException {
        EquipamentoRepository equipamentoRepository = new EquipamentoRepository();

        Equipamento equipamentoRetornado = equipamentoRepository.buscarEquipamentoPorId(id);

        if(equipamentoRetornado == null) {
            throw new RuntimeException("Equipamento não encontrado!");
        }
        return equipamentoRetornado;
    }
}
