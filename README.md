# delivery-api-tiago-ribeiro

# Delivery Tech API


API REST desenvolvida em **Spring Boot 3.2.x** e **Java 21 (JDK 21)** para gerenciar uma API de delivery de forma simples e organizada.


## 🚀 Tecnologias
- Java 21 (JDK 21)
- Spring Boot 3.2.x
- Spring Data JPA
- MySQL
- Podman
- Lombok
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

____________________________________________________________________________________________________________________

## ⚙️ EXECUÇÃO DO PROJETO


🧪 ROTEIRO DE TESTES E EXEMPLOS DE USO
Os testes abaixo podem ser executados diretamente no Postman ou Insomnia.

# 🔒 AUTORIZAÇÃO
1️⃣ Cadastrar usuário
POST /auth/register

```
{
  "nome": "Cleber",
  "email": "cleberADMIN@cleberleao.com",
  "senha": "123456789",
  "role": "ADMIN"
}
```

🟢 Esperado: Retorna 201 Created com os dados do usuário salvo.

2️⃣ Logar usuário
POST /auth/login

```
{
  "email": "cleberADMIN@cleberleao.com",
  "senha": "123456789"
}
```

🟢 Esperado: Retorna 201 Created com o usuário logado.
____________________________________________________________________________________________________________________

# 🧍 CLIENTES
1️⃣ Criar Cliente
POST /clientes

json
Copiar código
```
{
  "nome": "João Silva",
  "email": "joaosilva@email.com",
  "telefone": "(11) 91234-5678",
  "endereco": "Rua das Flores, 123, São Paulo, SP"
}
```
🟢 Esperado: Retorna 201 Created com os dados do cliente salvo.

2️⃣ Listar Clientes
GET /clientes

🟢 Esperado: Retorna lista com todos os clientes ativos.

3️⃣ Buscar Cliente por ID
GET /clientes/{id}

🟢 Esperado: Retorna os dados do cliente com ID 1.

4️⃣ Buscar Cliente por Email
GET /clientes/email/{email}

🟢 Esperado: Retorna os dados do cliente com o email digitado.

5️⃣Buscar Cliente por Nome
GET /clientes/buscar/{nome}

🟢 Esperado: Retorna os dados do cliente com o nome digitado.

6️⃣ Atualizar Cliente
PUT /clientes/{id}

json
Copiar código
```
{
  "nome": "João Silva",
  "email": "joaosilva@email.com",
  "telefone": "(11) 91234-5678",
  "endereco": "Rua das Flores, 123, São Paulo, SP"
}
```
🟢 Esperado: Retorna 200 OK com os dados atualizados.

7️⃣ Inativar Cliente
DELETE /clientes/{id}

🟢 Esperado: Retorna 204 No Content e o cliente deixa de aparecer nas buscas ativas.

____________________________________________________________________________________________________________________

# 🍴 RESTAURANTES
1️⃣ Cadastrar Restaurante
POST /restaurantes

json
Copiar código
```
{
  "nome": "Pizzaria Express",
  "categoria": "Melhores pizzas da cidade",
  "cep": "12345-678",
  "endereco": "Rua das Flores, 123",
  "telefone": "+5511999999999",
  "taxaEntrega": 5,
  "avaliacao": 4.5,
  "ativo": true
}
```
🟢 Esperado: Restaurante criado com sucesso (201 Created).

2️⃣ Listar Restaurantes
GET /restaurantes

🟢 Esperado: Lista todos os restaurantes cadastrados.

3️⃣ Buscar por Categoria
GET /restaurantes/categoria/{categoria}

🟢 Esperado: Retorna apenas restaurantes da categoria escolhida.

4️⃣ Buscar Top 5
GET /restaurantes/top-cinco

🟢 Esperado: Retorna apenas cinco restaurantes com as melhores avaliações.

5️⃣ Ordenar por taxa de entrega
GET /restaurantes/taxa-entrega

🟢 Esperado: Retorna restaurantes por taxa de entrega.

6️⃣ Gerar um relatório de vendas
GET /restaurantes/relatorio-vendas

🟢 Esperado: Gerar um relatório de vendas por restaurante.

7️⃣ Buscar por faixa de preço
GET /restaurantes/preco/{precoMinimo}/{precoMaximo}

🟢 Esperado: Lista todos os restaurantes dentro de uma faixa de preço específica.

8️⃣ Buscar por nome
GET /restaurantes/nome/{nome}

🟢 Esperado: Recupera os detalhes de um restaurante específico pelo nome.

9️⃣ Inativar e/ou inativar um restaurante
PATCH /restaurantes/{id}/ativar-desativar

🟢 Esperado: Ativa ou desativa um restaurante pelo ID com "ativo" sendo true ou false.

🔟 Atualizar restaurante
PUT /restaurantes/{id}

🟢 Esperado: Atualiza os detalhes de um restaurante existente pelo ID.

json
Copiar código
```
{
  "nome": "Pizzaria Express",
  "categoria": "Melhores pizzas da cidade",
  "cep": "12345-678",
  "endereco": "Rua das Flores, 123",
  "telefone": "+5511999999999",
  "taxaEntrega": 5,
  "avaliacao": 4.5,
  "ativo": true
}
```
____________________________________________________________________________________________________________________

# 🍕 PRODUTOS
1️⃣ Cadastrar Produto
POST /produtos

json
Copiar código
{
  "nome": "Pizza Margherita",
  "descricao": "Deliciosa pizza com molho de tomate, mussarela e manjericão",
  "preco": 29.9,
  "categoria": "Pizzas",
  "disponivel": true,
  "restauranteId": 1
}
🟢 Esperado: Retorna 201 Created com os dados do produto salvo.

2️⃣ Buscar por Restaurante
GET /produtos/restaurante/{id}

🟢 Esperado: Retorna produtos do restaurante com ID digitado.

3️⃣ Buscar por Categoria
GET /produtos/categoria/{categoria}

🟢 Esperado: Lista produtos da categoria digitada.

4️⃣ Buscar Todos
GET /produtos

🟢 Esperado: Retorna todos produtos os produtos cadastrados.

5️⃣ Buscar por restaurante
GET /produtos/restaurante/{restauranteId}

🟢 Esperado: Lista todos os produtos de um restaurante específico pelo ID.

6️⃣ Buscar por preço
GET /produtos/preco/{valor}

🟢 Esperado: Lista todos os produtos com preço menor ou igual ao valor especificado.

7️⃣ Buscar por nome
GET /produtos/nome/{nome}

🟢 Esperado: Recupera os detalhes de um produto específico pelo nome.

8️⃣ Ativar/Desativar produto
PATCH /produtos/{id}/ativar-desativar

🟢 Esperado: Ativa ou desativa um produto pelo ID com "disponivel" sendo true ou false.

9️⃣ Atualizar produto
PUT /produtos/{id}

```
{
  "nome": "Pizza Margherita",
  "descricao": "Deliciosa pizza com molho de tomate, mussarela e manjericão",
  "preco": 29.9,
  "categoria": "Pizzas",
  "disponivel": true,
  "restauranteId": 1
}
```

🟢 Esperado: Atualiza os detalhes de um produto existente pelo ID.

____________________________________________________________________________________________________________________

# 📦 PEDIDOS
1️⃣ Criar Pedido
POST /pedidos

json
Copiar código
{
  "numeroPedido": "12345",
  "dataPedido": "2023-10-01",
  "valorTotal": 99.99,
  "observacoes": "Não colocar cebola",
  "clienteId": "PENDENTE",
  "restauranteId": 1,
  "enderecoEntrega": "Rua das Flores, 123",
  "cep": "string",
  "itens": [
    {
      "produtoId": 1,
      "quantidade": 2
    }
  ]
}
🟢 Esperado: Pedido criado com valor total calculado automaticamente.

2️⃣ Consultar Pedidos por Cliente
GET /pedidos/cliente/{id}

🟢 Esperado: Retorna lista de pedidos feitos pelo id de cliente digitado.

3️⃣ Filtrar por Status
GET /pedidos/status/{status}

🟢 Esperado: Retorna todos os pedidos com status digitado.

4️⃣ Filtrar por Data
GET /pedidos/data/2025-10-30

🟢 Esperado: Lista pedidos criados nessa data.

5️⃣  Filtrar por recentes
GET /pedidos/recentes

🟢 Esperado: Lista pedidos criados recentemente.

6️⃣ Filtrar por restaurante
GET /pedidos/restaurante/{id}

🟢 Esperado: Retorna lista de pedidos feitos pelo id de restaurante digitado.

7️⃣ Atualizar Status do Pedido
PUT /pedidos/{id}/{status}

json
Copiar código
{
  "status": "ENTREGUE"
}
🟢 Esperado: Retorna 200 OK com status atualizado no pedido fonecido em {id}.

____________________________________________________________________________________________________________________

## 💾 BANCO DE DADOS H2
A aplicação utiliza H2 Database em memória.
Para visualizar os dados:

Acesse: http://localhost:8080/h2-console

Configure:

JDBC URL: jdbc:h2:mem:deliverydb

Usuário: sa

Senha: (vazio)

## 💾 BANCO DE DADOS MySQL
Para visualizar os dados:

Em um gerenciador de banco de dados a sua escolha:

Configure:

JDBC URL: jdbc:mysql://localhost:3306/delivery_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true

Usuário: root

Senha: 123456

Tabelas criadas automaticamente:

CLIENTE
RESTAURANTE
PRODUTO
PEDIDO
USUARIOS
ITEM_PEDIDO

____________________________________________________________________________________________________________________

## 🔬 Teste

Comando para realizar testes

```
mvn test
```
____________________________________________________________________________________________________________________

## 📦 Containerização usando Podman

📃 Use o cmd ou powershell e execute os comando

# Criar uma network
```
podman network create appnet
```

# Criar um container para base do MySQL

```
podman pull mysql:8
```
```
podman run -d -p 3306:3306  --name delivery_db --network appnet  -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_ROOT_HOST='%' -e MYSQL_DATABASE=delivery_db -v mysql_data:/var/lib/mysql mysql:8
```

# Criar um container para a API ao mesmo tempo que conecta com o MySQL

```
podman run -d --name delivery-api `
  --network appnet `
  -p 8080:8080 `
  -e SPRING_DATASOURCE_URL="jdbc:mysql://delivery_db:3306/delivery_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" `
  -e SPRING_DATASOURCE_USERNAME="root" `
  -e SPRING_DATASOURCE_PASSWORD="123456" `
  localhost/delivery-api:latest
```

____________________________________________________________________________________________________________________

## 📂 ESTRUTURA DE PROJETO (RESUMO)

src
└── main/
    └── java/com/deliverytech/delivery_api/
        ├── config/          → Configurações gerais da aplicação (Web, Security).
        ├── controller/      → Endpoints REST (@RestController). Recebe requisições HTTP.
        ├── service/
        │   ├── impl/        → Implementações da Lógica de Negócio principal.
        │   └── (Interfaces) → Contratos dos serviços.
        ├── repository/      → Consultas JPA (@JpaRepository). Camada de acesso a dados.
        ├── model/           → Entidades do Banco de Dados (@Entity).
        ├── security/        → Componentes do Spring Security (JWT, Filtros).
        ├── dto/             → Objetos de Transferência de Dados (Request/Response).
        ├── exceptions/      → Classes de exceção personalizadas.
        ├── validation/      → Lógica para validação de dados de entrada.
        ├── projection/      → Interfaces para projeções de dados específicas.
        └── DeliveryApiApplication.java → Classe principal da aplicação.

    └── resources/
        ├── application.properties/yml  → Configurações (Banco de Dados, Servidor, etc.).
        └── data.sql (opcional)         → Scripts para carga inicial de dados.

└── test/
    └── java/com/deliverytech/delivery_api/
        └── (Estrutura de pacotes espelhada para Testes Unitários e de Integração)

____________________________________________________________________________________________________________________

# 👨‍💻 Desenvolvedor
Tiago Ribeiro Pereira
Análise e Desenvolvimento de Sistemas
Desenvolvido com JDK 21 e Spring Boot 3.2.x
