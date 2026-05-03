# OrderFlow

OrderFlow is an event-driven trading engine built with Java and Spring Boot. It supports limit order submission, price-time-priority matching, trade execution, PostgreSQL persistence, and a REST API for interacting with orders, trades, and order book state.

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Docker
- JUnit

Planned additions:

- Redis for live order book snapshots
- Kafka for trade event streaming
- GitHub Actions for continuous integration
- Benchmark scripts for local throughput testing

## Core Features

- Submit buy and sell limit orders
- Match orders using price-time priority
- Support full and partial fills
- Track open, partially filled, filled, and cancelled orders
- Persist orders and trades
- Expose REST APIs for orders, trades, and order book snapshots

## Matching Engine Design

OrderFlow uses a price-time-priority matching engine.

Buy orders are prioritized by:

1. Highest price first
2. Earliest creation time first

Sell orders are prioritized by:

1. Lowest price first
2. Earliest creation time first

When a new buy order arrives, it matches against the lowest-priced sell orders where the sell price is less than or equal to the buy price.

When a new sell order arrives, it matches against the highest-priced buy orders where the buy price is greater than or equal to the sell price.

## Project Status

This project is currently in active development.

Current milestone:

- [x] Initialize project structure
- [x] Configure Maven project
- [ ] Implement domain models
- [ ] Implement matching engine
- [ ] Add unit tests
- [ ] Add REST APIs
- [ ] Add PostgreSQL persistence
- [ ] Add Docker Compose
- [ ] Add Redis caching
- [ ] Add Kafka trade events

## Running Locally

This section will be updated once the first working Spring Boot application is implemented.

Planned command:

```bash
./mvnw spring-boot:run