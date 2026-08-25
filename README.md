# Camunda Order Saga Demo

> [English](README.en.md)

## Çalıştırma

Docker Desktop açıkken proje klasöründe:

```bash
docker compose up --build
```

Bu komut **hem build eder hem çalıştırır** (`build` + `up` birlikte). Terminal açık kalır; logları görürsün. Hazır olunca `http://localhost:8080` cevap verir.

Sadece build edip ayrı başlatmak istersen:

```bash
docker compose build
docker compose up
```

Durdurmak için: `Ctrl+C`, sonra `docker compose down`

## Örnek istek

```bash
curl -X POST http://localhost:8080/orders -H "Content-Type: application/json" -d "{\"customerId\":\"ahmet\",\"amount\":100}"
```

## Camunda Cockpit

Süreci görsel izlemek için tarayıcıda aç:

**http://localhost:8080/camunda/app/cockpit/**

- Kullanıcı: `demo`
- Şifre: `demo`

Cockpit → **Processes** → `orderSaga` → çalışan instance'ları ve adımları buradan görebilirsin.

## Camunda nasıl çalışıyor?

Bu projede **ayrı Camunda sunucusu yok**. Camunda, Spring Boot uygulamasının içinde gömülü çalışır.

1. App açılınca `order-saga.bpmn` deploy edilir, Postgres'te `ACT_*` tabloları oluşur
2. `POST /orders` → Camunda `orderSaga` sürecini başlatır
3. BPMN akışı adım adım ilerler: Create Order → Payment → Inventory
4. Her adımda Camunda ilgili **Java delegate**'i çağırır; delegate iş mantığını çalıştırır (`orders` / `payments` tablolarına yazar)
5. Inventory başarısız olursa (amount > 500) → Refund → Cancel yolu çalışır

**BPMN** = akış (ne zaman, hangi sırayla) · **Delegate** = Java kodu (ne yapılacak) · **Cockpit** = izleme ekranı
