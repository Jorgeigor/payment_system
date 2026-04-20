# 💳 Payment System - REST API

A robust RESTful API built to handle digital wallet operations and money transfers, based on the architecture of the **PicPay Backend Challenge**. This backend service provides secure and consistent endpoints for user management, balance deposits, and peer-to-peer transactions.

## ✨ Features

- **User Management:** Registration and authentication for two types of clients: Common Users (PF - Pessoa Física) and Merchants (PJ - Pessoa Jurídica). Ensures unique CPF/CNPJ and Email validations.
- **Wallet & Deposits:** Allows Common Users to securely add funds to their digital wallets.
- **Transaction Engine:** - Validates balances before processing transfers.
    - Enforces business rules: Merchants (PJ) can only receive transfers, not send them.
- **External Mock Services Resilience:**
    - **Authorization:** Integrates with an external mock service to authorize transfers.
    - **Notifications:** Simulates sending emails/SMS via an external API. Built with resilience to not rollback transactions if the notification service is temporarily unavailable.
- **Statement/History:** Generates detailed transaction histories, separating incoming transfers, outgoing transfers, and direct deposits.

## 🛠️ Tech Stack

- **Language:** [Java 17+](https://www.java.com/)
- **Framework:** [Spring Boot 3](https://spring.io/projects/spring-boot)
- **Data Access:** Spring Data JPA / Hibernate
- **Database:** [PostgreSQL](https://www.postgresql.org/)
- **Containerization:** [Docker](https://www.docker.com/) & Docker Compose
- **API Communication:** `RestTemplate` for external service consumption
- **Data Transfer:** Java Records for concise and immutable DTOs
- **Build Tool:** Maven

## 🏗️ Architecture & Technical Highlights

- **Modular Structure:** Code is organized into cohesive modules (`client`, `transaction`, `api`) to separate domain boundaries.
- **DTO Pattern:** Extensively uses Java Records (DTOs) to isolate database entities from the presentation layer, preventing data leakage and over-fetching.
- **Resilient Integrations:** The notification system is designed to fail gracefully. If the external notification mock goes offline, the core transaction still succeeds, and the UI is informed accordingly.
- **Audit-Ready Transactions:** All transfers and deposits are logged via `TransactionalEntity`, providing a reliable source of truth for user statements.

## 🚀 Getting Started

### Prerequisites
Make sure you have [Java 17+](https://adoptium.net/) and [Maven](https://maven.apache.org/) installed on your machine.

### Installation & Execution

1. Clone the repository:
   ```bash
   git clone https://github.com/Jorgeigor/payment_system.git
    ```
2. Navigate to the project directory:
```bash
    cd payment-system-api
```
3. 🐳 Start the Infrastructure (Docker):
```bash

docker-compose up -d
```
4. Configure the Database:
```bash
   Update your src/main/resources/application.properties with the credentials defined in the docker-compose.yml.
````
5. Build and run the application:
```bash
mvn spring-boot:run
```
The API will be available at http://localhost:8080.
## 📂 Project Structure
```bash
src/main/java/com/picpay/payment_system/
├── modules/
│   ├── api/            # Global API configurations and shared DTOs
│   ├── client/         # Client domain (Entities, Repositories, Services, DTOs)
│   └── transaction/    # Transaction domain (Transfer logic, Statements, External API calls)
└── PaymentSystemApplication.java
```
## 👤 Author
Developed by Jorge Igor Gomes

[![Linkedin Badge](https://img.shields.io/badge/-LinkedIn-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/jorge-igor-gomes/)](https://www.linkedin.com/in/jorge-igor-gomes/)