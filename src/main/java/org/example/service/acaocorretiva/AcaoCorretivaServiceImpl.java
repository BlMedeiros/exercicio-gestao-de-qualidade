package org.example.service.acaocorretiva;

import org.example.model.AcaoCorretiva;
import org.example.model.Falha;
import org.example.repository.AcaoCorretivaRepository;
import org.example.repository.EquipamentoRepository;
import org.example.repository.FalhaRepository;

import java.sql.SQLException;

public class AcaoCorretivaServiceImpl implements AcaoCorretivaService{

    @Override
    public AcaoCorretiva registrarConclusaoDeAcao(AcaoCorretiva acao) throws SQLException {

        AcaoCorretivaRepository acaoCorretivaRepository = new AcaoCorretivaRepository();
        FalhaRepository falhaRepository = new FalhaRepository();
        EquipamentoRepository equipamentoRepository = new EquipamentoRepository();

        Falha falha = falhaRepository.buscarFalhaPorId(acao.getFalhaId());

        if(falha == null) {
            throw new RuntimeException("Falha não encontrada!");
        }

        AcaoCorretiva acaoCorretiva = acaoCorretivaRepository.registrarConclusaoDeAcao(acao);
        falhaRepository.atualizarFalha(acao.getFalhaId(),"RESOLVIDA");
        acaoCorretivaRepository.atualizarFalhaCritica(acao.getId());

        return acaoCorretiva;

    }
}
