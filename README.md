# ServiceDesk Pro - Java Swing

Sistema profissional de gestao de chamados com interface grafica em Java Swing, persistencia em SQLite, arquitetura em camadas, validacoes de negocio e testes automatizados.

O projeto simula uma ferramenta usada por empresas para registrar, acompanhar e resolver demandas internas de suporte, atendimento, financeiro ou administracao.

## Status do projeto

Projeto criado e compilado com sucesso em Windows usando:

- JDK 21 Eclipse Adoptium
- Maven 3.9.9 instalado manualmente em `Documents\Code\Tools`
- PowerShell

Resultado validado:

```text
BUILD SUCCESS
```

## Objetivo do sistema

Criar uma aplicacao desktop em Java para abertura e acompanhamento de chamados internos, com dados persistidos em banco SQLite e interface simples, limpa e utilizavel.

Este projeto foi pensado para portfolio de quem busca estagio ou primeira vaga em TI, mostrando Java aplicado em um problema real de empresa.

## Tecnologias utilizadas

- Java 21
- Java Swing
- Maven
- SQLite
- JDBC
- JUnit 5
- PowerShell
- Git e GitHub

## Funcionalidades

- Interface grafica desktop
- Cadastro automatico de solicitantes
- Abertura de chamados
- Listagem em tabela
- Detalhamento do chamado selecionado
- Atualizacao de status
- Dashboard com indicadores por status
- Validacao de campos obrigatorios
- Banco SQLite criado automaticamente
- Testes automatizados para validacoes

## Estrutura de pastas

```text
servicedesk-swing-java/
|-- src/
|   |-- main/
|   |   |-- java/br/com/nogueira/servicedesk/
|   |   |   |-- config/
|   |   |   |-- database/
|   |   |   |-- exception/
|   |   |   |-- model/
|   |   |   |-- repository/
|   |   |   |-- service/
|   |   |   |-- ui/
|   |   |   `-- Main.java
|   |   `-- resources/schema.sql
|   `-- test/java/br/com/nogueira/servicedesk/service/
|-- data/
|-- docs/
|-- scripts/
|-- pom.xml
|-- .gitignore
`-- README.md
```

## Arquitetura

O projeto usa uma arquitetura em camadas:

- `model`: classes de dominio, como `Ticket`, `Customer`, `Priority` e `TicketStatus`.
- `repository`: acesso ao banco de dados com JDBC.
- `service`: regras de negocio, validacoes e fluxo da aplicacao.
- `database`: conexao e inicializacao do SQLite.
- `ui`: telas e componentes Swing.
- `exception`: erros de negocio e banco.
- `resources`: script SQL de criacao das tabelas.
- `tests`: testes automatizados.

Essa organizacao facilita manutencao e evolucao para Spring Boot, PostgreSQL ou uma interface web no futuro.

## Como rodar no Windows

Entre na pasta do projeto:

```powershell
cd "$env:USERPROFILE\Documents\Code\Projetos\servicedesk-swing-java"
```

Se o seu PowerShell ainda nao reconhece `java` e `mvn`, configure o ambiente nesta janela:

```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"
$env:MAVEN_HOME = "$env:USERPROFILE\Documents\Code\Tools\apache-maven-3.9.9"
$env:Path = "$env:JAVA_HOME\bin;$env:MAVEN_HOME\bin;$env:Path"
```

Confira as versoes:

```powershell
java -version
mvn -version
```

Compile o projeto:

```powershell
mvn clean install
```

Execute a aplicacao com interface grafica:

```powershell
mvn exec:java
```

## Rodar com script pronto

O Windows pode bloquear arquivos `.ps1` por politica de seguranca. Para rodar sem alterar a configuracao permanente do sistema, use:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\run.ps1
```

Para rodar apenas os testes:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\test.ps1
```

## Instalacao do Java e Maven

O JDK foi instalado com:

```powershell
winget install EclipseAdoptium.Temurin.21.JDK
```

No seu Windows, o pacote `Apache.Maven` nao foi encontrado pelo `winget`. Por isso, o Maven foi instalado manualmente em:

```text
C:\Users\USERPROFILE\Documents\Code\Tools\apache-maven-3.9.9
```

Comandos usados para instalar o Maven manualmente:

```powershell
$MavenVersion = "3.9.9"
$MavenZip = "$env:TEMP\apache-maven-$MavenVersion-bin.zip"
$MavenDir = "$env:USERPROFILE\Documents\Code\Tools"

New-Item -ItemType Directory -Force -Path $MavenDir

Invoke-WebRequest "https://archive.apache.org/dist/maven/maven-3/$MavenVersion/binaries/apache-maven-$MavenVersion-bin.zip" -OutFile $MavenZip

Expand-Archive $MavenZip -DestinationPath $MavenDir -Force
```

Depois disso, configure o Maven na janela atual:

```powershell
$env:MAVEN_HOME = "$env:USERPROFILE\Documents\Code\Tools\apache-maven-3.9.9"
$env:Path = "$env:MAVEN_HOME\bin;$env:Path"
```

## Problemas resolvidos durante a configuracao

### `mvn` nao reconhecido

Significa que o Maven nao esta no `Path`. Use:

```powershell
$env:MAVEN_HOME = "$env:USERPROFILE\Documents\Code\Tools\apache-maven-3.9.9"
$env:Path = "$env:MAVEN_HOME\bin;$env:Path"
```

### `java` nao reconhecido

Significa que o JDK nao esta no `Path` da janela atual. Use:

```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
```

### Execucao de scripts desabilitada

Use o script com bypass apenas para esta execucao:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\run.ps1
```

### Erro `illegal character: '\ufeff'`

Esse erro foi causado por arquivos Java salvos com BOM, um caractere invisivel no inicio do arquivo. Os arquivos foram corrigidos e regravados em UTF-8 sem BOM.

## Banco de dados

O sistema cria automaticamente o arquivo:

```text
data/servicedesk.db
```

Tabelas:

- `customers`: solicitantes.
- `tickets`: chamados.

## Como usar a aplicacao

1. Abra a aplicacao.
2. Preencha solicitante, e-mail, departamento, titulo, descricao e prioridade.
3. Clique em `Abrir chamado`.
4. Veja o chamado na tabela.
5. Selecione um chamado e altere o status.
6. Use `Ver detalhes` para consultar todas as informacoes.
7. Acompanhe os indicadores no rodape.

## Arquivos principais

- `Main.java`: inicializa banco, servicos e interface.
- `MainFrame.java`: janela principal Swing.
- `TicketTableModel.java`: modelo da tabela de chamados.
- `TicketService.java`: regras dos chamados.
- `CustomerService.java`: criacao/reuso de solicitantes.
- `TicketRepository.java`: persistencia dos chamados.
- `CustomerRepository.java`: persistencia dos solicitantes.
- `ValidationService.java`: validacoes reutilizaveis.
- `schema.sql`: estrutura do banco.


## Organizacao de branches

```text
main
feature/domain-models
feature/sqlite-persistence
feature/business-services
feature/swing-ui
fix/windows-setup
feature/tests
```

Fluxo recomendado:

```powershell
git checkout -b fix/windows-setup
git add .
git commit -m "fix: document windows java and maven setup"
git checkout main
git merge fix/windows-setup
```


## Habilidades tecnicas comprovadas

- Java com POO
- Interface grafica Swing
- Maven
- JDBC
- SQLite
- SQL
- Arquitetura em camadas
- Validacao de dados
- Tratamento de excecoes
- Testes com JUnit
- Git e GitHub
- Documentacao profissional
