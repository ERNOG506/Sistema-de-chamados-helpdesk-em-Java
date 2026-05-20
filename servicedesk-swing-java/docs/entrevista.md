# Guia de entrevista - ServiceDesk Pro

## Pitch de 30 segundos

Desenvolvi um sistema desktop de chamados em Java Swing para simular uma ferramenta real de suporte interno. A aplicacao permite abrir chamados, listar em tabela, alterar status, ver detalhes e acompanhar indicadores. Usei Maven, SQLite, JDBC, arquitetura em camadas e testes com JUnit.

## Como explicar tecnicamente

O projeto separa interface, regra de negocio e banco de dados. A tela Swing chama os services, os services validam regras e coordenam o fluxo, e os repositories executam SQL no SQLite. Essa divisao deixa o sistema mais facil de manter e expandir.

## Decisoes importantes

- Swing foi escolhido para criar uma interface grafica sem depender de JavaFX.
- SQLite foi escolhido por ser simples de rodar localmente e mostrar persistencia real.
- JDBC foi usado para demonstrar SQL e conexao direta com banco.
- Enums foram usados para status e prioridade, evitando textos soltos.
- JUnit foi usado para mostrar preocupacao com qualidade.

## Evolucao futura

Eu evoluiria para Spring Boot com API REST, PostgreSQL, autenticacao, filtros avancados, relatorios em Excel e uma interface web.
