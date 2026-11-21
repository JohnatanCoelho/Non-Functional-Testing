# Testes não funcionais de uma impressora

A impressora simulada guarda até cinco documentos em uma fila e leva cerca de 40 ms para imprimir cada um. Os três testes JUnit usam esse mesmo modelo para responder a perguntas diferentes.

## Como executar

Com JDK 17 ou superior e Maven 3.9 instalados, abra o terminal na pasta que contém o arquivo `pom.xml` e execute:

```sh
mvn test
```

O terminal mostra as medições e o total de testes aprovados. Para apresentar um cenário por vez, use o nome do método, por exemplo:

```sh
mvn "-Dtest=ImpressoraTest#desempenhoComCargaNormal" test
```

## Roteiro para a demonstração

| Técnica | O que acontece | O que observar |
| --- | --- | --- |
| Performance Testing (desempenho) | Enviamos três documentos e medimos o tempo até o último sair. | A meta escolhida para a aula é concluir o lote em até 500 ms. Compare o tempo exibido com essa meta. A medição varia conforme a máquina. |
| Stress Testing (estresse) | Enviamos 12 documentos antes de começar a imprimir. A fila só tem cinco vagas. | Cinco entram e sete são recusados. O teste verifica se os cinco saem na ordem e se, após o pico, a impressora volta a aceitar trabalho. |
| Fail & Recovery Testing (falha e recuperação) | Imprimimos o relatório e desconectamos a impressora antes de imprimir o contrato. A tentativa falha. Depois reconectamos. | O contrato permanece na fila durante a falha e sai após a reconexão, sem reenvio. A fila fica vazia. |

Para apresentar, abra `src/test/java/br/exemplo/ImpressoraTest.java`. Antes de executar cada método, peça à turma para prever o resultado. No caso do estresse, destaque que recusar os sete documentos excedentes é o comportamento esperado da fila cheia.
