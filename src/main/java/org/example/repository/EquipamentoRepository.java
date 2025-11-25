package org.example.repository;

import org.example.database.Conexao;
import org.example.model.Equipamento;

import java.sql.*;

public class EquipamentoRepository {

    public Equipamento criarEquipamento(Equipamento equipamento) throws SQLException {

       ///    id BIGINT AUTO_INCREMENT PRIMARY KEY,
       ///                 nome VARCHAR(255) NOT NULL,
       ///                 numeroDeSerie VARCHAR(100) NOT NULL UNIQUE,
       ///                 areaSetor VARCHAR(100) NOT NULL,
       ///                 statusOperacional VARCHAR(50) NOT NULL,

        String insertQuery = """
                INSERT INTO equipamento(nome,
                numeroDeSerie,
                areaSetor,
                StatusOperacional)
                VALUES(?,?,?,?)
                """;

       try(Connection conn = Conexao.conectar();
           PreparedStatement stmt = conn.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS)) {

           stmt.setString(1,equipamento.getNome());
           stmt.setString(2,equipamento.getNumeroDeSerie());
           stmt.setString(3,equipamento.getAreaSetor());
           stmt.setString(4,"OPERACIONAL");

           stmt.executeUpdate();

           ResultSet rs = stmt.getGeneratedKeys();

           if(rs.next()){
               equipamento.setId(Long.valueOf(rs.getString(1)));
               return equipamento;
           } else {
               throw new RuntimeException("Houve um Erro ao Inserir o Equipamento no Banco de Dados");
           }
       }
    }

    public Equipamento buscarEquipamentoPorId(long id) throws SQLException {
        String selectQuery = """
                SELECT id,nome,numeroDeSerie,areaSetor,statusOperacional
                FROM equipamento
                WHERE id = ?
                """;

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(selectQuery)) {

            stmt.setLong(1,id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                return new Equipamento(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("numeroDeSerie"),
                        rs.getString("areaSetor"),
                        rs.getString("statusOperacional")
                );
            }
        }
        return null;
    }

    public void atualizarStatusEquipamento(long idEquipamento, String status) throws SQLException {
        String updateQuery = """
                UPDATE equipamento
                SET statusOperacional = ?
                WHERE id = ?
                """;

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setString(1, status);
            stmt.setLong(2,idEquipamento);

            stmt.executeUpdate();
        }
    }
}
