# 📚 API de Gerenciamento de Impostos
Bem-vindo à API de Gerenciamento de Impostos! Este projeto foi desenvolvido para gerenciar tipos de impostos, realizar cálculos e gerenciar usuários com autenticação baseada em JWT. 🚀

---

## 🛠️ **Tecnologias Utilizadas**
- **Java 17** ☕
- **Spring Boot** 🌱
- **Spring Security** 🔒
- **JWT (JSON Web Token)** 🔑
- **Swagger/OpenAPI** 📖
- **Jakarta Validation** ✅
- **Banco de Dados Relacional** 🗄️

---

## 📋 **Funcionalidades**

### 🔐 **Autenticação e Autorização**
- Autenticação baseada em **JWT**.
- Controle de acesso por **roles** (`ROLE_ADMIN`, `ROLE_USER`).
- Endpoints protegidos para diferentes níveis de acesso.

### 📊 **Gerenciamento de Impostos**
- **CRUD** de tipos de impostos.
- Cálculo de impostos com base em valores fornecidos.

### 👥 **Gerenciamento de Usuários**
- Registro de novos usuários.
- Login com geração de token JWT.
- Listagem de usuários cadastrados (apenas para administradores).

---

## 🚀 **Como Executar o Projeto**
### **Pré-requisitos**
1. **Java 17** instalado.
2. **Maven** instalado.
3. Banco de dados configurado (ex.: MySQL, PostgreSQL).
4. Variáveis de ambiente configuradas:
- `JWT_SECRET_KEY`: Chave secreta para geração de tokens.
- `DB_USERNAME`: Nome de usuário do banco de dados (ex.: `postgres`).
- `DB_URL`: URL de conexão com o banco de dados (ex.: `jdbc:postgresql://localhost:5432/desafio_impostos`).
- `DB_PASSWORD`: Senha do banco de dados (ex.: `123456123456`).

### **Passos para rodar o projeto**
1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/desafio-imposto.git
cd desafio-imposto
```
2. Configure o arquivo `application.properties` com as credenciais do banco de dados:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/impostos
spring.datasource.username=seu-usuario
spring.datasource.password=sua-senha
```
3. Compile e execute o projeto:
```bash
mvn spring-boot:run
```
4. Acesse a documentação da API no navegador:
```
http://localhost:8080/swagger-ui.html
```

---

## 🔑 **Autenticação**
A autenticação é feita via **JWT**. Para acessar os endpoints protegidos, siga os passos:

1. **Registre um usuário**:
- Endpoint: `POST /users`
- Exemplo de payload:
```json
{
"username": "admin",
"password": "123456",
"role": ["ROLE_ADMIN"]
}
```
2. **Faça login**:
- Endpoint: `POST /users/login`
- Exemplo de payload:
```json
{
"username": "admin",
"password": "123456"
}
```
- Resposta:
```json
{
"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```
3. Use o token JWT no cabeçalho das requisições:
```http
Authorization: Bearer <seu-token>
```

---

## 📖 **Documentação da API**
Acesse a documentação completa no Swagger:
```
http://localhost:8080/swagger-ui.html
```

### Exemplos de Endpoints

#### **1. Listar Tipos de Impostos**
- **Método**: `GET`
- **URL**: `/tax/tipos`
- **Resposta**:
```json
[
{
"id": 1,
"name": "ICMS",
"rate": 18.0
},
{
"id": 2,
"name": "ISS",
"rate": 5.0
}
]
```

#### **2. Adicionar Tipo de Imposto**
- **Método**: `POST`
- **URL**: `/tax/tipos`
- **Payload**:
```json
{
"name": "IPI",
"rate": 10.0
}
```
- **Resposta**:
```json
{
"id": 3,
"name": "IPI",
"rate": 10.0
}
```

#### **3. Calcular Imposto**
- **Método**: `POST`
- **URL**: `/tax/calculo`
- **Payload**:
```json
{
"taxId": 1,
"baseValue": 1000.0
}
```
- **Resposta**:
```json
{
"taxName": "ICMS",
"baseValue": 1000.0,
"rate": 18.0,
"calculatedValue": 180.0
}
```

---

## 🔒 **Controle de Acesso**
| **Endpoint**            | **Método** | **Acesso**            |
|-------------------------|------------|-----------------------|
| `/users`                | `POST`     | Público               |
| `/users/login`          | `POST`     | Público               |
| `/tax/tipos`            | `GET`      | Autenticado           |
| `/tax/tipos`            | `POST`     | Somente `ROLE_ADMIN`  |
| `/tax/calculo`          | `POST`     | Somente `ROLE_ADMIN`  |
| `/tax/tipos/{id}`       | `DELETE`   | Somente `ROLE_ADMIN`  |

---

## 🛡️ **Segurança**
- **JWT** é usado para autenticação e autorização.
- Tokens são validados em cada requisição protegida.
- Roles (`ROLE_ADMIN`, `ROLE_USER`) definem o nível de acesso.

---

## 🧪 **Testes**

### **Testando com cURL**
1. **Registrar usuário**:
```bash
curl -X POST http://localhost:8080/users \\
-H "Content-Type: application/json" \\
-d '{"username": "admin", "password": "123456", "role": ["ROLE_ADMIN"]}'
```
2. **Login**:
```bash
curl -X POST http://localhost:8080/users/login \\
-H "Content-Type: application/json" \\
-d '{"username": "admin", "password": "123456"}'
```
3. **Listar tipos de impostos**:
```bash
curl -X GET http://localhost:8080/tax/tipos \\
-H "Authorization: Bearer <seu-token>"
```

---


---

### Feito com Café☕, Paixão ❤️ e Java

