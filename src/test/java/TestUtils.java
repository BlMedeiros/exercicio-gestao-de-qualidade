import org.example.database.Conexao;

import java.sql.Connection;
import java.sql.Statement;

public class TestUtils {
    public static void inserirEquipamentosFalhasEAcoes() {
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                INSERT INTO Equipamento (nome, status)
                VALUES ('Motor Principal', 'OPERACIONAL'),
                       ('Esteira 01', 'OPERACIONAL'),
                       ('Caldeira', 'OPERACIONAL');
            """);

            stmt.execute("""
                INSERT INTO Falha (equipamentoId, dataHoraOcorrencia, descricao, criticidade, status, tempoParadaHoras)
                VALUES
                (1, NOW(), 'Falha leve', 'BAIXA', 'ABERTA', 1.5),
                (1, NOW(), 'Falha crítica', 'CRITICA', 'RESOLVIDA', 5.0),
                (2, NOW(), 'Falha média', 'MEDIA', 'ABERTA', 2.0);
            """);

            stmt.execute("""
                INSERT INTO AcaoCorretiva (falhaId, dataHoraInicio, dataHoraFim, responsavel, descricaoAcao)
                VALUES (2, NOW(), NOW(), 'Carlos', 'Reparo completo');
            """);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
