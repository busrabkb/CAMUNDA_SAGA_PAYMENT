# REQUIREMENT ANALYSIS

## Business Goal

Order Saga demo projesinde iki geliştirme hedefi var:

1. **Altyapı:** Projenin Docker ile hatasız ayağa kalkması (Camunda deploy + Flyway migration).
2. **Yeni özellik:** Başarıyla tamamlanan saga process'lerinde, process'i tamamlayan kullanıcı bilgisinin veritabanına kaydedilmesi ve sorgulanabilmesi.

Bu bir öğrenme projesidir; küçük, anlaşılır ve mevcut mimariye uyumlu adımlarla ilerlenmelidir.

---

## Main Flow

### Mevcut başarılı akış

```
Start → Create Order → Payment → Inventory → End (Complete Order)
```

### Mevcut compensation akışı (değişmeyecek)

```
Inventory (fail) → Refund Payment → Cancel Order → End
```

### Hedef başarılı akış

```
Start → Create Order → Payment → Inventory → Record Completion → End (Complete Order)
```

**Record Completion** adımı yalnızca happy path'te çalışır. Compensation yolunda completion kaydı oluşturulmaz.

---

## Affected Services

| Bileşen | Etki |
|---------|------|
| `order-saga` monolith (Spring Boot + Camunda) | Tüm değişiklikler bu uygulama içinde |
| PostgreSQL — business tabloları | Yeni `process_completions` tablosu |
| PostgreSQL — Camunda tabloları (ACT_*) | TASK-01 kapsamında; yeni feature etkilemez |
| REST API (`POST /orders`, `GET /orders/{id}`) | GET yanıtına completion alanları eklenir |
| BPMN (`order-saga.bpmn`) | Inventory sonrası yeni service task |
| Docker / config | TASK-01 kapsamında |

Microservice ayrımı yok; harici servis entegrasyonu yok.

---

## Important Decisions

| Karar | Gerekçe |
|-------|---------|
| Completion kaydı **yalnızca happy path**'te oluşturulur | Compensation = process başarısız; "tamamlayan kullanıcı" kavramı bu yola uymaz |
| Compensation / saga iptal yolu **değiştirilmez** | Mevcut `amount > 500` senaryosu ve boundary event akışı korunur |
| Camunda orchestration, business logic service katmanında kalır | Mevcut delegate → domain service → repository deseni |
| Yeni tablo Flyway ile yönetilir | `orders`, `payments` ile aynı yaklaşım |
| Aynı `process_instance_id` için tek completion kaydı | Duplicate kayıt engellenir |
| User Task **bu fazda eklenmez** | Mevcut akış tamamen otomatik service task'lardan oluşuyor |

---

## Open Questions

### ✅ Karar verildi

1. **"Process'i tamamlayan kullanıcı" kimdir?**
   - **Karar:** Tamamlayan kullanıcı bilgisi **endpoint'ten gelir** (`POST /orders` request body).
   - Mevcut akışta bu alan `customerId`; process başlatılırken Camunda variable olarak set edilir, Record Completion adımında `completed_by` olarak DB'ye yazılır.
   - User Task veya ayrı auth sistemi **bu fazda yok**.
   - Kod ve task notlarında açıkça belirtilmeli: *"completedBy, siparişi başlatan endpoint'ten gelen kullanıcı bilgisidir."*

2. **Completion kaydında ek alan gerekli mi?**
   - **Karar:** Hayır. Sadece: `order_id`, `process_instance_id`, `completed_by`, `completed_at`.

3. **Completion yokken API'de alanlar ne döner?**
   - **Karar:** `completedBy` ve `completedAt` alanları response'da **her zaman bulunur**; completion kaydı yoksa değerleri **`null`** döner.
   - Örnek: compensation path (`amount > 500`) → sipariş iptal, completion kaydı yok → `"completedBy": null, "completedAt": null`.

### ⏳ Hâlâ açık

4. **TASK-01 yerel commit (`b23d158`) ile zaten uygulanmış olabilir** — kapanmadan önce Docker ile doğrulanmalı.

---

## GITHUB TASKS

### TASK-01 — Docker'da Camunda ve Flyway ayağa kalkma hatasını gider

#### Amaç

Geliştirici ortamında `docker compose up --build` komutunun Camunda ve Flyway hataları olmadan tamamlanmasını sağlamak.

#### Business Requirement

Proje Docker ile ayağa kalkabilmeli; geliştirici altyapı sorunlarıyla uğraşmadan feature geliştirmeye geçebilmeli.

#### Scope

* Camunda 7.23 `historyTimeToLive` zorunluluğunu BPMN process tanımında karşıla
* Flyway'in mevcut `ACT_*` tabloları varken migration'a devam edebilmesi için gerekli yapılandırmayı ekle
* Docker compose ile uygulamanın ayağa kalktığını doğrula

#### Out of Scope

* Yeni business feature
* Saga akışına yeni adım ekleme
* Test yazma

#### Acceptance Criteria

- [ ] `docker compose up --build` hatasız tamamlanır
- [ ] Uygulama `http://localhost:8080` üzerinde erişilebilir
- [ ] Camunda ENGINE-12018 (`historyTimeToLive`) hatası oluşmaz
- [ ] Flyway migration mevcut şemada takılmaz

#### Dependencies

Yok

#### Expected Changes

* Backend
* Camunda/BPMN

#### Notes

Yerel ortamda commit `b23d158` bu değişiklikleri içerebilir; issue kapatılmadan önce Docker ile doğrulanmalı.

---

### TASK-02 — process_completions veritabanı migration'ı

#### Amaç

Başarıyla tamamlanan process'lerin completion bilgisini kalıcı olarak saklayacak veritabanı şemasını oluşturmak.

#### Business Requirement

Sistem, happy path'te tamamlanan her order saga için tamamlanma bilgisini veritabanında tutabilmeli.

#### Scope

* Flyway `V2__create_process_completions.sql` migration dosyası oluştur
* Tablo: `process_completions`
* Alanlar: `id`, `order_id`, `process_instance_id`, `completed_by`, `completed_at`
* `process_instance_id` üzerinde unique constraint
* Gerekli foreign key / index kararlarını mevcut tablo convention'ına göre uygula

#### Out of Scope

* Java kodu
* BPMN değişikliği
* API değişikliği

#### Acceptance Criteria

- [ ] Migration mevcut Flyway naming convention'ına uygun
- [ ] Tablo happy path completion kayıtlarını tutacak yapıda
- [ ] Aynı `process_instance_id` için ikinci kayıt engellenir
- [ ] Uygulama ayağa kalktığında migration hatasız uygulanır

#### Dependencies

TASK-01

#### Expected Changes

* Database

#### Notes

`orders` tablosundaki naming ve timestamp convention'ını takip et.

---

### TASK-03 — ProcessCompletion persistence model tanımı

#### Amaç

Completion kayıtlarını temsil edecek persistence model yapısını tanımlamak.

#### Business Requirement

Completion verisi uygulama katmanları arasında tip güvenli ve tutarlı şekilde taşınabilmeli.

#### Scope

* Mevcut `OrderRecord` / `PaymentRecord` pattern'ine uygun record veya entity tanımı
* Completion kaydının temel alanlarını kapsayan immutable yapı
* Naming ve package yapısı mevcut persistence katmanıyla tutarlı olmalı

#### Out of Scope

* Repository implementasyonu
* Business validation
* BPMN / API değişikliği

#### Acceptance Criteria

- [ ] Completion kaydı temsil eden persistence model mevcut
- [ ] Alanlar TASK-02 migration şemasıyla uyumlu
- [ ] Gereksiz abstraction veya ek katman eklenmemiş

#### Dependencies

TASK-02

#### Expected Changes

* Backend

#### Notes

Coding agent mevcut `infrastructure.persistence` paketindeki convention'ı inceleyerek karar vermelidir.

---

### TASK-04 — ProcessCompletion repository katmanı

#### Amaç

`process_completions` tablosuna okuma/yazma erişimini sağlayan repository katmanını oluşturmak.

#### Business Requirement

Completion kayıtları veritabanına yazılabilmeli; process instance veya order ile sorgulanabilmeli.

#### Scope

* Insert metodu
* `process_instance_id` ile lookup metodu
* Mevcut `OrderRepository` yapısına paralel, ince persistence katmanı

#### Out of Scope

* Business kuralları (duplicate kontrolü service'te)
* BPMN / delegate kodu
* API değişikliği

#### Acceptance Criteria

- [ ] Completion kaydı insert edilebilir
- [ ] `process_instance_id` ile kayıt bulunabilir
- [ ] Kod mevcut repository pattern'i ile tutarlı
- [ ] JDBC/SQL yaklaşımı projedeki diğer repository'lerle aynı

#### Dependencies

TASK-03

#### Expected Changes

* Backend

---

### TASK-05 — CompletionService iş mantığı

#### Amaç

Process tamamlanma kaydının business kurallarını service katmanında tanımlamak.

#### Business Requirement

Happy path'te saga tamamlandığında tamamlanma bilgisi tek seferlik olarak kaydedilmeli.

#### Scope

* Completion kayıt oluşturma servisi (mevcut domain yapısına uygun konumlandırma)
* Girdi: `orderId`, `processInstanceId`, `completedBy`
* Aynı `process_instance_id` için tekrar kayıt oluşturulmaması kuralı
* Servis için unit test'ler

#### Out of Scope

* BPMN / delegate implementasyonu
* REST API genişletmesi
* Compensation yolu

#### Acceptance Criteria

- [ ] İlk kayıt başarıyla oluşturulur
- [ ] Duplicate `process_instance_id` güvenli şekilde yönetilir
- [ ] Unit test'ler happy path ve duplicate senaryosunu kapsar
- [ ] Business logic delegate içinde değil service'te

#### Dependencies

TASK-04

#### Expected Changes

* Backend
* Test

#### Notes

`completedBy`, siparişi başlatan `POST /orders` endpoint'inden gelen kullanıcı bilgisidir (`customerId` process variable). Record Completion delegate bu değeri okuyup DB'ye yazar.

---

### TASK-06 — BPMN Record Completion service task ve delegate

#### Amaç

Happy path akışına Inventory sonrası completion kaydını tetikleyen Camunda adımını eklemek.

#### Business Requirement

Inventory başarıyla tamamlandıktan sonra, process sonlanmadan önce tamamlanma bilgisi kaydedilmeli.

#### Scope

* BPMN: Inventory → **Record Completion** → End akışını oluştur
* İnce `JavaDelegate` implementasyonu (mevcut delegate pattern)
* Process variable'lara completion bilgisi yaz (`completedBy`, `completedAt` — isimler mevcut convention'a uygun)
* Compensation / iptal yoluna dokunma

#### Out of Scope

* REST API değişikliği
* Entegrasyon testleri
* User Task ekleme

#### Acceptance Criteria

- [ ] Başarılı akış: Start → Create Order → Payment → Inventory → Record Completion → End
- [ ] İptal yolu aynen çalışır (`amount > 500` compensation senaryosu)
- [ ] Delegate orchestration yapar; iş mantığı TASK-05 service'inde kalır
- [ ] Process variable'lar set edilir

#### Dependencies

TASK-05

#### Expected Changes

* Backend
* Camunda/BPMN

#### Notes

Camunda orchestration yapar; business logic BPMN'e taşınmaz.

---

### TASK-07 — Order API'de completion bilgisini göster

#### Amaç

Tamamlanma bilgisinin sipariş detay API'si üzerinden okunabilmesini sağlamak.

#### Business Requirement

Sipariş sorgulandığında, başarıyla tamamlanan process'ler için tamamlanma bilgisi görülebilmeli.

#### Scope

* `GET /orders/{id}` yanıtına completion alanları ekle
* Completion kaydı yoksa (compensation veya henüz tamamlanmamış) davranışı netleştir
* Mevcut response yapısını bozmadan genişlet

#### Out of Scope

* Yeni endpoint
* BPMN değişikliği
* Entegrasyon testleri (TASK-08)

#### Acceptance Criteria

- [ ] Happy path'te tamamlanan siparişlerde completion bilgisi response'da görünür
- [ ] Completion kaydı olmayan siparişlerde davranış tutarlı ve documented
- [ ] Mevcut API contract'ı breaking change olmadan genişler

#### Dependencies

TASK-06

#### Expected Changes

* Backend
* API

#### Notes

`completedBy` ve `completedAt` response'da her zaman bulunur. Completion kaydı yoksa (compensation path) her iki alan `null` döner.

---

### TASK-08 — Completion feature entegrasyon testleri

#### Amaç

Completion feature'ının uçtan uca doğru çalıştığını otomatik testlerle kanıtlamak.

#### Business Requirement

Hem başarılı saga hem compensation senaryolarında completion davranışının doğru olduğu garanti altına alınmalı.

#### Scope

* Happy path entegrasyon testi: completion kaydı oluşur, API'den okunabilir
* Compensation path entegrasyon testi: completion kaydı oluşmaz
* Mevcut testlerin bozulmadığını doğrula

#### Out of Scope

* Yeni feature geliştirme
* BPMN / service kodu değişikliği (yalnızca test)

#### Acceptance Criteria

- [ ] `amount <= 500`: completion kaydı DB'de ve API response'da görünür
- [ ] `amount > 500`: completion kaydı oluşmaz
- [ ] Tüm mevcut testler geçer
- [ ] Testler mevcut `OrderSagaIntegrationTest` pattern'ini takip eder

#### Dependencies

TASK-07

#### Expected Changes

* Test

---

## IMPLEMENTATION ORDER

```
TASK-01 → TASK-02 → TASK-03 → TASK-04 → TASK-05 → TASK-06 → TASK-07 → TASK-08
```

| Sıra | Task | Neden bu sırada |
|------|------|-----------------|
| 1 | TASK-01 | Altyapı; diğer tüm task'ların ön koşulu |
| 2 | TASK-02 | Veritabanı şeması |
| 3 | TASK-03 | Persistence model |
| 4 | TASK-04 | Repository; service'in bağımlılığı |
| 5 | TASK-05 | Business logic; delegate'in bağımlılığı |
| 6 | TASK-06 | Camunda akışına bağlama |
| 7 | TASK-07 | API'den okuma |
| 8 | TASK-08 | Uçtan uca doğrulama |

**Paralel yapılamaz:** Her task bir öncekine bağlıdır (TASK-01 hariç).

**Open Questions TASK-05 öncesi netleştirilmeli:** `completedBy` kaynağı.
