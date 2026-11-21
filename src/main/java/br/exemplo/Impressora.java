package br.exemplo;

import java.util.ArrayDeque;
import java.util.Queue;

public class Impressora {
    private static final int CAPACIDADE = 5;
    private final Queue<String> fila = new ArrayDeque<>();
    private boolean conectada = true;

    public boolean enviar(String documento) {
        if (fila.size() >= CAPACIDADE) {
            return false;
        }
        return fila.offer(documento);
    }

    public String imprimirProximo() throws InterruptedException {
        if (!conectada) {
            throw new IllegalStateException("Impressora desconectada");
        }
        Thread.sleep(40); 
        return fila.remove();
    }

    public int pendentes() {
        return fila.size();
    }

    public void desconectar() {
        conectada = false;
    }

    public void reconectar() {
        conectada = true;
    }
}
