import org.example.dto.EquipamentoContagemFalhasDTO;
import org.example.dto.FalhaDetalhadaDTO;
import org.example.dto.RelatorioParadaDTO;
import org.example.model.Equipamento;
import org.example.service.relatorioservice.RelatorioService;
import org.example.service.relatorioservice.RelatorioServiceImpl;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RelatorioServiceTest {

    private static RelatorioService service;

    @BeforeAll
    static void setup() {
        service = new RelatorioServiceImpl();

        // INSERIR dados no banco para os testes
        TestUtils.inserirEquipamentosFalhasEAcoes();
    }

    // ------------------------------ TESTE 1 ------------------------------
    @Test
    @Order(1)
    void deveGerarRelatorioTempoParada() throws SQLException {
        List<RelatorioParadaDTO> lista = service.gerarRelatorioTempoParada();

        assertNotNull(lista);
        assertTrue(lista.size() > 0);

        RelatorioParadaDTO dto = lista.get(0);
        assertNotNull(dto.getNomeEquipamento());
        assertTrue(dto.getTotalHorasParadas() > 0);
    }

    // ------------------------------ TESTE 2 ------------------------------
    @Test
    @Order(2)
    void deveBuscarEquipamentosSemFalhas() throws SQLException{
        LocalDate inicio = LocalDate.now().minusDays(10);
        LocalDate fim = LocalDate.now().plusDays(10);

        List<Equipamento> lista = service.buscarEquipamentosSemFalhasPorPeriodo(inicio, fim);

        assertNotNull(lista);
        assertTrue(lista.size() >= 0);
    }

    // ------------------------------ TESTE 3 ------------------------------
    @Test
    @Order(3)
    void deveBuscarDetalhesCompletosDaFalha() throws SQLException{
        long falhaExistente = 1;

        Optional<FalhaDetalhadaDTO> detalhes = service.buscarDetalhesCompletosFalha(falhaExistente);

        assertTrue(detalhes.isPresent());
        assertNotNull(detalhes.get().getFalha());
        assertNotNull(detalhes.get().getEquipamento());
    }

    // ------------------------------ TESTE 4 ------------------------------
    @Test
    @Order(4)
    void deveLancarErroSeIdFalhaForInvalido() {
        assertThrows(RuntimeException.class, () -> service.buscarDetalhesCompletosFalha(0));
        assertThrows(RuntimeException.class, () -> service.buscarDetalhesCompletosFalha(-10));
    }

    // ------------------------------ TESTE 5 ------------------------------
    @Test
    @Order(5)
    void deveGerarRelatorioManutencaoPreventiva() throws SQLException{
        List<EquipamentoContagemFalhasDTO> lista =
                service.gerarRelatorioManutencaoPreventiva(1);

        assertNotNull(lista);
        assertTrue(lista.size() > 0);
        assertTrue(lista.get(0).getTotalFalhas() >= 1);
    }

    // ------------------------------ TESTE 6 ------------------------------
    @Test
    @Order(6)
    void deveLancarErroSeContagemMinimaInvalida() {
        assertThrows(RuntimeException.class, () -> service.gerarRelatorioManutencaoPreventiva(0));
        assertThrows(RuntimeException.class, () -> service.gerarRelatorioManutencaoPreventiva(-5));
    }

}
