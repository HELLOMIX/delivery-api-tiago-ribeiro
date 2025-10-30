# delivery-api-tiago-ribeiro

# Delivery Tech API


API REST desenvolvida em **Spring Boot 3.2.x** e **Java 21 (JDK 21)** para gerenciar uma API de delivery de forma simples e organizada.


## 🚀 Tecnologias
- **Java 21 LTS** (versão mais recente)
- Spring Boot 3.2.x
- Spring Web
- Spring Data JPA
- H2 Database
- Maven


## ⚡ Recursos Modernos Utilizados
- Records (Java 14+)
- Text Blocks (Java 15+)
- Pattern Matching (Java 17+)
- Virtual Threads (Java 21)


## 🏃‍♂️ Como executar
1. **Pré-requisitos:** JDK 21 instalado
2. Clone o repositório
3. Execute: `./mvnw spring-boot:run`
4. Acesse: http://localhost:8080/health


## 📋 Endpoints
- GET /health - Status da aplicação (inclui versão Java)
- GET /info - Informações da aplicação
- GET /h2-console - Console do banco H2


## 🔧 Configuração
- Porta: 8080
- Banco: H2 em memória
- Profile: development

---

## ⚙️ EXECUÇÃO DO PROJETO


🧪 ROTEIRO DE TESTES E EXEMPLOS DE USO
Os testes abaixo podem ser executados diretamente no Postman ou Insomnia.

🧍 CLIENTES
1️⃣ Criar Cliente
POST /clientes

json
Copiar código
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "telefone": "11999999999",
  "endereco": "Rua das Flores, 123",
  "ativo": true
}
🟢 Esperado: Retorna 201 Created com os dados do cliente salvo.

2️⃣ Listar Clientes
GET /clientes

🟢 Esperado: Retorna lista com todos os clientes ativos.

3️⃣ Buscar Cliente por ID
GET /clientes/1

🟢 Esperado: Retorna os dados do cliente com ID 1.

4️⃣ Atualizar Cliente
PUT /clientes/1

json
Copiar código
{
  "nome": "João Pedro Silva",
  "telefone": "11988888888",
  "ativo": true
}
🟢 Esperado: Retorna 200 OK com os dados atualizados.

5️⃣ Inativar Cliente
DELETE /clientes/1

🟢 Esperado: Retorna 204 No Content e o cliente deixa de aparecer nas buscas ativas.

🍴 RESTAURANTES
1️⃣ Cadastrar Restaurante
POST /restaurantes

json
Copiar código
{
  "nome": "Pizzaria Bella Napoli",
  "categoria": "Italiana",
  "avaliacao": 4.8,
  "ativo": true
}
🟢 Esperado: Restaurante criado com sucesso (201 Created).

2️⃣ Listar Restaurantes
GET /restaurantes

🟢 Esperado: Lista todos os restaurantes cadastrados.

3️⃣ Buscar por Categoria
GET /restaurantes/categoria/Italiana

🟢 Esperado: Retorna apenas restaurantes da categoria “Italiana”.

4️⃣ Buscar Ativos
GET /restaurantes/ativos

🟢 Esperado: Retorna apenas restaurantes com ativo = true.

5️⃣ Ordenar por Avaliação
GET /restaurantes/ordenar/avaliacao

🟢 Esperado: Retorna restaurantes ordenados da melhor para a pior avaliação.

🍕 PRODUTOS
1️⃣ Cadastrar Produto
POST /produtos

json
Copiar código
{
  "nome": "Pizza Margherita",
  "preco": 45.90,
  "categoria": "Pizza",
  "disponivel": true,
  "restauranteId": 1
}
🟢 Esperado: Retorna 201 Created com os dados do produto salvo.

2️⃣ Buscar por Restaurante
GET /produtos/restaurante/1

🟢 Esperado: Retorna produtos do restaurante com ID 1.

3️⃣ Buscar por Categoria
GET /produtos/categoria/Pizza

🟢 Esperado: Lista produtos da categoria “Pizza”.

4️⃣ Buscar Disponíveis
GET /produtos/disponiveis

🟢 Esperado: Retorna apenas produtos com disponivel = true.

📦 PEDIDOS
1️⃣ Criar Pedido
POST /pedidos

json
Copiar código
{
  "clienteId": 1,
  "restauranteId": 1,
  "produtos": [1, 2],
  "status": "PENDENTE"
}
🟢 Esperado: Pedido criado com valor total calculado automaticamente.

2️⃣ Consultar Pedidos por Cliente
GET /pedidos/cliente/1

🟢 Esperado: Retorna lista de pedidos feitos pelo cliente 1.

3️⃣ Filtrar por Status
GET /pedidos/status/PENDENTE

🟢 Esperado: Retorna todos os pedidos com status “PENDENTE”.

4️⃣ Filtrar por Data
GET /pedidos/data/2025-10-30

🟢 Esperado: Lista pedidos criados nessa data.

5️⃣ Atualizar Status do Pedido
PUT /pedidos/1/status

json
Copiar código
{
  "status": "ENTREGUE"
}
🟢 Esperado: Retorna 200 OK com status atualizado.

💾 BANCO DE DADOS H2
A aplicação utiliza H2 Database em memória.
Para visualizar os dados:

Acesse: http://localhost:8080/h2-console

Configure:

JDBC URL: jdbc:h2:mem:deliverydb

Usuário: sa

Senha: (vazio)

Tabelas criadas automaticamente:

objectivec
Copiar código
CLIENTE
RESTAURANTE
PRODUTO
PEDIDO
📂 ESTRUTURA DE PROJETO (RESUMO)
css
Copiar código
src/
 └── main/
     ├── java/com/deliverytech/delivery_api/
     │   ├── controller/        → Endpoints REST
     │   ├── service/           → Lógica de negócio
     │   ├── repository/        → Consultas JPA
     │   ├── model/             → Entidades
     │   └── DeliveryApiApplication.java
     └── resources/
         ├── application.properties
         └── data.sql (opcional para carga inicial)
🧰 TECNOLOGIAS
Java 21 (JDK 21)

Spring Boot 3.2.x

Spring Data JPA

Lombok

H2 Database

Maven

👨‍💻 Desenvolvedor
Tiago Ribeiro Pereira
Análise e Desenvolvimento de Sistemas
Desenvolvido com JDK 21 e Spring Boot 3.2.x
