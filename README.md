# Order Notification System

Sistema de microsserviços desenvolvido com Java 21 e Spring Boot 3, demonstrando comunicação assíncrona via Kafka, APIs REST, persistência com JPA e cobertura de testes acima de 80% com JUnit 5 e Jacoco. Aplica princípios SOLID e padrões de projeto em dois serviços independentes.

---

## Arquitetura

```
order-notification-system/
├── order-service/          # Gerencia pedidos e publica eventos no Kafka
├── notification-service/   # Consome eventos e registra notificações
└── docker-compose.yml      # Kafka + Zookeeper
```

### Fluxo

```
Cliente REST → Order Service → Kafka (orders-topic) → Notification Service
                    ↓                                          ↓
                H2 / Aurora                             H2 / DynamoDB
```

---

## Tecnologias

- Java 21
- Spring Boot 3.5
- Spring Data JPA
- Spring Kafka
- H2 Database (dev)
- JUnit 5 + Mockito
- Jacoco (cobertura ≥ 80%)
- Springdoc OpenAPI (Swagger)
- Docker + Docker Compose
- Lombok

---

## Serviços

### Order Service — porta 8080

Responsável por criar e consultar pedidos. Ao criar um pedido, publica um evento `OrderCreatedEvent` no tópico `orders-topic` do Kafka.

**Endpoints:**

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/orders` | Cria um novo pedido |
| GET | `/orders` | Lista todos os pedidos |
| GET | `/orders/{id}` | Busca pedido por ID |

### Notification Service — porta 8081

Consome eventos do tópico `orders-topic` e persiste uma notificação no banco de dados.

**Endpoints:**

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/notifications` | Lista todas as notificações |
| GET | `/notifications/order/{orderId}` | Busca notificações por pedido |

---

## Como executar

### Pré-requisitos

- Java 21
- Maven 3.8+
- Docker Desktop

### 1. Subir o Kafka

```bash
docker compose up -d
```

### 2. Executar o Order Service

```bash
cd order-service
mvn spring-boot:run
```

### 3. Executar o Notification Service

```bash
cd notification-service
mvn spring-boot:run
```

### Swagger

- Order Service: http://localhost:8080/swagger-ui/index.html
- Notification Service: http://localhost:8081/swagger-ui/index.html

### H2 Console

- Order Service: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:orderdb`
- Notification Service: http://localhost:8081/h2-console
  - JDBC URL: `jdbc:h2:mem:notificationdb`

---

## Testes

```bash
# Rodar todos os testes com relatório de cobertura
mvn clean test

# Visualizar relatório do Jacoco
# Abrir: target/site/jacoco/index.html
```

Cobertura atual do Order Service: **83%**

---

## Padrões aplicados

### SOLID

**Single Responsibility (S)**
Cada classe tem uma única responsabilidade. `OrderController` apenas recebe e responde requisições HTTP. `OrderService` contém exclusivamente a lógica de negócio. `IOrderRepository` cuida apenas do acesso ao banco de dados. `OrderProducer` é responsável apenas por publicar eventos no Kafka.

**Open/Closed (O)**
O `GlobalExceptionHandler` é extensível sem modificação. Para tratar um novo tipo de exceção basta adicionar um novo método anotado com `@ExceptionHandler` — nenhuma classe existente precisa ser alterada.

**Dependency Inversion (D)**
Todas as dependências são injetadas via construtor com `@RequiredArgsConstructor`. As classes dependem de abstrações (interfaces como `JpaRepository`) e não de implementações concretas, facilitando substituição e testes com mocks.

### Design Patterns

**Repository Pattern**
`IOrderRepository` abstrai o acesso ao banco de dados. A implementação é gerada automaticamente pelo Spring Data JPA, permitindo trocar a base de dados sem alterar a lógica de negócio.

**DTO Pattern**
`OrderRequestDTO` e `OrderResponseDTO` separam a camada de transporte da entidade de domínio. Nenhum dado interno da entidade é exposto diretamente pela API.

**Builder Pattern**
Lombok `@Builder` em todas as entidades e DTOs garante construção imutável e legível dos objetos, evitando construtores com muitos parâmetros.

**Event-Driven / Observer**
A comunicação entre `Order Service` e `Notification Service` é feita via eventos Kafka. O `Order Service` não conhece o `Notification Service` — apenas publica um `OrderCreatedEvent`. Isso desacopla os serviços e permite escalar e evoluir cada um de forma independente.

---

## Estrutura do Order Service

```
order-service/
└── src/main/java/com/github/anacarlag/order_service/
    ├── controller/        # Camada de entrada HTTP
    ├── service/           # Lógica de negócio
    ├── repository/        # Acesso ao banco de dados
    ├── model/             # Entidades JPA
    ├── dto/               # Objetos de transferência de dados
    ├── event/             # Eventos Kafka
    ├── producer/          # Publicação no Kafka
    └── exception/         # Tratamento global de erros
```

---

## Autora

**Ana Carla** — Desenvolvedora de Software  
[github.com/anacarlag](https://github.com/anacarlag)