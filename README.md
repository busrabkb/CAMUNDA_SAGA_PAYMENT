# Camunda Order Saga Demo

Camunda 7 ile sipariş saga akışı (Create Order → Payment → Inventory, hata durumunda Refund → Cancel).

## Gereksinimler

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (Docker Compose dahil)

## Docker ile çalıştırma

Proje kök dizininde.

### Tek komut (önerilen)

Build + container başlatma birlikte:

```bash
docker compose up --build
```

### İki ayrı komut

`build` sadece image oluşturur, uygulamayı **çalıştırmaz**. Build sonrası mutlaka `up` gerekir:

```bash
docker compose build
docker compose up
```

Arka planda çalıştırmak için:

```bash
docker compose up -d
```

İlk çalıştırmada Maven build + image oluşturma birkaç dakika sürebilir.

Uygulama hazır olunca:

- API: `http://localhost:8080`
- Camunda Cockpit: `http://localhost:8080/camunda/app/cockpit/`
  - Kullanıcı: `demo`
  - Şifre: `demo`

## Test istekleri

### Başarılı sipariş (amount ≤ 500)

```bash
curl -X POST http://localhost:8080/orders ^
  -H "Content-Type: application/json" ^
  -d "{\"customerId\":\"ahmet\",\"amount\":100}"
```

Beklenen: `orderStatus=COMPLETED`, `paymentStatus=SUCCESS`

### Stok hatası + compensation (amount > 500)

```bash
curl -X POST http://localhost:8080/orders ^
  -H "Content-Type: application/json" ^
  -d "{\"customerId\":\"ahmet\",\"amount\":750}"
```

Beklenen: `orderStatus=CANCELLED`, `paymentStatus=REFUNDED`, `inventoryStatus=FAILED`

### Sipariş sorgulama

```bash
curl http://localhost:8080/orders/{orderId}
```

`{orderId}` yerine create cevabındaki `orderId` değerini kullanın.

## Durdurma

```bash
docker compose down
```

Veritabanı volume'ünü de silmek için:

```bash
docker compose down -v
```

## Servisler

| Servis   | Port | Açıklama                          |
|----------|------|-----------------------------------|
| app      | 8080 | Spring Boot + Camunda             |
| postgres | 5432 | PostgreSQL (`camunda_demo` DB)    |

İlk açılışta:

- Flyway → `orders`, `payments` tablolarını oluşturur
- Camunda → `ACT_*` tablolarını oluşturur
- BPMN → `order-saga.bpmn` otomatik deploy edilir

## Sorun giderme

**Port 8080 veya 5432 dolu**

`docker-compose.yml` içinde port mapping'i değiştirin veya çakışan servisi durdurun.

**App postgres'e bağlanamıyor**

Postgres healthcheck tamamlanana kadar app bekler. Logları kontrol edin:

```bash
docker compose logs -f app
```

**Maven dependency hatası (IDE)**

```bash
mvn clean compile
```

Camunda sürümü `pom.xml` içinde `camunda.version=7.23.0` olmalıdır.
