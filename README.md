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
