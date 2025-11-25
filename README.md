# API Java Docker - Sistema de Autenticação JWT

API REST completa com autenticação JWT, CRUD de clientes e documentação Swagger. Desenvolvida com Spring Boot 3 (Java 21) + Maven + H2, preparada para desenvolvimento com DevContainer.

## 🚀 Aplicação

A aplicação está rodando em: **http://localhost:8080**

## 📖 Documentação Interativa

Acesse o Swagger UI para testar os endpoints interativamente:
- **Swagger UI**: http://localhost:8080/docs
- **OpenAPI JSON**: http://localhost:8080/api-docs

## 🗄️ Console H2 Database

Para visualizar o banco de dados:
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:file:./data/demo`
- **Username**: `sa`
- **Password**: *(deixe em branco)*

---

## 🔐 Endpoints de Autenticação

### 1. Registrar Usuário

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com",
    "password": "senha123"
  }'
```

**Resposta de Sucesso (201 Created):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "7a3e2f1b-9c8d-4e5f-a6b7-c8d9e0f1a2b3",
  "tokenType": "Bearer"
}
```

### 2. Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "password": "senha123"
  }'
```

**Resposta de Sucesso (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "7a3e2f1b-9c8d-4e5f-a6b7-c8d9e0f1a2b3",
  "tokenType": "Bearer"
}
```

### 3. Refresh Token

```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "7a3e2f1b-9c8d-4e5f-a6b7-c8d9e0f1a2b3"
  }'
```

### 4. Logout

```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "7a3e2f1b-9c8d-4e5f-a6b7-c8d9e0f1a2b3"
  }'
```

---

## 👥 Endpoints de Clientes (Requer Autenticação)

**Importante**: Todos os endpoints de clientes requerem o token JWT no header `Authorization: Bearer {token}`

### 1. Criar Cliente

```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
  -d '{
    "nome": "Maria Santos",
    "cpf": "123.456.789-00",
    "endereco": {
      "logradouro": "Rua das Flores, 123",
      "bairro": "Centro",
      "cidade": "São Paulo",
      "estado": "SP",
      "cep": "01234567"
    }
  }'
```

**Resposta de Sucesso (201 Created):**
```json
{
  "id": 1,
  "nome": "Maria Santos",
  "cpf": "123.456.789-00",
  "endereco": {
    "logradouro": "Rua das Flores, 123",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01234-567"
  },
  "createdAt": "2025-11-25T20:00:00",
  "updatedAt": "2025-11-25T20:00:00"
}
```

### 2. Listar Todos os Clientes

```bash
curl -X GET http://localhost:8080/api/clientes \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

### 3. Buscar Cliente por ID

```bash
curl -X GET http://localhost:8080/api/clientes/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

### 4. Buscar Cliente por CPF

```bash
curl -X GET "http://localhost:8080/api/clientes/cpf/12345678900" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

### 5. Atualizar Cliente

```bash
curl -X PUT http://localhost:8080/api/clientes/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
  -d '{
    "nome": "Maria Santos Silva",
    "cpf": "123.456.789-00",
    "endereco": {
      "logradouro": "Av. Paulista, 1000",
      "bairro": "Bela Vista",
      "cidade": "São Paulo",
      "estado": "SP",
      "cep": "01310100"
    }
  }'
```

### 6. Deletar Cliente

```bash
curl -X DELETE http://localhost:8080/api/clientes/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

---

## ✅ Validações Implementadas

### CPF
- Formato aceito: `123.456.789-00` ou `12345678900`
- Valida dígitos verificadores
- Rejeita CPFs inválidos (ex: `111.111.111-11`)
- Verifica unicidade no banco de dados

### Cliente
- **nome**: obrigatório, entre 3 e 100 caracteres
- **cpf**: obrigatório, formato e dígitos válidos, único
- **endereco.logradouro**: obrigatório
- **endereco.bairro**: obrigatório
- **endereco.cidade**: obrigatório
- **endereco.estado**: obrigatório, 2 caracteres (ex: SP, RJ)
- **endereco.cep**: obrigatório, 8 dígitos

### Autenticação
- **email**: formato válido
- **password**: mínimo 6 caracteres
- **name**: obrigatório para registro

---

## 🔒 Exemplos de Fluxo Completo

### Fluxo 1: Registrar e Criar Cliente

```bash
# 1. Registrar usuário
RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Admin User",
    "email": "admin@example.com",
    "password": "admin123"
  }')

# 2. Extrair token
TOKEN=$(echo $RESPONSE | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)

# 3. Criar cliente
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "nome": "Pedro Costa",
    "cpf": "98765432100",
    "endereco": {
      "logradouro": "Rua A, 100",
      "bairro": "Jardim",
      "cidade": "Rio de Janeiro",
      "estado": "RJ",
      "cep": "20000000"
    }
  }'
```

### Fluxo 2: Login e Listar Clientes

```bash
# 1. Fazer login
RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "admin123"
  }')

# 2. Extrair token
TOKEN=$(echo $RESPONSE | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)

# 3. Listar clientes
curl -X GET http://localhost:8080/api/clientes \
  -H "Authorization: Bearer $TOKEN"
```

---

## ⚠️ Tratamento de Erros

### Erro de Validação (400 Bad Request)

```json
{
  "timestamp": "2025-11-25T20:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "CPF inválido",
  "path": "/api/clientes"
}
```

### Erro de Autenticação (401 Unauthorized)

```json
{
  "timestamp": "2025-11-25T20:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Credenciais inválidas",
  "path": "/api/auth/login"
}
```

### Erro de Autorização (403 Forbidden)

```json
{
  "timestamp": "2025-11-25T20:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Acesso negado",
  "path": "/api/clientes"
}
```

### Recurso Não Encontrado (404 Not Found)

```json
{
  "timestamp": "2025-11-25T20:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Cliente não encontrado",
  "path": "/api/clientes/999"
}
```

---

## 🔄 Hot Reload

A aplicação está configurada com **Spring DevTools** para hot reload automático:
- Altere qualquer arquivo `.java` no diretório `src/`
- A aplicação recompila e reinicia automaticamente
- O token JWT continua válido após o reload

---

## 🏗️ Arquitetura

```
src/main/java/com/example/demo/
├── config/
│   └── OpenApiConfig.java          # Configuração Swagger
├── controller/
│   ├── AuthController.java         # Endpoints de autenticação
│   └── ClienteController.java      # CRUD de clientes
├── dto/
│   ├── auth/                        # DTOs de autenticação
│   └── cliente/                     # DTOs de cliente
├── exception/
│   ├── ErrorResponse.java           # Resposta de erro padronizada
│   └── GlobalExceptionHandler.java  # Tratamento global de exceções
├── model/
│   ├── Cliente.java                 # Entidade Cliente
│   ├── Endereco.java                # Endereço embarcado
│   ├── RefreshToken.java            # Token de refresh
│   └── User.java                    # Usuário (autenticação)
├── repository/
│   ├── ClienteRepository.java
│   ├── RefreshTokenRepository.java
│   └── UserRepository.java
├── security/
│   ├── JwtAuthenticationFilter.java # Filtro JWT
│   ├── JwtTokenProvider.java        # Geração/validação tokens
│   └── SecurityConfig.java          # Configuração Spring Security
├── service/
│   ├── AuthService.java             # Lógica de autenticação
│   ├── ClienteService.java          # Lógica de negócio Cliente
│   ├── CustomUserDetailsService.java
│   └── RefreshTokenService.java
├── validation/
│   ├── CPF.java                     # Anotação de validação
│   └── CPFValidator.java            # Lógica de validação CPF
└── DemoApplication.java
```

---

## 📝 Notas Importantes

1. **Token JWT**: Válido por 1 minuto (configurável em `application.properties`)
2. **Refresh Token**: Válido por 5 minutos
3. **Banco de Dados**: Persiste em arquivo `./data/demo.mv.db`
4. **Formato CPF**: Aceita com ou sem pontuação
5. **Formato CEP**: Armazenado sem hífen, retornado com formatação
6. **Segurança**: Senhas são criptografadas com BCrypt

---

## 🛠️ Tecnologias Utilizadas

- Spring Boot 3.3.13
- Spring Security 6.3.10
- Spring Data JPA
- JWT (JJWT 0.12.5)
- H2 Database
- Bean Validation
- Swagger/OpenAPI 3.0
- Lombok
- Maven

---

## 🚀 Como Executar

### DevContainer (Recomendado)

1. Abra o projeto no VS Code
2. Clique em "Reopen in Container" quando solicitado
3. Execute: `mvn spring-boot:run`

### Docker Compose

```bash
docker-compose up
```

### Maven Local

```bash
mvn spring-boot:run
```

A aplicação estará disponível em http://localhost:8080 com hot reload ativo.

---

## 📄 Licença

Este projeto é um exemplo para fins educacionais.
