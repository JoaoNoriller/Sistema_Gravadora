<h1>Sistema de Gerenciamento de Gravadora</h1>

API foi desenvolvida para automatizar e centralizar o controle das operações de uma gravadora musical. 
O sistema permite o cadastro, consulta, atualização e remoção de artistas, álbuns e gravadoras, 
aplicando regras de negócio que garantem a integridade e consistência dos dados.


<h1>📁 Estrutura do Projeto</h1>
<h3>O repositório do projeto está organizado da seguinte forma:</h3>

+ `src/ :` Contém o código-fonte do projeto.
+ `controller/ :` Implementação dos controllers que recebem as requisições HTTP e se comunicam com os services.
+ `service/ :` Implementação das regras de negócio da aplicação. 
+ `repository/ :` Interfaces de acesso ao banco de dados, utilizadas pelos services. 
+ `model/ :` Classes que representam as entidades da aplicação (Album, Artista, Gravadora). 
+ `dto/ :` Objetos de transferência de dados utilizados nas requisições e respostas. 
- `docs/ :` Documentação do projeto, como diagramas e explicações da arquitetura.
- `test/ :` Testes unitários e de integração para garantir a qualidade do código.
- `scripts/ :` Scripts auxiliares para configuração ou execução do projeto.

<h3>README.md : Documento principal com instruções básicas sobre o projeto.</h3>

<h1>Pré-Requisitos</h1>

Veja as seguintes ferramentas instaladas antes da obrigação

+ Java 17 ou superior (JDK)
+ Maven 3.8+
+ MySQL 8.0+
+ Git

<h1>Instalação</h1>

+ Git Clone: `https://github.com/seu-usuario/Sistema_Gravadora.git`
+ cd `Sistema_Gravadora`

<h1>Configurar o banco de dados MySQL</h1>

+ mysql -u root -p
+ Criar Banco de Dados Sistema_Gravadora;
+ Saída;
 
<h1>Instale as dependências e compile o projeto</h1>

+ `mvn clean install`

<h1>Execução</h1>

Para executar o projeto, siga os passos abaixo

+ 1- Inicie a aplicação Spring Boot:
+ `mvn spring-boot:run`
+ 2- Acesse a API no navegador ou via Postman:
+ `http://localhost:8080`


<h1>Funcionalidades</h1>

+ `Cadastro de Artistas:` Permite registrar novos artistas no sistema, garantindo que não existam dois artistas cadastrados com o mesmo nome.
+ `Cadastro de Gravadoras:` Permite ao registrador gravar informações como nome, endereço, telefone, país, CNPJ e dados de fundação.
+ `Cadastro de Álbuns:` Permite registrar álbuns vinculados a um artista e uma gravadora existente, com validações de quantidade de músicas, título, duração e limite anual de lançamentos.
+ `Listagem e Consulta:` Possibilidade de listar todos os registros e buscar artistas, gravadoras e álbuns individualmente pelo seu identificador (ID).
+ `Exclusão de Registros:` Permite remover artistas, gravadoras e álbuns por ID, com validação de existência antes da exclusão.
+ `Status Automático de Álbum:` O sistema define automaticamente o status do álbum como COMPLETO (10 ou mais músicas) ou INCOMPLETO (menos de 10 músicas), sem intervenção manual.

<h1>Teste</h1>

Para garantir a qualidade do código, execute os testes com o seguinte comando

+ `mvn test`



