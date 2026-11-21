package br.exemplo;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImpressoraTest {
    private final Impressora impressora = new Impressora();

    @Test
    void desempenhoComCargaNormal() throws InterruptedException {
        var documentos = List.of("Ata", "Contrato", "Relatório");
        double metaMs = 500;
        long inicio = System.nanoTime();
        for (String documento : documentos) {
            assertTrue(impressora.enviar(documento));
        }
        for (String documento : documentos) {
            assertEquals(documento, impressora.imprimirProximo());
        }
        double tempoMs = (System.nanoTime() - inicio) / 1_000_000.0;
        System.out.printf("Desempenho: 3 documentos em %.1f ms. Meta: até %.0f ms.%n",
                tempoMs, metaMs);
        assertTrue(tempoMs <= metaMs, "O lote excedeu a meta de 500 ms");
    }

    @Test
    void estresseComFilaAcimaDaCapacidade() throws InterruptedException {
        int aceitos = 0;
        int enviados = 12;
        for (int i = 1; i <= enviados; i++) {
            if (impressora.enviar("Documento " + i)) {
                aceitos++;
            }
        }
        System.out.printf("Estresse: %d enviados, %d aceitos, %d recusados.%n",
                enviados, aceitos, enviados - aceitos);
        assertEquals(5, aceitos);
        assertEquals(5, impressora.pendentes());
        for (int i = 1; i <= aceitos; i++) {
            assertEquals("Documento " + i, impressora.imprimirProximo());
        }
        assertTrue(impressora.enviar("Após o pico"));
        assertEquals("Após o pico", impressora.imprimirProximo());
        assertEquals(0, impressora.pendentes());  
        System.out.println("Após o pico, a impressora aceitou e imprimiu um novo documento.");
    }

    @Test
    void recuperacaoAposPerdaDeConexao() throws InterruptedException {
        impressora.enviar("Relatório");
        impressora.enviar("Contrato");
        assertEquals("Relatório", impressora.imprimirProximo());

        impressora.desconectar();
        var falha = assertThrows(IllegalStateException.class, impressora::imprimirProximo);
        assertEquals(1, impressora.pendentes());
        System.out.printf("Falha: %s. Documentos pendentes: %d.%n",
                falha.getMessage(), impressora.pendentes());

        impressora.reconectar();
        String recuperado = impressora.imprimirProximo();
        assertEquals("Contrato", recuperado);
        assertEquals(0, impressora.pendentes());
        System.out.printf("Recuperação: %s impresso sem reenvio. Fila vazia.%n", recuperado);
    }
}
