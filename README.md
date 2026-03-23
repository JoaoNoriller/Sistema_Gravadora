<h1>Sistema de Gerenciamento de Gravadora</h1>

API foi desenvolvida para automatizar e centralizar o controle das operações de uma gravadora musical.
O sistema permite o cadastro, consulta, atualização e remoção de artistas, álbuns e gravadoras,
aplicando regras de negócio que garantem a integridade e consistência dos dados.

<h1>📁 Estrutura do Projeto</h1>
<h3>O repositório do projeto está organizado da seguinte forma:</h3>

- `src/ :` Contém o código-fonte do projeto.
- `controller/ :` Implementação dos controllers que recebem as requisições HTTP e se comunicam com os services.
- `service/ :` Implementação das regras de negócio da aplicação.
- `repository/ :` Interfaces de acesso ao banco de dados, utilizadas pelos services.
- `model/ :` Classes que representam as entidades da aplicação (Album, Artista, Gravadora).
- `dto/ :` Objetos de transferência de dados utilizados nas requisições e respostas.

* `docs/ :` Documentação do projeto, como diagramas e explicações da arquitetura.
* `test/ :` Testes unitários e de integração para garantir a qualidade do código.
* `scripts/ :` Scripts auxiliares para configuração ou execução do projeto.

<h3>README.md : Documento principal com instruções básicas sobre o projeto.</h3>

<h1>Pré-Requisitos</h1>

Veja as seguintes ferramentas instaladas antes da obrigação

- Java 17 ou superior (JDK)
- Maven 3.8+
- MySQL 8.0+
- Git

<h1>Instalação</h1>

- Git Clone: `https://github.com/seu-usuario/Sistema_Gravadora.git`
- cd `Sistema_Gravadora`

<h1>Configurar o banco de dados MySQL</h1>

- mysql -u root -p
- Criar Banco de Dados Sistema_Gravadora;
- Saída;

<h1>Instale as dependências e compile o projeto</h1>

- `mvn clean install`

<h1>Execução</h1>

Para executar o projeto, siga os passos abaixo

- 1- Inicie a aplicação Spring Boot:
- `mvn spring-boot:run`
- 2- Acesse a API no navegador ou via Postman:
- `http://localhost:8080`

<h1>Funcionalidades</h1>

- `Cadastro de Artistas:` Permite registrar novos artistas no sistema, garantindo que não existam dois artistas cadastrados com o mesmo nome.
- `Cadastro de Gravadoras:` Permite ao registrador gravar informações como nome, endereço, telefone, país, CNPJ e dados de fundação. Garante também que não exista duas gravadoras cadastradas com o mesmo nome ou Cnpj.
- `Cadastro de Álbuns:` Permite registrar álbuns vinculados a um artista e uma gravadora existente, com validações de quantidade de músicas, título, duração e limite anual de lançamentos.
- `Listagem e Consulta:` Possibilidade de listar todos os registros e buscar artistas, gravadoras e álbuns individualmente pelo seu identificador (ID).
- `Exclusão de Registros:` Permite remover artistas, gravadoras e álbuns por ID, com validação de existência antes da exclusão.
- `Status Automático de Álbum:` O sistema define automaticamente o status do álbum como COMPLETO (10 ou mais músicas) ou INCOMPLETO (menos de 10 músicas), sem intervenção manual.

<h1>Teste</h1>

Para garantir a qualidade do código, execute os testes com o seguinte comando

- `mvn test`

### 🎤 Artista — `/artista`

- **POST** `/artista`
  - **Justificativa:** POST é utilizado para criação de novos recursos. Envia os dados do artista no corpo da requisição.
  - **Body:**
    ```json
    {
      "dcNome": "string",
      "dcEndereco": "string",
      "dtNascimento": "yyyy-MM-dd",
      "dcNacionalidade": "string",
      "dcGeneroMusical": "string"
    }
    ```
  - **Response:**
    ```json
    {
      "idArtista": 1,
      "dcNome": "string",
      "dcEndereco": "string",
      "dtNascimento": "yyyy-MM-dd",
      "dcNacionalidade": "string",
      "dcGeneroMusical": "string"
    }
    ```

- **GET** `/artista`
  - **Justificativa:** GET é utilizado para consulta de dados sem alteração de estado. Retorna todos os artistas cadastrados.
  - **Response:**
    ```json
    [
      {
        "idArtista": 1,
        "dcNome": "string",
        "dcEndereco": "string",
        "dtNascimento": "yyyy-MM-dd",
        "dcNacionalidade": "string",
        "dcGeneroMusical": "string"
      }
    ]
    ```

- **GET** `/artista/{id}`
  - **Justificativa:** GET com parâmetro de rota para buscar um recurso específico pelo seu identificador.
  - **Response:**
    ```json
    {
      "idArtista": 1,
      "dcNome": "string",
      "dcEndereco": "string",
      "dtNascimento": "yyyy-MM-dd",
      "dcNacionalidade": "string",
      "dcGeneroMusical": "string"
    }
    ```

- **PUT** `/artista/{id}`
  - **Justificativa:** PUT é utilizado para atualização completa de um recurso existente, identificado pelo ID na rota.
      O ID do artista é informado na URL, não precisa ser enviado no body.
  - **Body:**
    ```json
    {
      "dcNome": "string",
      "dcEndereco": "string",
      "dtNascimento": "yyyy-MM-dd",
      "dcNacionalidade": "string",
      "dcGeneroMusical": "string"
    }
    ```
  - **Response:**
    ```json
    {
      "idArtista": 1,
      "dcNome": "string",
      "dcEndereco": "string",
      "dtNascimento": "yyyy-MM-dd",
      "dcNacionalidade": "string",
      "dcGeneroMusical": "string"
    }
    ```

- **DELETE** `/artista/{id}`
  - **Justificativa:** DELETE é utilizado para remoção de um recurso existente identificado pelo ID.
  - **Response:**
    ```
    Artista removido com sucesso!
    ```

---

### 🏢 Gravadora — `/gravadora`

- **POST** `/gravadora`
  - **Justificativa:** POST é utilizado para criação de novos recursos. Envia os dados da gravadora no corpo da requisição.
  - **Body:**
    ```json
    {
      "dcNome": "string",
      "dcEndereco": "string",
      "dcTelefone": "string",
      "dcPais": "string",
      "dtDataFundacao": "yyyy-MM-dd",
      "dcCnpj": "string"
    }
    ```
  - **Response:**
    ```json
    {
      "idGravadora": 1,
      "dcNome": "string",
      "dcEndereco": "string",
      "dcTelefone": "string",
      "dcPais": "string",
      "dtDataFundacao": "yyyy-MM-dd",
      "dcCnpj": "string"
    }
    ```

- **GET** `/gravadora`
  - **Justificativa:** GET é utilizado para consulta de dados sem alteração de estado. Retorna todas as gravadoras cadastradas.
  - **Response:**
    ```json
    [
      {
        "idGravadora": 1,
        "dcNome": "string",
        "dcEndereco": "string",
        "dcTelefone": "string",
        "dcPais": "string",
        "dtDataFundacao": "yyyy-MM-dd",
        "dcCnpj": "string"
      }
    ]
    ```

- **GET** `/gravadora/{id}`
  - **Justificativa:** GET com parâmetro de rota para buscar uma gravadora específica pelo seu identificador.
  - **Response:**
    ```json
    {
      "idGravadora": 1,
      "dcNome": "string",
      "dcEndereco": "string",
      "dcTelefone": "string",
      "dcPais": "string",
      "dtDataFundacao": "yyyy-MM-dd",
      "dcCnpj": "string"
    }
    ```

- **PUT** `/gravadora/{id}`
  - **Justificativa:** PUT é utilizado para atualização completa de um recurso existente, identificado pelo ID na rota.
      O ID da gravadora é informado na URL, não precisa ser enviado no body.
  - **Body:**
    ```json
    {
      "dcNome": "string",
      "dcEndereco": "string",
      "dcTelefone": "string",
      "dcPais": "string",
      "dtDataFundacao": "yyyy-MM-dd",
      "dcCnpj": "string"
    }
    ```
  - **Response:**
    ```json
    {
      "idGravadora": 1,
      "dcNome": "string",
      "dcEndereco": "string",
      "dcTelefone": "string",
      "dcPais": "string",
      "dtDataFundacao": "yyyy-MM-dd",
      "dcCnpj": "string"
    }
    ```

- **DELETE** `/gravadora/{id}`
  - **Justificativa:** DELETE é utilizado para remoção de um recurso existente identificado pelo ID.
  - **Response:**
    ```
    Gravadora removida com sucesso!
    ```

---

### 💿 Album — `/album`

- **POST** `/album`
  - **Justificativa:** POST é utilizado para criação de novos recursos. Envia os dados do álbum no corpo da requisição, com vínculo obrigatório a um artista e uma gravadora.
    `dcStatus não é enviado, pois é gerado automaticamente pelo service.`
  - **Body:**
    ```json
    {
      "idAlbum": null,
      "dcTitulo": "string",
      "dtAnoLancamento": "yyyy-MM-dd",
      "dcStatus": null,
      "qtdMusica": 10,
      "tmDuracao": "HH:mm:ss",
      "idArtista": 1,
      "idGravadora": 1
    }
    ```
  - **Response:**
    ```json
    {
      "idAlbum": 1,
      "dcTitulo": "string",
      "dtAnoLancamento": "yyyy-MM-dd",
      "dcStatus": "COMPLETO",
      "qtdMusica": 10,
      "tmDuracao": "HH:mm:ss",
      "artista": { "idArtista": 1, "dcNome": "string" },
      "gravadora": { "idGravadora": 1, "dcNome": "string" }
    }
    ```

- **GET** `/album`
  - **Justificativa:** GET é utilizado para consulta de dados sem alteração de estado. Retorna todos os álbuns cadastrados.
  - **Response:**
    ```json
    [
      {
        "idAlbum": 1,
        "dcTitulo": "string",
        "dtAnoLancamento": "yyyy-MM-dd",
        "dcStatus": "COMPLETO",
        "qtdMusica": 10,
        "tmDuracao": "HH:mm:ss",
        "artista": { "idArtista": 1, "dcNome": "string" },
        "gravadora": { "idGravadora": 1, "dcNome": "string" }
      }
    ]
    ```

- **GET** `/album/{id}`
  - **Justificativa:** GET com parâmetro de rota para buscar um álbum específico pelo seu identificador.
  - **Response:**
    ```json
    {
      "idAlbum": 1,
      "dcTitulo": "string",
      "dtAnoLancamento": "yyyy-MM-dd",
      "dcStatus": "COMPLETO",
      "qtdMusica": 10,
      "tmDuracao": "HH:mm:ss",
      "artista": { "idArtista": 1, "dcNome": "string" },
      "gravadora": { "idGravadora": 1, "dcNome": "string" }
    }
    ```

- **GET** `/album/artista/{idArtista}`
  - **Justificativa:** GET com parâmetro de rota para listar todos os álbuns vinculados a um artista específico.
      Útil para verificar quantos álbuns um artista possui e quais são eles.
  - **Response:**
    ```json
    [
      {
        "idAlbum": 18,
        "dcTitulo": "Rock",
        "dtAnoLancamento": "2026-03-20",
        "dcStatus": "COMPLETO",
        "qtdMusica": 30,
        "tmDuracao": "01:02:02",
        "artista": {
          "idArtista": 11,
          "dcNome": "Artista teste",
          "dcEndereco": "teste",
          "dtNascimento": "2026-03-20",
          "dcNacionalidade": "string",
          "dcGeneroMusical": "pop"
        },
        "gravadora": {
          "idGravadora": 3,
          "dcNome": "calypso",
          "dcEndereco": "string",
          "dcTelefone": "string",
          "dcPais": "string",
          "dtDataFundacao": "2026-03-18",
          "dcCnpj": "string44"
        }
      }
    ]
    ```

- **PUT** `/album/{id}`
  - **Justificativa:** PUT é utilizado para atualização completa de um recurso existente, identificado pelo ID na rota. O ID do álbum é informado na URL, o campo `idAlbum`
      no body deve ser enviado como `null`. `dcStatus não é enviado, pois é gerado automaticamente pelo service.`
  - **Body:**
    ```json
    {
      "idAlbum": null,
      "dcTitulo": "string",
      "dtAnoLancamento": "yyyy-MM-dd",
      "dcStatus": null,
      "qtdMusica": 10,
      "tmDuracao": "HH:mm:ss",
      "idArtista": 1,
      "idGravadora": 1
    }
    ```
  - **Response:**
    ```json
    {
      "idAlbum": 1,
      "dcTitulo": "string",
      "dtAnoLancamento": "yyyy-MM-dd",
      "dcStatus": "COMPLETO",
      "qtdMusica": 10,
      "tmDuracao": "HH:mm:ss",
      "artista": { "idArtista": 1, "dcNome": "string" },
      "gravadora": { "idGravadora": 1, "dcNome": "string" }
    }
    ```

- **DELETE** `/album/{id}`
  - **Justificativa:** DELETE é utilizado para remoção de um recurso existente identificado pelo ID.
  - **Response:**
    ```
    Álbum removido com sucesso!
    ```

