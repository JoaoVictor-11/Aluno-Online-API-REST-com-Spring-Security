# 🎓 Aluno Online API — Gestão Acadêmica com Spring Security, JWT & Swagger UI

API RESTful para gestão acadêmica desenvolvida em **Java** com **Spring Boot**, integrando persistência relacional com **PostgreSQL**, controle de acesso e autenticação stateless com **Spring Security** e **JSON Web Token (JWT)**, além de documentação interativa completa via **Swagger UI (OpenAPI 3)**.

Projeto acadêmico desenvolvido para a disciplina de **Tecnologias para Back-End** do curso de **Ciência da Computação** no **UNIESP**, sob orientação do **Prof. Me. Junio Figueirêdo**.

---

## 📌 Sumário
- [Visão Geral](#-visão-geral)
- [Principais Funcionalidades](#-principais-funcionalidades)
- [Arquitetura e Camadas](#-arquitetura-e-camadas)
- [Mapeamento de Endpoints](#-mapeamento-de-endpoints)
- [Fluxo de Segurança e Autenticação](#-fluxo-de-segurança-e-autenticação)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Configuração e Execução](#-configuração-e-execução)
- [Roteiro de Validação via Swagger UI](#-roteiro-de-validação-via-swagger-ui)
- [Autor](#-autor)

---

## 📖 Visão Geral

A aplicação **Aluno Online** tem como objetivo centralizar e automatizar os principais processos acadêmicos de uma instituição de ensino, abrangendo:
- Gestão cadastral de alunos, professores e disciplinas curriculares.
- Processo de matrícula e controle de ciclo de vida do aluno na disciplina.
- Lançamento incremental de notas com cálculo automático de média e determinação de situação (`APROVADO` ou `REPROVADO`).
- Trancamento controlado de matrículas ativas.

### 🔐 Camada de Segurança (Spring Security + JWT)
A API opera sob o modelo **Stateless** (sem retenção de sessão em memória no servidor):
1. **Cadastro e Login Públicos:** Qualquer novo usuário pode se registrar no sistema (`/cadastros`) e obter um token de acesso (`/login`).
2. **Hash Criptográfico de Senhas:** As credenciais nunca são persistidas em texto puro; todas as senhas utilizam algoritmo de hash seguro **BCrypt**.
3. **Assinatura e Validação JWT:** Cada requisição protegida deve conter o cabeçalho `Authorization: Bearer <token>`. O token JWT é assinado via algoritmo **HMAC-256** com prazo de expiração de **2 horas**.
4. **Filtro de Requisições Customizado:** Um filtro `OncePerRequestFilter` intercepta as requisições, valida a integridade do token, recupera o usuário e o registra no contexto do Spring Security.

---

## ⚙️ Arquitetura e Camadas

O projeto adota a **Arquitetura em Camadas (Layered Architecture)**, promovendo separação de responsabilidades e alta coesão:

```text
src/main/java/br/com/alunoonline/api/
├── controller/          # Endpoints REST (recebem requisições HTTP e direcionam aos services)
│   ├── AlunoController.java
│   ├── DisciplinaController.java
│   ├── ProfessorController.java
│   ├── MatriculaAlunoController.java
│   ├── AutenticacaoController.java
│   └── UsuarioController.java
├── service/             # Regras de negócio, cálculos acadêmicos e orquestração
│   ├── AlunoService.java
│   ├── DisciplinaService.java
│   ├── ProfessorService.java
│   ├── MatriculaAlunoService.java
│   └── AutenticacaoService.java
├── repository/          # Camada de persistência de dados com Spring Data JPA
│   ├── AlunoRepository.java
│   ├── DisciplinaRepository.java
│   ├── ProfessorRepository.java
│   ├── MatriculaAlunoRepository.java
│   └── UsuarioRepository.java
├── model/               # Entidades JPA mapeadas para o PostgreSQL
│   ├── Aluno.java
│   ├── Disciplina.java
│   ├── Professor.java
│   ├── MatriculaAluno.java
│   └── Usuario.java
├── dtos/                # Records e classes de transferência de dados (Data Transfer Objects)
│   ├── AtualizarNotasRequestDTO.java
│   ├── DadosAutenticacao.java
│   ├── DadosCadastroUsuario.java
│   └── DadosDetalhamentoUsuario.java
└── infra/               # Configurações transversais de infraestrutura
    ├── exception/       # Interceptação global de erros (@RestControllerAdvice)
    │   └── TratadorDeErros.java
    ├── openapi/         # Configuração do Swagger UI / OpenAPI 3 com Bearer JWT
    │   └── OpenAPIConfig.java
    └── security/        # Configurações de segurança, filtros e geração de tokens
        ├── SecurityConfig.java
        ├── SecurityFilter.java
        ├── TokenService.java
        └── DadosTokenJWT.java
```

---

## 🛣️ Mapeamento de Endpoints

### 1. Autenticação e Documentação (Públicos)
| Método | Endpoint | Descrição | Acesso |
| :--- | :--- | :--- | :--- |
| `POST` | `/cadastros` | Cadastra um novo usuário no sistema (senha em BCrypt) | Aberto |
| `POST` | `/login` | Autentica usuário e retorna o Token JWT | Aberto |
| `GET` | `/swagger-ui.html` | Interface gráfica interativa da documentação da API | Aberto |
| `GET` | `/v3/api-docs` | Especificação OpenAPI 3 em formato JSON | Aberto |

### 2. Gestão Cadastral (Protegidos por Token JWT)
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/alunos` | Cadastra um novo aluno |
| `GET` | `/alunos` | Lista todos os alunos cadastrados |
| `GET` | `/alunos/{id}` | Busca os dados de um aluno por ID |
| `PUT` | `/alunos/{id}` | Atualiza o registro cadastral de um aluno |
| `DELETE`| `/alunos/{id}` | Remove um aluno do banco de dados |
| `POST` | `/professores` | Cadastra um novo professor |
| `GET` | `/professores` | Lista todos os professores cadastrados |
| `GET` | `/professores/{id}` | Busca os dados de um professor por ID |
| `PUT` | `/professores/{id}` | Atualiza o registro de um professor |
| `DELETE`| `/professores/{id}` | Exclui um professor cadastrado |
| `POST` | `/disciplinas` | Cadastra uma nova disciplina vinculada a um professor |
| `GET` | `/disciplinas` | Lista todas as disciplinas cadastradas |
| `GET` | `/disciplinas/{id}` | Busca disciplina por ID |
| `PUT` | `/disciplinas/{id}` | Atualiza dados da disciplina |
| `DELETE`| `/disciplinas/{id}` | Remove uma disciplina |

### 3. Matrículas e Avaliação Acadêmica (Protegidos por Token JWT)
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/matriculas` | Registra a matrícula de um aluno em disciplina (status inicial: `MATRICULADO`) |
| `PATCH`| `/matriculas/atualizar-notas/{id}` | Lança ou altera `nota1` e `nota2`, calculando a média e definindo `APROVADO`/`REPROVADO` |
| `PATCH`| `/matriculas/trancar/{id}` | Altera o status da matrícula para `TRANCADO` (apenas para matrículas ativas) |

---

## 🔄 Fluxo de Segurança e Autenticação

```text
                    [ Requisição do Usuário ]
                                │
                                ▼
                    ┌───────────────────────┐
                    │    SecurityFilter     │  (Intercepta toda requisição)
                    └───────────┬───────────┘
                                │
                 Token presente no cabeçalho?
                    ├───────────┴───────────┐
                   SIM                     NÃO
                    │                       │
                    ▼                       ▼
       ┌────────────────────────┐  Rota pública permitida?
       │ TokenService.getSubject│  (/login, /cadastros, /swagger-ui)
       └────────────┬───────────┘           │
                    │                  ┌────┴────┐
        Token válido e não expirado?  SIM       NÃO
                    │                  │         │
                   SIM                 │         ▼
                    │                  │    403 Forbidden
                    ▼                  ▼
       ┌────────────────────────┐  Libera para
       │ SecurityContextHolder  │  o Controller
       └────────────────────────┘
```

### Regras de Negócio de Matrícula
- **Cálculo da Média:** Quando ambas as notas (`nota1` e `nota2`) estão registradas, o sistema executa:
  $$\text{Média} = \frac{\text{nota1} + \text{nota2}}{2}$$
  - Se $\text{Média} \ge 7.0 \longrightarrow$ Status: `APROVADO`
  - Se $\text{Média} < 7.0 \longrightarrow$ Status: `REPROVADO`
- **Condição de Trancamento:** Uma matrícula só pode ser trancada se o seu status atual for rigorosamente `MATRICULADO`. Caso contrário, uma `ResponseStatusException` de código `400 Bad Request` é lançada.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 21 LTS
- **Framework Principal:** Spring Boot 4
- **Segurança:** Spring Security 7 & BCrypt Password Encoder
- **Tokens de Acesso:** Auth0 Java-JWT (4.5.1)
- **Documentação de API:** Springdoc OpenAPI 3.1.1 (Swagger UI interativo)
- **Persistência & ORM:** Spring Data JPA / Hibernate
- **Banco de Dados:** PostgreSQL 18
- **Produtividade:** Project Lombok
- **Gerenciador de Dependências:** Apache Maven (com Maven Wrapper `mvnw`)

---

## 🚀 Configuração e Execução

### 1. Pré-requisitos
- **Java JDK 21** instalado e configurado no `PATH`.
- **PostgreSQL** instalado e ativo localmente na porta padrão `5432`.
- Base de dados criada com o nome `aluno_online`.

### 2. Configurações de Ambiente
A aplicação aceita parâmetros via arquivo [application.properties](src/main/resources/application.properties) ou variáveis de ambiente do sistema:

| Propriedade / Variável | Padrão | Descrição |
| :--- | :--- | :--- |
| `DB_URL` | `jdbc:postgresql://localhost:5432/aluno_online` | String de conexão JDBC com o banco |
| `DB_USERNAME` | `postgres` | Usuário do banco PostgreSQL |
| `DB_PASSWORD` | *(definido localmente)* | Senha do usuário do banco |
| `PORT` | `8081` | Porta HTTP da aplicação |
| `JWT_SECRET` | `12345678` | Chave secreta de assinatura do token JWT |

### 3. Executando a Aplicação
No terminal do projeto (na raiz onde se encontra o `mvnw`), execute:

```bash
# No Windows (PowerShell / CMD):
.\mvnw.cmd spring-boot:run

# No Linux / macOS:
./mvnw spring-boot:run
```

As tabelas do banco de dados (`aluno`, `professor`, `disciplina`, `matricula_aluno`, `usuarios`) serão geradas e atualizadas automaticamente pelo Hibernate.

---

## 🧪 Roteiro de Validação via Swagger UI

Com a aplicação iniciada, acesse a interface interativa de documentação no navegador:
👉 **[http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)**

### Passo 1: Cadastro de Usuário (Público)
1. Expanda o endpoint `POST /cadastros`.
2. Clique em **Try it out** e informe o payload JSON:
   ```json
   {
     "login": "aluno.demo",
     "senha": "123"
   }
   ```
3. Clique em **Execute**. Resposta esperada: **`201 Created`** com o ID e login cadastrados.

### Passo 2: Efetuar Login (Público)
1. Expanda o endpoint `POST /login`.
2. Clique em **Try it out** com as mesmas credenciais:
   ```json
   {
     "login": "aluno.demo",
     "senha": "123"
   }
   ```
3. Clique em **Execute**. Resposta esperada: **`200 OK`** retornando o token JWT:
   ```json
   {
     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
   }
   ```
4. Copie o valor do token gerado.

### Passo 3: Autorizar no Swagger (Botão Cadeado)
1. No topo da página do Swagger, clique no botão verde **Authorize** (com ícone de cadeado).
2. No campo **Value**, cole o token copiado.
3. Clique em **Authorize** e em seguida em **Close**. O cadeado passará a exibir o estado trancado.

### Passo 4: Executar Endpoints Protegidos
1. Expanda qualquer rota protegida (exemplo: `GET /alunos`).
2. Clique em **Try it out** e depois em **Execute**.
3. O Swagger enviará automaticamente o cabeçalho `Authorization: Bearer <token>`, retornando **`200 OK`** com a resposta da API.
4. *(Opcional)* Se tentar executar a mesma rota sem autorização prévia, a API retornará **`403 Forbidden`**, atestando o bloqueio de segurança.

---

## 👨‍💻 Autor

**João Victor Andrade Nascimento Ramalho**  
🎓 Bacharelado em Ciência da Computação  
🏛️ Centro Universitário UNIESP  
📚 Disciplina: *Tecnologias para Back-End*  
👨‍🏫 Professor: *Me. Junio Figueirêdo*
