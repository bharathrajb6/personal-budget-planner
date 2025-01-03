# Personal Budget Planner

## Overview

The Personal Budget Planner is a Spring Boot application designed to help users efficiently manage their finances. The
app allows users to perform CRUD operations for transactions and savings goals, register and log in, export transaction
data to CSV files, and send email notifications under specific conditions. It leverages MySQL for data persistence and
Redis for caching to ensure optimal performance.

## Features

### User Management:

- Register and log in securely.
- Manage user sessions with JWT authentication.

### Transaction Management:

- Create, update, view, and delete financial transactions.
- Export transaction history to a CSV file for offline use.

### Savings Goals:

- Define and manage your savings goals.

### Email Notifications:

- Get notified via email for specific conditions (e.g., exceeding budget limits).

### Performance Optimizations:

- Caching using Redis to reduce database load and improve response times.

## Technology Stack

### Backend:

- Java (Spring Boot Framework)
- Spring Security for authentication and authorization
- Spring Data JPA for database interaction

### Database:

- MySQL for storing user and transaction data

### Cache:

- Redis for caching frequently accessed data

## Prerequisites

Before running the application, ensure you have the following installed:

- Java 17 or later
- MySQL 8.0 or later
- Redis

## Installation and Setup

#### 1. Clone the Repository:

```
git clone https://github.com/bharathrajb6/personal-budget-planner.git
cd personal-budget-planner
```

#### 2. Configure the Database: Update the application.properties or application.yml file with your MySQL and Redis configurations:
```
spring.datasource.url=jdbc:mysql://localhost:3306/budget_planner
spring.datasource.username=<your_mysql_username>
spring.datasource.password=<your_mysql_password>
spring.redis.host=localhost
spring.redis.port=6379
```

#### 3. Run the Application:
```
./mvnw spring-boot:run
```

#### 4. Access the Application:

Open your browser and navigate to http://localhost:8080.

## API Endpoints

### User Management:

POST /register - Register a new user

POST /login - Log in and receive a JWT

GET /api/v1/user/getUserDetails - Get User Details

POST /api/v1/user/updateUserDetails - Update user details

POST /api/v1/user/updatePassword - Update password

### Transactions:

GET /api/v1/transaction - Fetch all transactions

GET /api/v1/transaction/{transactionID} - Fetch transaction details based on ID

POST /api/v1/transaction - Add a new transaction

PUT /api/transactions/{transactionID} - Update a transaction

DELETE /api/transactions/{transactionID} - Delete a transaction

GET /api/v1/transaction/filter - Get the transaction based on filter

GET /api/v1/transaction/category/{value} - Get the transaction based on category like Shopping,Grocery etc.

GET /api/v1/transaction/category/{value}/total - Get the total amount spent on that category

GET /api/v1/transaction/export - Export all transactions into a CSV file

GET /api/v1/transaction/export/filter - Export all transactions that happened in specific time into a CSV file

### Savings Goals:

GET /api/v1/goal - Fetch the savings goal

POST /api/v1/goal - Add a new savings goal

PUT /api/v1/goal - Update a savings goal

DELETE /api/v1/goal - Delete a savings goal

## Future Enhancements
- Integration with external budgeting APIs.
- Enhanced analytics and reporting tools.
- Mobile-friendly UI for better accessibility.

## Contributions
Contributions are welcome! Feel free to open issues or submit pull requests to improve the app.**