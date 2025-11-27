package org.example.repository;

import org.example.database.Conexao;
import org.example.model.AcaoCorretiva;
import org.example.model.Equipamento;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AcaoCorretivaRepository {

    /// CREATE TABLE IF NOT EXISTS AcaoCorretiva (
    ///                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ///                 falhaId BIGINT NOT NULL,
    ///                 dataHoraInicio DATETIME NOT NULL,
    ///                 dataHoraFim DATETIME NOT NULL,
    ///                 responsavel VARCHAR(255) NOT NULL,
    ///                 descricaoAcao TEXT NOT NULL,
    ///
    ///                 CONSTRAINT fk_acao_falha FOREIGN KEY (falhaId)
    ///                 REFERENCES Falha(id)
    ///                 ON DELETE RESTRICT
    ///             );
    ///             """;

    public AcaoCorretiva registrarConclusaoDeAcao(AcaoCorretiva acao) throws SQLException {
        String insertQuery = """
                INSERT INTO AcaoCorretiva(
                falhaId,
                dataHoraInicio,
                dataHoraFim,
                responsavel,
                descricaoAcao)
                VALUES(?,?,?,?,?)
                """;

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1,acao.getFalhaId());
            stmt.setTimestamp(2, Timestamp.valueOf(acao.getDataHoraInicio()));
            stmt.setTimestamp(3, Timestamp.valueOf(acao.getDataHoraFim()));
            stmt.setString(4, acao.getResponsavel());
            stmt.setString(5,acao.getDescricaoArea());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()) {
                String id = rs.getString(1);

                acao.setId(Long.valueOf(id));

                return acao;
            }
        }
        return null;
    }

    public AcaoCorretiva buscarAcaoCorretiva(long id) throws SQLException {
        String selectQuery = """
                SELECT * FROM AcaoCorretiva
                WHERE id = ?
                """;

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
            stmt.setLong(1,id);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                return new AcaoCorretiva(
                        rs.getLong("id"),
                        rs.getLong("falhaId"),
                        rs.getTimestamp("dataHoraInicio").toLocalDateTime(),
                        rs.getTimestamp("dataHoraFim").toLocalDateTime(),
                        rs.getString("responsavel"),
                        rs.getString("descricaoAcao")
                );
            }
        }
        return null;
    }

    public void atualizarFalhaCritica(long id) throws SQLException {
        String query = """
                UPDATE Equipamento e
                JOIN Falha f on f.equipamentoId = e.id
                JOIN AcaoCorretiva c ON f.id = c.falhaId
                SET e.statusOperacional = 'OPERACIONAL'
                WHERE f.status = 'RESOLVIDA' AND c.falhaId = ?
                """;

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1,id);

            stmt.executeUpdate();
        }

    }

}
