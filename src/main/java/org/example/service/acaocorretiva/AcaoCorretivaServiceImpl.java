package org.example.service.acaocorretiva;

import org.example.model.AcaoCorretiva;

import java.sql.SQLException;

public class AcaoCorretivaServiceImpl implements AcaoCorretivaService{

    @Override
    public AcaoCorretiva registrarConclusaoDeAcao(AcaoCorretiva acao) throws SQLException {
        if(acao == null) {
            throw new RuntimeException("Falha não encontrada!");
        }
        return acao;
    }
}
