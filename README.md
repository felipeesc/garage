#  Estapar Backend Developer 

##  Objetivo
Implementar um sistema backend simples para gerenciar um estacionamento:

- Controlar vagas disponíveis
- Entrada/saída de veículos
- Calcular receita por setor e data

O sistema se integra a um **simulador de garagem** que envia eventos de veículos via webhook.

---

##  Tecnologias
- Java 21
- Spring Boot 3 (Web, Data JPA, Validation)
- MySQL 8
- Docker
- Lombok
- Maven

---

##  Como rodar o projeto

### 1. Subir banco de dados
```bash
     docker run -d --name garage-db   -e MYSQL_ROOT_PASSWORD=root   -e MYSQL_DATABASE=garage   -p 3306:3306 mysql:8.0
```

### 2. Subir simulador
```bash
   docker run -d --network="host" cfontes0estapar/garage-sim:1.0.0
```

O simulador irá:
- Expor `GET /garage` com a configuração inicial da garagem
- Enviar eventos para `POST /webhook`

### 3. Rodar aplicação
```bash
   ./mvnw spring-boot:run
```

A API sobe por padrão em: [http://localhost:3003](http://localhost:3003)

---

## Endpoints implementados

### 🔹 Webhook — `POST /webhook`

#### ENTRY
```json
{
  "license_plate": "ZUL0001",
  "entry_time": "2025-01-01T12:00:00.000Z",
  "event_type": "ENTRY"
}
```

#### PARKED
```json
{
  "license_plate": "ZUL0001",
  "lat": -23.561684,
  "lng": -46.655981,
  "event_type": "PARKED"
}
```

#### EXIT
```json
{
  "license_plate": "ZUL0001",
  "exit_time": "2025-01-01T16:40:00.000Z",
  "event_type": "EXIT"
}
```

---

###  Receita — `GET /revenue`

#### Exemplo de requisição
```bash
  curl -X GET "http://localhost:3003/revenue?date=2025-01-01&sector=A"   -H "Accept: application/json"
```

#### Exemplo de resposta
```json
{
  "amount": 150.00,
  "currency": "BRL",
  "timestamp": "2025-01-01T23:59:59.000Z"
}
```

---

##  Regras de Negócio

- **ENTRY** → escolhe setor com vaga disponível, salva `entry_time` e setor
- **PARKED** → marca a vaga (`Spot`) como ocupada pelo veículo
- **EXIT** → libera vaga, calcula preço (30 min grátis + tarifa/hora + ajuste por ocupação)
- **Lotação máxima** → bloqueia novas entradas quando setor cheio

---

##  Estrutura do projeto

```
src/main/java/com/estapar/garage
├── controllers        # REST Controllers
├── domains            # Entidades JPA
├── dtos               # DTOs de entrada/saída
├── enums             # Enums (ENTRY, PARKED, EXIT)
├── repositorys        # Repositórios JPA
├── services          # Serviços de negócio
├── startup           # Inicialização (GarageInitializer)
└── GarageApplication # Classe principal
```
---

