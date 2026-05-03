# OrderFlow

OrderFlow is a Java/Spring Boot trading engine that supports limit order submission, price-time-priority matching, trade execution, PostgreSQL persistence, and REST APIs for orders, trades, and order book snapshots.

The project is designed to model the backend of an exchange-style order matching system.

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Docker Compose
- JUnit
- GitHub Actions

Planned additions:

- Redis for cached order book snapshots
- Kafka for trade event streaming
- Benchmark scripts for local throughput testing

## Features

- Submit buy and sell limit orders
- Match orders using price-time priority
- Support full fills and partial fills
- Track order states: `OPEN`, `PARTIALLY_FILLED`, `FILLED`, and `CANCELLED`
- Persist orders and trades with PostgreSQL
- Expose REST APIs for orders, trades, and order book state
- Run unit tests automatically with GitHub Actions

## Matching Engine Design

OrderFlow uses a price-time-priority matching engine.

Buy orders are prioritized by:

1. Highest price first
2. Earliest creation time first

Sell orders are prioritized by:

1. Lowest price first
2. Earliest creation time first

A buy order matches when its price is greater than or equal to the best sell price.

A sell order matches when its price is less than or equal to the best buy price.

The execution price is taken from the resting order already in the book.

## API Endpoints

### Create an order

```http
POST /api/orders