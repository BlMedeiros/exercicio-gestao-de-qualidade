package org.example.service.falha;

import org.example.model.Falha;

import java.sql.SQLException;
import java.util.List;

public class FalhaServiceImpl implements FalhaService{
    @Override
    public Falha registrarNovaFalha(Falha falha) throws SQLException {
        return null;
    }

    @Override
    public List<Falha> buscarFalhasCriticasAbertas() throws SQLException {
        return List.of();
    }
}
