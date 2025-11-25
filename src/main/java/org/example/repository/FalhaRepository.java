package org.example.repository;

import org.example.database.Conexao;
import org.example.model.Falha;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FalhaRepository {

    /// id BIGINT AUTO_INCREMENT PRIMARY KEY,
    /// equipamentoId BIGINT NOT NULL,
    /// dataHoraOcorrencia DATETIME NOT NULL,
    /// descricao TEXT NOT NULL,
    /// criticidade VARCHAR(50) NOT NULL,
    /// status VARCHAR(50) NOT NULL,
    /// tempoParadaHoras DECIMAL(10,2) DEFAULT 0.00,


    public Falha registrarNovaFalha(Falha falha) throws SQLException {
        String insertQuery = """
                INSERT INTO Falha (equipamentoId,
                 dataHoraOcorrencia,
                 descricao,
                 criticidade,
                 status,
                 tempoParadaHoras)
                VALUES(?,?,?,?,?,?)
                """;

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {


                stmt.setLong(1,falha.getEquipamentoId());
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(falha.getDataHoraOcorrencia()));
            stmt.setString(3,falha.getDescricao());
                stmt.setString(4,falha.getCriticidade());
                stmt.setString(5, "ABERTA");
                stmt.setBigDecimal(6,falha.getTempoParadaHoras());

                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        long idGerado = rs.getLong(1);
                        falha.setId(idGerado);
                    }
                }

                return falha;

        }
    }




    public List<Falha> buscarFalhasCriticasAbertas() throws SQLException {
        String selectQuery = """
                SELECT * FROM falha
                WHERE status = 'ABERTA' AND criticidade = 'CRITICA'
                """;

        List<Falha> falhaList = new ArrayList<>();

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(selectQuery, Statement.RETURN_GENERATED_KEYS)) {

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                falhaList.add(new Falha(
                        rs.getLong("id"),
                        rs.getLong("equipamentoId"),
                        rs.getTimestamp("dataHoraOcorrencia").toLocalDateTime(),
                        rs.getString("descricao"),
                        rs.getString("criticidade"),
                        rs.getString("status"),
                        rs.getBigDecimal("tempoParadaHoras")
                ));
            }
        }
        return falhaList;
    }
}
