# Tennis Scoring – Technical Test (Spring Boot / Java)

This project is an implementation of a **tennis game scoring system** as part of a technical test.

The goal is to expose a REST API that:
- takes as input a sequence of balls won by players `A` or `B`,
- computes the score after each ball,
- and returns all intermediate scores plus the final winner.

The scoring rules follow standard tennis logic (15/30/40, deuce, advantage, game).

---

## 1. Tech Stack

- **Java 21**
- **Spring Boot 3**
- **Maven**
- **JUnit 5**, **AssertJ** for testing

---

## 2. Architecture Overview

The project follows a **hexagonal / layered architecture** idea:

- **Domain (`com.example.tennis.domain`)**
    - Pure business logic, no Spring dependency.
    - `Player` enum
    - `TennisGame` class: core scoring rules (15/30/40, deuce, advantage, winner).

- **Application (`com.example.tennis.application`)**
    - Use case / service layer.
    - `TennisScoringService`: consumes a string sequence (e.g. `"ABABAA"`), plays the game step by step, returns intermediate scores and final winner.

- **Infrastructure / Web (`com.example.tennis.infrastructure.web`)**
    - `TennisController`: REST controller exposing the scoring endpoint.
    - `GlobalExceptionHandler`: `@RestControllerAdvice` to centralize error handling.
    - `ErrorResponse`: standard error payload for HTTP errors.

    
The project is designed so that the **domain layer is fully testable and independent** of any framework.

---

## 3. Business Rules

- Each player starts at **0 point**.
- First ball won → **15** points.
- Second ball won → **30** points.
- Third ball won → **40** points.
- If a player has 40 and wins the ball, he wins the game, except when:
    - both players are at 40 → **“Deuce”**
    - from **Deuce**:
        - winner of next ball → **“Advantage Player X”**
        - if player with advantage wins ball → **wins the game**
        - if player without advantage wins ball → back to **Deuce**

The input is a `String` containing characters:
- `'A'` → *player A won the ball*
- `'B'` → *player B won the ball*

The system prints / returns the score **after each ball**, and finally the **winner of the game**.

---

## 4. How to Run

### 4.1. Run tests

```bash
mvn clean test
