package org.example.repository;

import org.example.database.Conexao;
import org.example.dto.EquipamentoContagemFalhasDTO;
import org.example.dto.FalhaDetalhadaDTO;
import org.example.dto.RelatorioParadaDTO;
import org.example.model.Equipamento;
import org.example.model.Falha;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RelatorioRepository {

    public List<RelatorioParadaDTO> gerarRelatorioTempoParada() throws SQLException {

        String selectQuery = """
                SELECT f.equipamentoId, e.nome, f.tempoParadaHoras
                FROM Falha f
                JOIN Equipamento e ON f.equipamentoId = e.id
                """;

        List<RelatorioParadaDTO> listRelatorioParada = new ArrayList<>();

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(selectQuery)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rs.getLong("equipamentoId");
                rs.getString("nome");
                rs.getDouble("tempoParadaHoras");

                listRelatorioParada.add(new RelatorioParadaDTO(
                        rs.getLong("equipamentoId"),
                        rs.getString("nome"),
                        rs.getDouble("tempoParadaHoras")
                ));
            }
            return listRelatorioParada;
        }
    }

    public List<Equipamento> buscarEquipamentosSemFalhasPorPeriodo(LocalDate inicio, LocalDate fim) throws SQLException {



        ///    CREATE TABLE IF NOT EXISTS Equipamento (
        ///                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
        ///                 nome VARCHAR(255) NOT NULL,
        ///                 numeroDeSerie VARCHAR(100) NOT NULL UNIQUE,
        ///                 areaSetor VARCHAR(100) NOT NULL,
        ///                 statusOperacional VARCHAR(50) NOT NULL,
        String selectQuery = """
                SELECT e.id,e.nome,e.numeroDeSerie,e.areaSetor,e.statusOperacional
                FROM Equipamento e
                WHERE e.id NOT IN (
                SELECT f.equipamentoId
                FROM Falha f
                WHERE dataHoraOcorrencia >= ? AND dataHoraOcorrencia <= ?
                )
                """;

        List<Equipamento> listEquipamentosSemFalhas = new ArrayList<>();

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(selectQuery)) {

            stmt.setDate(1,Date.valueOf(inicio));
            stmt.setDate(2,Date.valueOf(fim));

            ResultSet rs = stmt.executeQuery();


            ///  public Equipamento(Long id, String nome, String numeroDeSerie, String areaSetor, String statusOperacional) {
            ///         this.id = id;
            ///         this.nome = nome;
            ///         this.numeroDeSerie = numeroDeSerie;
            ///         this.areaSetor = areaSetor;
            ///         this.statusOperacional = statusOperacional;
            ///     }


            while (rs.next()) {
                listEquipamentosSemFalhas.add(new Equipamento(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("numeroDeSerie"),
                        rs.getString("areaSetor"),
                        rs.getString("statusOperacional")
                ));
            }
            return listEquipamentosSemFalhas;
        }
    }

    public Optional<FalhaDetalhadaDTO> buscarDetalhesCompletosFalha(long falhaId) throws SQLException {

        FalhaDetalhadaDTO dto = null;

        String sql = """
            SELECT 
                f.id AS f_id, f.equipamentoId, f.dataHoraOcorrencia, f.descricao, f.criticidade, f.status AS f_status, f.tempoParadaHoras,
                e.id AS e_id, e.nome, e.numeroDeSerie, e.areaSetor, e.statusOperacional
            FROM Falha f
            JOIN Equipamento e ON f.equipamentoId = e.id
            WHERE f.id = ?
        """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, falhaId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    dto = new FalhaDetalhadaDTO();

                    Falha falha = new Falha();
                    falha.setId(rs.getLong("f_id"));
                    falha.setEquipamentoId(rs.getLong("equipamentoId"));
                    falha.setDataHoraOcorrencia(rs.getTimestamp("dataHoraOcorrencia").toLocalDateTime());
                    falha.setDescricao(rs.getString("descricao"));
                    falha.setCriticidade(rs.getString("criticidade"));
                    falha.setStatus(rs.getString("f_status"));
                    falha.setTempoParadaHoras(rs.getBigDecimal("tempoParadaHoras"));

                    Equipamento equipamento = new Equipamento();
                    equipamento.setId(rs.getLong("e_id"));
                    equipamento.setNome(rs.getString("nome"));
                    equipamento.setNumeroDeSerie(rs.getString("numeroDeSerie"));
                    equipamento.setAreaSetor(rs.getString("areaSetor"));
                    equipamento.setStatusOperacional(rs.getString("statusOperacional"));

                    dto.setFalha(falha);
                    dto.setEquipamento(equipamento);
                }
            }
        }

        return Optional.ofNullable(dto);
    }


    public List<EquipamentoContagemFalhasDTO> gerarRelatorioManutencaoPreventiva(int contagemMinimaFalhas) throws SQLException {
        if (contagemMinimaFalhas <= 0) {
            throw new RuntimeException("Contagem mínima deve ser maior que zero");
        }

        List<EquipamentoContagemFalhasDTO> relatorio = new ArrayList<>();

        String sql = """
            SELECT 
                e.id, 
                e.nome, 
                e.numeroDeSerie, 
                e.statusOperacional,
                COUNT(f.id) AS total_falhas
            FROM Equipamento e
            JOIN Falha f ON e.id = f.equipamentoId
            GROUP BY e.id, e.nome, e.numeroDeSerie, e.statusOperacional
            HAVING COUNT(f.id) >= ?
        """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, contagemMinimaFalhas);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EquipamentoContagemFalhasDTO item = new EquipamentoContagemFalhasDTO();

                    item.setNomeEquipamento(rs.getString("nome"));
                    item.setTotalFalhas((int) rs.getLong("total_falhas"));

                    relatorio.add(item);
                }
            }
        }

        return relatorio;
    }
}
