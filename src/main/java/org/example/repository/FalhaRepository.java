package org.example.repository;

import org.example.database.Conexao;
import org.example.model.AcaoCorretiva;
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

                falha.setStatus("ABERTA");

                try (ResultSet rsKey = stmt.getGeneratedKeys()) {
                    if (rsKey.next()) {
                        long idGerado = rsKey.getLong(1);
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

    public Falha buscarFalhaPorId(long id) throws SQLException {
        String selectQuery = """
                SELECT * FROM Falha
                WHERE id = ?
                """;

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
            stmt.setLong(1,id);

            ResultSet rs = stmt.executeQuery();

           ///  CREATE TABLE IF NOT EXISTS Falha (
           ///                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
           ///                 equipamentoId BIGINT NOT NULL,
           ///                 dataHoraOcorrencia DATETIME NOT NULL,
           ///                 descricao TEXT NOT NULL,
           ///                 criticidade VARCHAR(50) NOT NULL,
           ///                 status VARCHAR(50) NOT NULL,
           ///                 tempoParadaHoras DECIMAL(10,2) DEFAULT 0.00,
           ///



            while (rs.next()) {
                return new Falha(
                        rs.getLong("id"),
                        rs.getLong("equipamentoId"),
                        rs.getTimestamp("dataHoraOcorrencia").toLocalDateTime(),
                        rs.getString("descricao"),
                        rs.getString("criticidade"),
                        rs.getString("status"),
                        rs.getBigDecimal("tempoParadaHoras")
                );
            }
        }
        return null;
    }

    public void atualizarFalha(long id, String status) throws SQLException {
        String updateQuery = """
                UPDATE Falha
                SET status = ?
                WHERE id = ?
                """;

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setString(1,status);
            stmt.setLong(2,id);

            stmt.executeUpdate();
        }
    }
}
