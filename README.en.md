# Camunda Order Saga Demo

> [Türkçe](README.md)

## Run

With Docker Desktop running, from the project root:

```bash
docker compose up --build
```

This command **both builds and starts** the app (`build` + `up` together). The terminal stays open with logs. When ready, `http://localhost:8080` responds.

To build and start separately:

```bash
docker compose build
docker compose up
```

To stop: `Ctrl+C`, then `docker compose down`

## Example request

```bash
curl -X POST http://localhost:8080/orders -H "Content-Type: application/json" -d "{\"customerId\":\"ahmet\",\"amount\":100}"
```

## Camunda Cockpit

Open in your browser to monitor processes visually:

**http://localhost:8080/camunda/app/cockpit/**

- Username: `demo`
- Password: `demo`

Cockpit → **Processes** → `orderSaga` → view running instances and their current steps.

## How Camunda works here

There is **no separate Camunda server** in this project. Camunda runs embedded inside the Spring Boot application.

1. On startup, `order-saga.bpmn` is deployed and Camunda creates `ACT_*` tables in Postgres
2. `POST /orders` → Camunda starts the `orderSaga` process
3. The BPMN flow runs step by step: Create Order → Payment → Inventory
4. At each step, Camunda calls the matching **Java delegate**; the delegate runs business logic (writes to `orders` / `payments` tables)
5. If inventory fails (amount > 500) → the Refund → Cancel path runs

**BPMN** = flow (when and in what order) · **Delegate** = Java code (what to do) · **Cockpit** = monitoring UI
